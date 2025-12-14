package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class PedidoDisparoCanalMensageiraBigDataRequest {

    @JsonAlias("multiconta")
    private MulticontaRequestDTO multicontaRequestDTO;

    private Boolean buscarBigData = false;

    private List<String> criterios;


}
