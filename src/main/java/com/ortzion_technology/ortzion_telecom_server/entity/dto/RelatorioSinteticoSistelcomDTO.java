package com.ortzion_technology.ortzion_telecom_server.entity.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioSinteticoSistelcomDTO {

    private Long idRelatorioSinteticoSistelcom;
    private String empresa;
    private String codigoCampanha;
    private String dataMesReferencia;
    private Long total = 0L;
    private Long aEnviar = 0L;
    private Long enviados = 0L;
    private Long entregue = 0L;
    private Long naoEntregue = 0L;
    private Long higienizado = 0L;
    private Long invalido = 0L;
    private Long cancelado = 0L;
    private Long retornos = 0L;
    private Long quantidade = 0L;
    private BigDecimal valorTarifado;

}
