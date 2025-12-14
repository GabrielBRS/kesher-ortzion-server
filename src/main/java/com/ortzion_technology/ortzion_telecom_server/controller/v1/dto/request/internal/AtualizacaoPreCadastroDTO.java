package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AtualizacaoPreCadastroDTO {

    @JsonAlias("multiconta")
    private MulticontaRequestDTO multicontaRequestDTO;

    private String assunto;
    private String destinatario;
    private String mensagem;

}
