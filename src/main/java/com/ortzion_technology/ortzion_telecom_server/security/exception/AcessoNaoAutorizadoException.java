package com.ortzion_technology.ortzion_telecom_server.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AcessoNaoAutorizadoException extends RuntimeException {

    public AcessoNaoAutorizadoException(String message) {
        super(message);
    }

}
