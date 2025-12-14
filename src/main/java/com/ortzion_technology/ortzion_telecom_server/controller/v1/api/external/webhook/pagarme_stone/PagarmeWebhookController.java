package com.ortzion_technology.ortzion_telecom_server.controller.v1.api.external.webhook.pagarme_stone;

import com.ortzion_technology.ortzion_telecom_server.service.external.webhook.pagarme.WebhookPagarmeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
public class PagarmeWebhookController {

//    private final PagarmeSignatureValidator signatureValidator;
    private final WebhookPagarmeService webhookProcessingService;

    public PagarmeWebhookController(WebhookPagarmeService webhookProcessingService) {
        this.webhookProcessingService = webhookProcessingService;
    }

    @PostMapping("/pagarme")
    public ResponseEntity<Void> handlePagarmeWebhook(@RequestBody String payloadAsString) {

        webhookProcessingService.processarWebhook(payloadAsString);
        return ResponseEntity.ok().build();
    }



//    @PostMapping("/pagarme")
//    public ResponseEntity<Void> handlePagarmeWebhook(
//            @RequestHeader("X-Hub-Signature-256") String signature,
//            ContentCachingRequestWrapper request) {
//
//        byte[] body = request.getContentAsByteArray();
//
//        if (!signatureValidator.isValid(signature, body)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        String payloadAsString = new String(body, StandardCharsets.UTF_8);
//        webhookProcessingService.processarWebhook(payloadAsString);
//
//        return ResponseEntity.ok().build();
//    }

}