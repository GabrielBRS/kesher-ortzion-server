package com.ortzion_technology.ortzion_telecom_server.configuration.banco_inter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "banco.inter.api")
public record BancoInterConfiguration(
        String clientId,
        String clientSecret,
        String certPath,
        String certPassword,
        String urlBase,
        String authUrl,
        String chavePix,
        String webhookUrl
) {}