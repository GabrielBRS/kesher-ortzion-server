package com.ortzion_technology.ortzion_telecom_server.service.internal.processar_pagamentos;

import com.ortzion_technology.ortzion_telecom_server.configuration.mercado_virtual.MercadoVirtualCacheService;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.*;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.pagarme.WebhookPagarmeResponseDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.enums.log.StatusPagamentoEnum;
import com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual.EstoqueMercadoriaVirtualService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual.CompraService;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class ProcessarPagamentosService {

    private final EstoqueMercadoriaVirtualService estoqueMercadoriaVirtualService;
    private final CompraService compraService;
    private static final Logger log = LoggerFactory.getLogger(ProcessarPagamentosService.class);
    private final MercadoVirtualCacheService mercadoVirtualCacheService;

    public ProcessarPagamentosService(EstoqueMercadoriaVirtualService estoqueMercadoriaVirtualService, CompraService compraService, MercadoVirtualCacheService mercadoVirtualCacheService) {
        this.estoqueMercadoriaVirtualService = estoqueMercadoriaVirtualService;
        this.compraService = compraService;
        this.mercadoVirtualCacheService = mercadoVirtualCacheService;
    }

    @Transactional
    public void acrescentarEstoqueEmLote(List<Compra> comprasPagas) {
        if (comprasPagas == null || comprasPagas.isEmpty()) return;

        log.info("LOTE: Processando estoque para {} compras.", comprasPagas.size());

        for (Compra compra : comprasPagas) {
            try {
                if (jaEstaFinalizado(compra)) continue;
                acrescentarEstoqueIndividual(compra);
            } catch (Exception e) {
                log.error("Erro ao processar estoque da compra ID " + compra.getIdCompra(), e);
            }
        }
    }

    @Transactional
    public void processarOrderPaid(WebhookPagarmeResponseDTO.OrderDataDTO orderData, Compra compra) {
        log.info("PAGAR.ME: Processando 'order.paid' para Pedido ID: {}", compra.getIdCompra());
        if (jaEstaFinalizado(compra)) return;

        atualizarStatusCompra(compra, StatusPagamentoEnum.PAGO, orderData.getCharges().getFirst().getStatus());
        acrescentarEstoqueIndividual(compra);
    }

    @Transactional
    public void processarChargePaid(WebhookPagarmeResponseDTO.ChargeDTO chargeData, Compra compra) {
        if (!jaEstaFinalizado(compra)) {
            atualizarStatusCompra(compra, StatusPagamentoEnum.PROCESSANDO, chargeData.getStatus());
        }
    }

    @Transactional
    public void processarPagamentoFalhou(WebhookPagarmeResponseDTO.OrderDataDTO orderData, Compra compra) {
        String chargeStatus = orderData.getCharges().getFirst().getStatus();
        atualizarStatusCompra(compra, StatusPagamentoEnum.FALHO, chargeStatus);
    }

    @Transactional
    public void processarChargePaymentFailed(WebhookPagarmeResponseDTO.ChargeDTO chargeData, Compra compra) {
        atualizarStatusCompra(compra, StatusPagamentoEnum.FALHO, chargeData.getStatus());
    }

    @Transactional
    public void processarChargeRefunded(WebhookPagarmeResponseDTO.ChargeDTO chargeData, Compra compra) {
        log.warn("PAGAR.ME: Estorno Pedido ID: {}", compra.getIdCompra());
        atualizarStatusCompra(compra, StatusPagamentoEnum.ESTORNADO, chargeData.getStatus());
    }

    @Transactional
    public void processarChargePaymentUnderpaid(WebhookPagarmeResponseDTO.ChargeDTO chargeData, Compra compra) {
        atualizarStatusCompra(compra, StatusPagamentoEnum.INSUFICENTE, chargeData.getStatus());
    }

    @Transactional
    public void processarChargePaymentOverpaid(WebhookPagarmeResponseDTO.ChargeDTO chargeData, Compra compra) {
        atualizarStatusCompra(compra, StatusPagamentoEnum.PAGO_A_MAIS, chargeData.getStatus());
    }

    @Transactional
    public void processarPagamentoInter(Compra compra, String statusInter, BigDecimal valorPago) {
        log.info("BANCO INTER: Processando pagamento Pedido ID: {}", compra.getIdCompra());

        if (jaEstaFinalizado(compra)) {
            log.info("Pedido ID {} já processado. Ignorando.", compra.getIdCompra());
            return;
        }

        atualizarStatusCompra(compra, StatusPagamentoEnum.PAGO, statusInter);
        acrescentarEstoqueIndividual(compra);
    }

    @Transactional
    public void processarFalhaInter(Compra compra, String statusInter) {
        log.warn("BANCO INTER: Falha/Cancelamento Pedido ID: {}", compra.getIdCompra());
        if (!jaEstaFinalizado(compra)) {
            atualizarStatusCompra(compra, StatusPagamentoEnum.FALHO, statusInter);
        }
    }

    private void acrescentarEstoqueIndividual(Compra compra) {

        Hibernate.initialize(compra.getItemPedidos());

        for (ItemPedido itemPedido : compra.getItemPedidos()) {

            Integer idPacote = itemPedido.getPacoteCanalMensageria().getIdPacoteCanalMensageria();

            PacoteCanalMensageria pacoteCached = mercadoVirtualCacheService.getPacote(idPacote);

            if (pacoteCached == null) {
                log.error("ERRO CRÍTICO: Pacote ID {} não encontrado no cache! Ignorando.", idPacote);
                continue;
            }

            for (ItemPacoteCanalMensageria config : pacoteCached.getItemPacoteCanalMensageria()) {

                CanalMensageria canal = config.getCanalMensageria();
                Long qtdNoPacote = config.getQuantidade();

                EstoqueMercadoriaVirtual estoque = estoqueMercadoriaVirtualService.pegarEstoquePorCanalMensageria(compra, canal);

                long totalCreditar = itemPedido.getQuantidade() * qtdNoPacote;

                estoqueMercadoriaVirtualService.acrescentarEstoque(estoque, totalCreditar);

                log.info("CREDITADO: +{} no canal {}", totalCreditar, canal.getNomeCanalMensageria());
            }
        }

        compra.setStatusPagamento(StatusPagamentoEnum.CONCLUIDO.getCodigoNumerico());
        compraService.salvar(compra);
    }

    private boolean jaEstaFinalizado(Compra compra) {
        Integer status = compra.getStatusPagamento();
        return status.equals(StatusPagamentoEnum.CONCLUIDO.getCodigoNumerico());
    }

    private void atualizarStatusCompra(Compra compra, StatusPagamentoEnum statusInterno, String statusExterno) {
        compra.setStatusPagamento(statusInterno.getCodigoNumerico());
        if (compra.getMeioPagamento() != null) {
            compra.getMeioPagamento().setStatusPagarme(statusExterno);
            compra.getMeioPagamento().setStatusInter(statusExterno);
            compra.getMeioPagamento().setStatusProcessamentoPagamento(statusInterno.getCodigoNumerico());
            compra.getMeioPagamento().setDataProcessamentoPagamento(LocalDateTime.now());

            if (statusInterno == StatusPagamentoEnum.PAGO) {
                compra.getMeioPagamento().setDataConfirmacaoPagamento(LocalDateTime.now());
            }
        }
        compraService.salvar(compra);
    }

}