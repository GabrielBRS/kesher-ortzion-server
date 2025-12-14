package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.Paginavel;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CampanhaMensageriaRequest {

    @JsonAlias("multiconta")
    private MulticontaRequestDTO multicontaRequestDTO;

    private String nomeCampanha;

    private Integer statusCampanha;

    private LocalDateTime dataCriacaoInicio;

    private LocalDateTime dataCriacaoFim;

    private LocalDateTime dataHoraInicio;

    private LocalDateTime dataHoraFim;

    private Paginavel paginavel;

}
