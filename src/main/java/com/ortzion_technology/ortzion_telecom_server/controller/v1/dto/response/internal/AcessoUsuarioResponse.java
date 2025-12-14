package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.response.internal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class AcessoUsuarioResponse {

    private String token;
    private String email;
    private String nome;
    private Collection<? extends GrantedAuthority> roles;

    public AcessoUsuarioResponse(String token, String email, String nome, Collection<? extends GrantedAuthority> roles) {
        this.token = token;
        this.email = email;
        this.nome = nome;
        this.roles = roles;
    }



}
