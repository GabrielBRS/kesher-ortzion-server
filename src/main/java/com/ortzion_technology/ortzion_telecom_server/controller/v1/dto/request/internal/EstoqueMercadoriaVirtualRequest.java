package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class EstoqueMercadoriaVirtualRequest {

    @JsonAlias("multiconta")
    private MulticontaRequestDTO multiconta;

    //Definicao Canal
    @NotNull(message = "O id da mercadoria virtual não pode ser nulo.")
    private Integer idMercadoriaVirtual;

    //Definicao Estoque
    @NotNull(message = "A quantidade da mercadoria virtual não pode ser nulo.")
    private Long quantidadeParaEstoque;

}
