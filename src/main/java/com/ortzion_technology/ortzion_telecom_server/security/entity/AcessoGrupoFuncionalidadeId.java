package com.ortzion_technology.ortzion_telecom_server.security.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class AcessoGrupoFuncionalidadeId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "id_grupo")
    private Long idGrupo;

    @Column(name = "id_funcionalidade")
    private Long idFuncionalidade;

}
