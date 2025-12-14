package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.FiltroClientePessoaBigDataDTO;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class FiltroClientePessoaBigDataRequest {

    @JsonAlias("multiconta")
    private MulticontaRequestDTO multicontaRequestDTO;

    @JsonAlias("filtro")
    private FiltroClientePessoaBigDataDTO filtro;

}
