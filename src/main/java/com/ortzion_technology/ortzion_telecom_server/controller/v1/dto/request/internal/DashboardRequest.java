package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardRequest {

    @JsonAlias("multiconta")
    private MulticontaRequestDTO multiconta;

    private Integer idCanalMensageria;


}
