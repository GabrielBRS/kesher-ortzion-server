package com.ortzion_technology.ortzion_telecom_server.configuration.async_config;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.api.external.webhook.pagarme_stone.CustomAsyncExceptionHandler;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig implements AsyncConfigurer {

    @Bean(name = "taskExecutorEmailLogin")
    public Executor taskExecutorEmailLogin() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("Async-Email-Login-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "taskExecutorSMS")
    public Executor taskExecutorAlta() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("Async-SMS-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "taskProcessarPagamentoPagarme")
    public Executor taskProcessarPagamentoPagarme() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("Async-ProcessarPagamentoPagarme-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "taskProcessarWebhookPagarme")
    public Executor threadPoolTaskPagarme() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("WebhookAsync-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "taskProcessarWebhookInter")
    public Executor threadPoolTaskInter() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("WebhookInterAsync-");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncExceptionHandler();
    }

}
