package com.ortzion_technology.ortzion_telecom_server.entity.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DashboardDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idDashboard;

    private Integer tipoPessoa;

    private Long idSubjectus;

    private Integer idDepartamento;

    private Long idColaborador;

    private Integer idCanalMensageria;

    private String linkDashboard;

    private Long solicitado = 0L;

    private Long invalidos = 0L;

    private Long validos = 0L;

    private Long enviados = 0L;

    private Long cancelado = 0L;

    private Long entregue = 0L;

    private Long naoEntregue = 0L;

    private Long disponivel = 0L;

    private Long consumido = 0L;

}
