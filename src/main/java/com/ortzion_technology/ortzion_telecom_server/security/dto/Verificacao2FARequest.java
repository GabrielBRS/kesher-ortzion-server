package com.ortzion_technology.ortzion_telecom_server.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Verificacao2FARequest {
    private String documento;
    private String codigo2FA;
}
