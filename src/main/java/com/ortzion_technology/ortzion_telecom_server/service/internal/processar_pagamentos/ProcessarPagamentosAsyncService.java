package com.ortzion_technology.ortzion_telecom_server.service.internal.processar_pagamentos;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.*;
import com.ortzion_technology.ortzion_telecom_server.entity.enums.log.StatusPagamentoEnum;
import com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual.EstoqueMercadoriaVirtualService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual.CompraService;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProcessarPagamentosAsyncService {

    private final EstoqueMercadoriaVirtualService estoqueMercadoriaVirtualService;
    private final CompraService compraService;
    private final ProcessarPagamentosService processarPagamentosService;
    private static final Logger log = LoggerFactory.getLogger(ProcessarPagamentosAsyncService.class);

    public ProcessarPagamentosAsyncService(EstoqueMercadoriaVirtualService estoqueMercadoriaVirtualService,
                                           CompraService compraService,
                                           ProcessarPagamentosService processarPagamentosService) {
        this.estoqueMercadoriaVirtualService = estoqueMercadoriaVirtualService;
        this.compraService = compraService;
        this.processarPagamentosService = processarPagamentosService;
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processarPagamentosPendentesDeLiberacao() {
        List<Compra> comprasPagas = compraService.pegarComprasPagasParaProcessar(StatusPagamentoEnum.PAGO.getCodigoNumerico());
        this.processarPagamentosService.acrescentarEstoqueEmLote(comprasPagas);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void processarComprasEstornadas() {
        log.debug("Executando verificação de compras estornadas.");
        List<Compra> comprasEstornadas = compraService.pegarComprasPagasParaProcessar(StatusPagamentoEnum.ESTORNADO.getCodigoNumerico());

        if (!comprasEstornadas.isEmpty()) {
            log.info("Encontradas {} compras estornadas para debitar estoque.", comprasEstornadas.size());
            for (Compra compra : comprasEstornadas) {
                Hibernate.initialize(compra.getItemPedidos());
                manipularEstorno(compra);
                compra.setStatusPagamento(StatusPagamentoEnum.CANCELADO_FINALIZADO.getCodigoNumerico());
                compraService.salvar(compra);
            }
        }
    }

    private void manipularEstorno(Compra compra) {
        if (compra.getItemPedidos() == null) return;

        for (ItemPedido itemPedido : compra.getItemPedidos()) {

            Hibernate.initialize(itemPedido.getPacoteCanalMensageria());
            PacoteCanalMensageria pacote = itemPedido.getPacoteCanalMensageria();

            Hibernate.initialize(pacote.getItemPacoteCanalMensageria());

            for (ItemPacoteCanalMensageria configPacote : pacote.getItemPacoteCanalMensageria()) {

                EstoqueMercadoriaVirtual estoque = estoqueMercadoriaVirtualService.pegarEstoquePorCanalMensageria(
                        compra, configPacote.getCanalMensageria()
                );

                if (estoque != null) {

                    long quantidadeTotal = itemPedido.getQuantidade() * configPacote.getQuantidade();

                    estoqueMercadoriaVirtualService.subtrairEstoque(estoque, quantidadeTotal);

                    log.info("Estoque debitado: -{} no canal {} (Compra {})",
                            quantidadeTotal,
                            configPacote.getCanalMensageria().getNomeCanalMensageria(),
                            compra.getIdCompra());
                } else {
                    log.warn("Impossível estornar: Estoque não encontrado para usuário {} e canal {}",
                            compra.getIdSubjectus(),
                            configPacote.getCanalMensageria().getIdCanalMensageria());
                }
            }
        }
    }

}