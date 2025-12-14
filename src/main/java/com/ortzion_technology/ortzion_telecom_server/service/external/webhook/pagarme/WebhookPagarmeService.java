package com.ortzion_technology.ortzion_telecom_server.service.external.webhook.pagarme;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.Compra;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.pagarme.PagarmeWebhookEvent;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.pagarme.WebhookPagarmeResponseDTO;
import com.ortzion_technology.ortzion_telecom_server.repository.pagarme.PagarmeWebhookEventRepository;
import com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual.CompraService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.processar_pagamentos.ProcessarPagamentosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class WebhookPagarmeService {

    private final ObjectMapper objectMapper;
    private final CompraService compraService;
    private final ProcessarPagamentosService processarPagamentosService;
    private final PagarmeWebhookEventRepository webhookEventRepository;
    private static final Logger log = LoggerFactory.getLogger(WebhookPagarmeService.class);

    public WebhookPagarmeService(ObjectMapper objectMapper, CompraService compraService, ProcessarPagamentosService processarPagamentosService, PagarmeWebhookEventRepository webhookEventRepository) {
        this.objectMapper = objectMapper;
        this.compraService = compraService;
        this.processarPagamentosService = processarPagamentosService;
        this.webhookEventRepository = webhookEventRepository;
    }

    @Async("taskProcessarWebhookPagarme")
    @Transactional
    public void processarWebhook(String rawPayload) {
        WebhookPagarmeResponseDTO webhook;
        try {
            webhook = objectMapper.readValue(rawPayload, WebhookPagarmeResponseDTO.class);
        } catch (Exception e) {
            log.error("Falha irrecuperável ao converter o payload do webhook para DTO. Payload: {}", rawPayload, e);
            return;
        }

        PagarmeWebhookEvent event = new PagarmeWebhookEvent(webhook.getId(), "RECEBIDO", rawPayload);
        try {
            webhookEventRepository.saveAndFlush(event);
        } catch (DataIntegrityViolationException e) {
            log.warn("Webhook duplicado recebido e ignorado. Event ID: {}", webhook.getId());
            return;
        }

        if (webhook.getData() == null || webhook.getData().getCharges() == null || webhook.getData().getCharges().isEmpty()) {
            log.warn("Webhook para o evento {} não continha 'charges' ou 'data'. Order ID: {}", webhook.getType(), webhook.getData().getId());
            atualizarStatusEvento(event, "FALHA");
            return;
        }

        String eventType = webhook.getType();
        WebhookPagarmeResponseDTO.OrderDataDTO orderData = webhook.getData();
        String chargeId = orderData.getCharges().getFirst().getId();
        String chargeCode = orderData.getCharges().getFirst().getCode();

        Compra compra = compraService.pegarHistoricoCompraWebhook(chargeId, chargeCode);
        if (compra == null) {
            log.warn("Nenhum HistoricoCompra encontrado para o webhook do evento '{}' com Charge ID '{}'", eventType, chargeId);
            atualizarStatusEvento(event, "FALHA");
            return;
        }

        processarEvento(eventType, orderData, compra);

        atualizarStatusEvento(event, "PROCESSADO");
    }

    private void processarEvento(String eventType, WebhookPagarmeResponseDTO.OrderDataDTO orderData, Compra compra) {
        switch (eventType) {
            case "order.paid":
                processarPagamentosService.processarOrderPaid(orderData, compra);
                break;
            case "order.payment_failed":
                processarPagamentosService.processarPagamentoFalhou(orderData, compra);
                break;
            case "charge.paid":
                processarPagamentosService.processarChargePaid(orderData.getCharges().getFirst(), compra);
                break;
            case "charge.refunded":
                processarPagamentosService.processarChargeRefunded(orderData.getCharges().getFirst(), compra);
                break;
            case "charge.payment_failed":
                processarPagamentosService.processarChargePaymentFailed(orderData.getCharges().getFirst(), compra);
                break;
            case "charge.underpaid":
                processarPagamentosService.processarChargePaymentUnderpaid(orderData.getCharges().getFirst(), compra);
                break;
            case "charge.overpaid":
                processarPagamentosService.processarChargePaymentOverpaid(orderData.getCharges().getFirst(), compra);
                break;
            default:
                log.info("Evento webhook não processado: {}. Charge ID: {}", eventType, orderData.getCharges().getFirst().getId());
                break;
        }
    }

    private void atualizarStatusEvento(PagarmeWebhookEvent event, String status) {
        event.setStatus(status);
        event.setProcessedAt(LocalDateTime.now());
        webhookEventRepository.save(event);
    }

//    @Async("taskProcessarWebhookPagarme")
//    public void processarWebhook(String rawPayload) {
//
//        WebhookPagarmeResponseDTO webhook;
//        try {
//            webhook = objectMapper.readValue(rawPayload, WebhookPagarmeResponseDTO.class);
//        } catch (Exception e) {
//            log.error("Falha ao converter o payload do webhook para DTO. Payload: {}", rawPayload, e);
//            return; // Falha irrecuperável
//        }
//
//        // ETAPA DE IDEMPOTÊNCIA
//        PagarmeWebhookEvent event = new PagarmeWebhookEvent(webhook.getId(), "RECEBIDO", rawPayload);
//        try {
//            webhookEventRepository.save(event);
//        } catch (DataIntegrityViolationException e) {
//            // Violação de chave primária significa que já recebemos este evento.
//            log.warn("Webhook duplicado recebido e ignorado. Event ID: {}", webhook.getId());
//            return; // Sucesso, já foi processado.
//        }
//
//        try {
//            webhook = objectMapper.readValue(rawPayload, WebhookPagarmeResponseDTO.class);
//        } catch (Exception e) {
//            log.error("Falha ao converter o payload do webhook para DTO. Payload: {}", rawPayload, e);
//            return;
//        }
//
//        if (webhook == null || webhook.getType() == null || webhook.getData() == null) {
//            log.warn("Webhook recebido nulo ou com dados essenciais faltando.");
//            return;
//        }
//
//        String eventType = webhook.getType();
//        WebhookPagarmeResponseDTO.OrderDataDTO orderData = webhook.getData();
//
//        if (orderData.getCharges() == null || orderData.getCharges().isEmpty()) {
//            log.warn("Webhook para o evento {} não continha 'charges'. Order ID: {}", eventType, orderData.getId());
//            return;
//        }
//
//        String chargeId = orderData.getCharges().getFirst().getId();
//        String chargeCode = orderData.getCharges().getFirst().getCode();
//
//        HistoricoCompra historicoCompra = historicoCompraService.pegarHistoricoCompraWebhook(chargeId, chargeCode);
//        if (historicoCompra == null) {
//            log.warn("Nenhum HistoricoCompra encontrado para o webhook do evento '{}' com Charge ID '{}'", eventType, chargeId);
//            return;
//        }
//
//        switch (eventType) {
//            case "order.paid":
//                processarPagamentosService.processarOrderPaid(orderData, historicoCompra);
//                break;
//            case "order.payment_failed":
//                processarPagamentosService.processarPagamentoFalhou(orderData, historicoCompra);
//                break;
//            case "charge.paid":
//                processarPagamentosService.processarChargePaid(orderData.getCharges().getFirst(), historicoCompra);
//                break;
//            case "charge.refunded":
//                processarPagamentosService.processarChargeRefunded(orderData.getCharges().getFirst(), historicoCompra);
//                break;
//            case "charge.payment_failed":
//                processarPagamentosService.processarChargePaymentFailed(orderData.getCharges().getFirst(), historicoCompra);
//                break;
//            case "charge.underpaid":
//                processarPagamentosService.processarChargePaymentUnderpaid(orderData.getCharges().getFirst(), historicoCompra);
//                break;
//            case "charge.overpaid":
//                processarPagamentosService.processarChargePaymentOverpaid(orderData.getCharges().getFirst(), historicoCompra);
//                break;
//            default:
//                log.info("Evento webhook não processado: {}. Charge ID: {}", eventType, chargeId);
//                break;
//        }
//    }

}