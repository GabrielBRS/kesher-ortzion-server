package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class PreCadastroRecuperar {

    private Integer tipoPessoa;
    private Long idSubjectus;

}
