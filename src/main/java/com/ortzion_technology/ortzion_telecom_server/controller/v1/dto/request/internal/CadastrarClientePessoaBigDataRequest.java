package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.ClientePessoaBigDataDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.EmpresaDTO;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class CadastrarClientePessoaBigDataRequest {

    @JsonAlias("multiconta")
    private MulticontaRequestDTO multiconta;

    @JsonAlias("empresa")
    private EmpresaDTO empresa;

    @JsonAlias("clientePessoa")
    private List<ClientePessoaBigDataDTO> clientePessoa;


}
