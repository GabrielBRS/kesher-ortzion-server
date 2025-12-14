package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.response.internal;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioLogadoResponse {

    private Long idUsuario;
    private String loginUnicoUsuario;
    private String documentoUsuario;


}
