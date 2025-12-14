package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.DepartamentoDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.EmpresaDTO;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FiltroDepartamentoRequest {

    @JsonAlias("multiconta")
    private MulticontaRequestDTO multiconta;

    private EmpresaDTO empresa;

    private DepartamentoDTO departamento;

}
