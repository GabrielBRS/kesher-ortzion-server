package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.response.internal;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PainelCompraMercadoVirtualResponse {

    private String nomeRepresentanteLegal;

    private String nomeMercadoriaVirtual;

    private BigDecimal precoMercadoriaVirtual;

}
