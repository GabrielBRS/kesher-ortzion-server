package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.response.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompraEmailResponse {

    private int emailsComprados;
    private BigDecimal valorDebitado;
    private BigDecimal novoSaldo;
    private String mensagem;

}
