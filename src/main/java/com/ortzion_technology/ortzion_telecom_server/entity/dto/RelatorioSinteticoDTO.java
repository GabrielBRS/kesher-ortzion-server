package com.ortzion_technology.ortzion_telecom_server.entity.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class RelatorioSinteticoDTO {

    // Corresponde a: cm.campanha
    private String campanha;
    // Corresponde a: cm.nome_emissor
    private String nomeEmissor;
    // Corresponde a: cm.nome_departamento_emissor
    private String nomeDepartamentoEmissor;
    // Corresponde a: cm.canal_mensageria
    private String canalMensageria; // <-- ATUALIZADO (de Integer para String)
    // Corresponde a: cm.ordem_fatiamento_lote
    private Integer ordemFatiamentoLote;
    // Corresponde a: cm.total
    private Long total;
    // Corresponde a: cm.enviados
    private Long enviados;
    // Corresponde a: cm.entregue
    private Long entregue;
    // Corresponde a: cm.nao_entregue
    private Long naoEntregue;
    // Corresponde a: cm.higienizado
    private Long higienizado;
    // Corresponde a: cm.cancelado
    private Long cancelado;
    // Corresponde a: cm.retornos
    private Long retornos;
    // Corresponde a: cm.data_inicio
    private LocalDateTime dataInicio;
    // Corresponde a: cm.data_agendada
    private LocalDateTime dataAgendada;
    // Corresponde a: cm.data_envio
    private LocalDateTime dataEnvio;
    // Corresponde a: cm.code
    private String code;
    // Corresponde a: cm.description
    private String description;
    // Corresponde a: cm.hash
    private String hash;
    // Corresponde a: cm.external_id
    private String externalId;
    // Corresponde a: cm.valor_tarifado
    private BigDecimal valorTarifado;

    public RelatorioSinteticoDTO(String campanha, String nomeEmissor, String nomeDepartamentoEmissor,
                                 String canalMensageria, Integer ordemFatiamentoLote, Long total,
                                 Long enviados, Long entregue, Long naoEntregue, Long higienizado,
                                 Long cancelado, Long retornos, LocalDateTime dataInicio,
                                 LocalDateTime dataAgendada, LocalDateTime dataEnvio, String code,
                                 String description, String hash, String externalId,
                                 BigDecimal valorTarifado) {
        this.campanha = campanha;
        this.nomeEmissor = nomeEmissor;
        this.nomeDepartamentoEmissor = nomeDepartamentoEmissor;
        this.canalMensageria = canalMensageria;
        this.ordemFatiamentoLote = ordemFatiamentoLote;
        this.total = total;
        this.enviados = enviados;
        this.entregue = entregue;
        this.naoEntregue = naoEntregue;
        this.higienizado = higienizado;
        this.cancelado = cancelado;
        this.retornos = retornos;
        this.dataInicio = dataInicio;
        this.dataAgendada = dataAgendada;
        this.dataEnvio = dataEnvio;
        this.code = code;
        this.description = description;
        this.hash = hash;
        this.externalId = externalId;
        this.valorTarifado = valorTarifado;
    }

}