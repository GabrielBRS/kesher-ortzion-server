package com.ortzion_technology.ortzion_telecom_server.configuration.rabbitMq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue filaHigienizacao() {
        return new Queue("fila.higienizacao", true);
    }
}