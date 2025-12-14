package com.ortzion_technology.ortzion_telecom_server.entity.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Data
public class RelatorioSistelcomDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idUsuario;
    private Integer tipoPessoa;
    private Long idSubjectus;
    private Integer idDepartamento;
    private String codigoCampanha;
    private String campanha;

    private Long total;
    private Long enviados;
    private Long entregue;
    private Long naoEntregue;
    private Long higienizado;
    private Long cancelado;
    private Long retornos;
    private BigDecimal valorTarifado;

    private String empresa;
    private String fornecedor;
    private LocalDateTime dataAgendada;
    private String destino;
    private String campoInfo;
    private String operadora;
    private LocalDateTime dataEnvio;
    private LocalDateTime dataConfirmacao;
    private String mensagem;
    private String situacao;
    private String identificador;

    private byte[] relatoriosExcel;

    public RelatorioSistelcomDTO(
            Long idUsuario, Integer tipoPessoa, Long idSubjectus, Integer idDepartamento, String codigoCampanha,
            Long total, Long enviados, Long entregue, Long naoEntregue, Long higienizado,
            Long cancelado, Long retornos, BigDecimal valorTarifado, String fornecedor, String campanha,
            LocalDateTime dataAgendada, String destino, String campoInfo, String operadora,
            LocalDateTime dataEnvio, LocalDateTime dataConfirmacao, String mensagem, String situacao,
            String identificador) {
        this.idUsuario = idUsuario;
        this.tipoPessoa = tipoPessoa;
        this.idSubjectus = idSubjectus;
        this.idDepartamento = idDepartamento;
//        this.empresa = empresa;
        this.codigoCampanha = codigoCampanha;
        this.total = total;
        this.enviados = enviados;
        this.entregue = entregue;
        this.naoEntregue = naoEntregue;
        this.higienizado = higienizado;
        this.cancelado = cancelado;
        this.retornos = retornos;
        this.valorTarifado = valorTarifado;
        this.fornecedor = fornecedor;
        this.campanha = campanha;
        this.dataAgendada = dataAgendada;
        this.destino = destino;
        this.campoInfo = campoInfo;
        this.operadora = operadora;
        this.dataEnvio = dataEnvio;
        this.dataConfirmacao = dataConfirmacao;
        this.mensagem = mensagem;
        this.situacao = situacao;
        this.identificador = identificador;
    }

}