package com.ortzion_technology.ortzion_telecom_server.security.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;


@Embeddable
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class MulticontaId implements Serializable {

    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "tipo_pessoa")
    private Integer tipoPessoa;

    @Column(name = "id_subjectus")
    private Long idSubjectus;

    @Column(name = "id_departamento")
    private Integer idDepartamento;

}
