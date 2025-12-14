package com.ortzion_technology.ortzion_telecom_server.configuration.banco_inter;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;

@Configuration
public class InterHttpClientConfig {

    private final BancoInterConfiguration interConfig;
    private final ResourceLoader resourceLoader;

    public InterHttpClientConfig(BancoInterConfiguration interConfig, ResourceLoader resourceLoader) {
        this.interConfig = interConfig;
        this.resourceLoader = resourceLoader;
    }

    @Bean("interRestTemplate")
    public RestTemplate interRestTemplate() throws Exception {

        // 1. Carregar o certificado .p12
        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        try (InputStream inputStream = resourceLoader.getResource(interConfig.certPath()).getInputStream()) {
            clientStore.load(inputStream, interConfig.certPassword().toCharArray());
        }

        // 2. Criar o SSLContext com a chave privada carregada
        SSLContext sslContext = SSLContextBuilder.create()
                .loadKeyMaterial(clientStore, interConfig.certPassword().toCharArray())
                .build();

        // 3. Configurar o Socket Factory para usar esse SSLContext (Versão HttpClient 5)
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext);

        // 4. Configurar o Connection Manager (Necessário no HttpClient 5)
        PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslSocketFactory)
                .build();

        // 5. Criar o HttpClient usando o Connection Manager seguro
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();

        // 6. Vincular ao RestTemplate do Spring
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }
}