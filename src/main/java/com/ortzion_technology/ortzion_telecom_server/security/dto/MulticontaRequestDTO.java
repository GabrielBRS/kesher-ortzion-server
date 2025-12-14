package com.ortzion_technology.ortzion_telecom_server.security.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class MulticontaRequestDTO {

    @NotNull(message = "O id do usuario não pode ser nulo.")
    Long idUsuario;

    @NotNull(message = "O tipo de pessoa não pode ser nulo.")
    Integer tipoPessoa;

    @NotNull(message = "O id do subjectus não pode ser nulo.")
    Long idSubjectus;

    String nomeSubjectus;

    @NotNull(message = "O id do departamento não pode ser nulo.")
    Integer idDepartamento;

    String nomeDepartamento;

    Long idColaborador;

    String nomeColaborador;

}
