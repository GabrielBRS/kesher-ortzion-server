package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class PedidoDisparoCanalMensageriaSMSRequest {

    @JsonAlias("multiconta")
    private MulticontaRequestDTO multiconta;

    @NotEmpty
    private String campanha;

    private Integer idCanalMensageria;

    private LocalDateTime dataAgendada;

    private LocalDateTime dataAgendadaFinal;

    private String conteudoMensagem;

    private Boolean conteudoMensagemUnicoBoolean = false;

    private Boolean conteudoMensagemEmLoteBoolean = false;

    private Boolean conteudoMensagemCaractereEspecialBoolean = false;

    private Boolean dataAgendadaFinalBoolean = false;

    private Boolean fatiarEmLotesBoolean = false;

    private Integer quantidadeLotes;



}
