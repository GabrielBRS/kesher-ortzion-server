package com.ortzion_technology.ortzion_telecom_server.configuration.banco_inter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.banco_digital.MeioPagamento;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.Compra;
import com.ortzion_technology.ortzion_telecom_server.service.internal.processar_pagamentos.MeioPagamentoService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.processar_pagamentos.ProcessarPagamentosService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class WebhookBancoInterService {

    private static final Logger log = LoggerFactory.getLogger(WebhookBancoInterService.class);

    private final MeioPagamentoService meioPagamentoService;
    private final ProcessarPagamentosService processarPagamentosService;
    private final ObjectMapper objectMapper;

    public WebhookBancoInterService(MeioPagamentoService meioPagamentoService,
                                    ProcessarPagamentosService processarPagamentosService,
                                    ObjectMapper objectMapper) {
        this.meioPagamentoService = meioPagamentoService;
        this.processarPagamentosService = processarPagamentosService;
        this.objectMapper = objectMapper;
    }

    @Async("taskProcessarWebhookInter")
    @Transactional
    public void processarNotificacao(String payloadJson) {
        try {
            List<Map<String, Object>> eventos = parsePayload(payloadJson);

            for (Map<String, Object> evento : eventos) {

                if (evento.containsKey("pix")) {
                    Object pixObj = evento.get("pix");
                    if (pixObj instanceof List) {
                        List<Map<String, Object>> listaInterna = (List<Map<String, Object>>) pixObj;
                        for (Map<String, Object> itemPix : listaInterna) {
                            processarEventoPix(itemPix);
                        }
                    } else if (pixObj instanceof Map) {
                        processarEventoPix((Map<String, Object>) pixObj);
                    }
                }
                else if (evento.containsKey("boleto")) {
                    Object bolObj = evento.get("boleto");
                    if (bolObj instanceof Map) {
                        processarEventoBoleto((Map<String, Object>) bolObj);
                    }
                }

                else if (evento.containsKey("endToEndId") && evento.containsKey("txid")) {
                    processarEventoPix(evento);
                }
                else if (evento.containsKey("nossoNumero") && (evento.containsKey("situacao") || evento.containsKey("status"))) {
                    processarEventoBoleto(evento);
                }
                else {
                    log.warn("Evento desconhecido do Inter ignorado: {}", evento);
                }
            }
        } catch (Exception e) {
            log.error("Erro fatal ao processar Webhook Inter", e);
        }
    }

    private void processarEventoPix(Map<String, Object> dados) {
        String txid = (String) dados.get("txid");
        Object valorObj = dados.get("valor");
        String valorStr = String.valueOf(valorObj);

        log.info("Webhook PIX identificado. TxId: {}, Valor: {}", txid, valorStr);

        MeioPagamento meioPagamento = meioPagamentoService.pegarMeioPagamentoPorIdTransacaoInter(txid);

        if (meioPagamento != null) {
            meioPagamento.setStatusInter("RECEBIDO_WEBHOOK");

            if (meioPagamento.getFormaPagamento() != null && meioPagamento.getFormaPagamento() == 3) {
                log.info("DETECTADO: Cliente pagou Boleto via Pix! Marcando flag...");
                meioPagamento.setBoletoInterCompensadoComoPix(true);
            }

            Compra compra = meioPagamento.getCompra();
            if (compra != null) {
                processarPagamentosService.processarPagamentoInter(
                        compra,
                        "PIX_PAGO",
                        new BigDecimal(valorStr)
                );

                meioPagamento.setStatusInter("PAGO");
                meioPagamento.setDataPagamento(LocalDateTime.now());
                meioPagamento.setDataConfirmacaoPagamento(LocalDateTime.now());
                meioPagamentoService.salvarMeioPagamento(meioPagamento);
            }
        } else {
            log.warn("MeioPagamento não encontrado para o Pix TxId: {}", txid);
        }
    }

    private void processarEventoBoleto(Map<String, Object> dados) {
        String nossoNumero = (String) dados.get("nossoNumero");
        String situacao = (String) dados.get("situacao");
        if (situacao == null) situacao = (String) dados.get("status");

        log.info("Webhook BOLETO identificado. NossoNumero: {}", nossoNumero);

        MeioPagamento meioPagamento = meioPagamentoService.pegarMeioPagamentoPorIdTransacaoInter(nossoNumero);

        if (meioPagamento != null) {
            meioPagamento.setStatusInter("RECEBIDO_" + situacao);

            Compra compra = meioPagamento.getCompra();

            if ("PAGO".equalsIgnoreCase(situacao) || "LIQUIDADO".equalsIgnoreCase(situacao) || "RECEBIDO".equalsIgnoreCase(situacao) || "MARCADO_RECEBIDO".equalsIgnoreCase(situacao)) {
                if (compra != null) {
                    processarPagamentosService.processarPagamentoInter(
                            compra,
                            situacao,
                            compra.getValorTotalCalculado()
                    );
                }
                meioPagamento.setStatusInter("PAGO");
                meioPagamento.setDataPagamento(LocalDateTime.now());
                meioPagamento.setDataConfirmacaoPagamento(LocalDateTime.now());

            } else if ("BAIXADO".equalsIgnoreCase(situacao) || "CANCELADO".equalsIgnoreCase(situacao)) {
                if (compra != null) {
                    processarPagamentosService.processarFalhaInter(compra, situacao);
                }
                meioPagamento.setStatusInter(situacao);
            }

            meioPagamentoService.salvarMeioPagamento(meioPagamento);
        } else {
            log.warn("MeioPagamento não encontrado para o Boleto NossoNumero: {}", nossoNumero);
        }
    }

    private List<Map<String, Object>> parsePayload(String json) throws Exception {
        JsonNode node = objectMapper.readTree(json);
        if (node.isArray()) {
            return objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {});
        } else {
            Map<String, Object> singleObj = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            return Collections.singletonList(singleObj);
        }
    }
}