//package com.ortzion_technology.ortzion_telecom_server.controller.v1.api.external.webhook.pagarme_stone;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.nio.charset.StandardCharsets;
//import java.security.InvalidKeyException;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.HexFormat;
//
//@Service
//public class PagarmeSignatureValidator {
//
//    private static final Logger log = LoggerFactory.getLogger(PagarmeSignatureValidator.class);
//
//    @Value("${pagarme.webhook.secret-key}")
//    private String secretKey;
//
//    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
//    private static final String SIGNATURE_PREFIX = "sha256=";
//
//    public boolean isValid(String signatureHeader, byte[] payload) {
//        if (signatureHeader == null ||!signatureHeader.startsWith(SIGNATURE_PREFIX)) {
//            log.warn("Assinatura de webhook inválida. Cabeçalho ausente ou com formato incorreto.");
//            return false;
//        }
//
//        String receivedSignature = signatureHeader.substring(SIGNATURE_PREFIX.length());
//
//        try {
//            Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
//            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_SHA256_ALGORITHM);
//            mac.init(secretKeySpec);
//
//            byte[] calculatedHash = mac.doFinal(payload);
//            String calculatedSignature = HexFormat.of().formatHex(calculatedHash);
//
//            boolean signaturesMatch = MessageDigest.isEqual(
//                    calculatedSignature.getBytes(StandardCharsets.UTF_8),
//                    receivedSignature.getBytes(StandardCharsets.UTF_8)
//            );
//
//            if (!signaturesMatch) {
//                log.warn("Falha na validação da assinatura do webhook. Assinatura recebida não corresponde à calculada.");
//            }
//
//            return signaturesMatch;
//
//        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
//            log.error("Erro crítico de configuração de criptografia ao validar assinatura de webhook", e);
//            throw new RuntimeException("Falha ao calcular a assinatura HMAC", e);
//        }
//    }
//
//}