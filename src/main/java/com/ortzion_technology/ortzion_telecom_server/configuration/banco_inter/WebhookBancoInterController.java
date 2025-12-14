package com.ortzion_technology.ortzion_telecom_server.configuration.banco_inter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhook/inter")
public class WebhookBancoInterController {

    private final WebhookBancoInterService webhookService;
    private final BancoInterService bancoInterService;

    public WebhookBancoInterController(WebhookBancoInterService webhookService, BancoInterService bancoInterService) {
        this.webhookService = webhookService;
        this.bancoInterService = bancoInterService;
    }

    @PostMapping
    public ResponseEntity<Void> receberNotificacao(@RequestBody String payload) {
        webhookService.processarNotificacao(payload);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/configurar-webhook")
    public ResponseEntity<String> configurarWebhook(
            @RequestParam String chavePix,
            @RequestParam String urlWebhook) {
        bancoInterService.configurarWebhook(chavePix, urlWebhook);
        return ResponseEntity.ok("Webhook configurado com sucesso para: " + urlWebhook);
    }

}