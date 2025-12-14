package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class RelatorioSistelcomRequest {

    @JsonAlias("multiconta")
    private MulticontaRequestDTO multicontaRequestDTO;

    private LocalDate dataInicio;
    private LocalDate dataFim;

    @NotNull(message = "O canal mensageria não pode ser nulo.")
    private Integer tipoRelatorio;

    @NotNull(message = "O canal mensageria não pode ser nulo.")
    private Integer tipoCanalMensageria;

}
