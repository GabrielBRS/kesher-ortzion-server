package com.ortzion_technology.ortzion_telecom_server.entity.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RelatorioAnaliticoDTO {

    private String descricaoCampanhaMensageria;
    private String nomeEmissor;
    private String nomeDepartamentoEmissor;
    private String canalMensageria;
    private String destino;
    private String mensagemDestinatario;
    private Integer retorno;
    private String fornecedor;
    private LocalDateTime dataInicio;
    private LocalDateTime dataAgendada;
    private LocalDateTime dataEnvio;
    private LocalDateTime dataConfirmacao;
    private String operadora;
    private String situacao;
    private String campoInfo;
    private String identificador;
    private String plataformaEnvio;

    public RelatorioAnaliticoDTO(String descricaoCampanhaMensageria, String nomeEmissor, String nomeDepartamentoEmissor,
                                 String canalMensageria, String destino, String mensagemDestinatario,
                                 Integer retorno, String fornecedor, LocalDateTime dataInicio,
                                 LocalDateTime dataAgendada, LocalDateTime dataEnvio, LocalDateTime dataConfirmacao,
                                 String operadora, String situacao, String campoInfo,
                                 String identificador, String plataformaEnvio) {
        this.descricaoCampanhaMensageria = descricaoCampanhaMensageria;
        this.nomeEmissor = nomeEmissor;
        this.nomeDepartamentoEmissor = nomeDepartamentoEmissor;
        this.canalMensageria = canalMensageria;
        this.destino = destino;
        this.mensagemDestinatario = mensagemDestinatario;
        this.retorno = retorno;
        this.fornecedor = fornecedor;
        this.dataInicio = dataInicio;
        this.dataAgendada = dataAgendada;
        this.dataEnvio = dataEnvio;
        this.dataConfirmacao = dataConfirmacao;
        this.operadora = operadora;
        this.situacao = situacao;
        this.campoInfo = campoInfo;
        this.identificador = identificador;
        this.plataformaEnvio = plataformaEnvio;
    }

}