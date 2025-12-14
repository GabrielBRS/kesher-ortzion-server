package com.ortzion_technology.ortzion_telecom_server.configuration.pagarme_stone;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("banco.pagarme.api")
public record PagarmeConfiguration(String key, String url, String clientSecret, String idWebhook) {};
