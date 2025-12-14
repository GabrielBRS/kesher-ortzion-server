package com.ortzion_technology.ortzion_telecom_server.configuration.brevo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import brevo.ApiClient;
import brevoApi.TransactionalEmailsApi;
import brevo.auth.ApiKeyAuth;

@Configuration
public class BrevoConfig {

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Bean
    public ApiClient brevoApiClient() {
        ApiClient defaultClient = brevo.Configuration.getDefaultApiClient();

        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKey.setApiKey(brevoApiKey);

        return defaultClient;
    }

    @Bean
    public TransactionalEmailsApi transactionalEmailsApi(ApiClient apiClient) {
        return new TransactionalEmailsApi(apiClient);
    }
}