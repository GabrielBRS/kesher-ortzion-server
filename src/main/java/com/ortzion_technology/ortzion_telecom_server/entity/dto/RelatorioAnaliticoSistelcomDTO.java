package com.ortzion_technology.ortzion_telecom_server.entity.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioAnaliticoSistelcomDTO {

    private String empresa;
    private String fornecedor;
    private String codigoCampanha;
    private LocalDateTime dataAgendada;
    private String destino;
    private String identificador;
    private Integer quantidade;
    private String campoInfo;
    private String operadora;
    private LocalDateTime dataEnvio;
    private LocalDateTime dataConfirmacao;
    private String mensagem;
    private String situacao;
    private String nomeArquivo;
    private Integer retornos;
    private String cpfCnpj;

}
