package com.ortzion_technology.ortzion_telecom_server.controller.v1.api.external.webhook.pagarme_stone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAsyncExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        logger.error("Exception caught in @Async method '{}'", method.getName(), ex);
        logger.error("Method parameters: {}", Arrays.toString(params));
    }
}
