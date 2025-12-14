package com.ortzion_technology.ortzion_telecom_server.security.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {
    private String documento;
    private String password;
}