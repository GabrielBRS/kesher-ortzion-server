package com.ortzion_technology.ortzion_telecom_server.configuration.rest_template;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateBean {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
