package com.ortzion_technology.ortzion_telecom_server.security.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "acesso_grupo_funcionalidade", schema = "security")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class AcessoGrupoFuncionalidade implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    @EqualsAndHashCode.Include
    private AcessoGrupoFuncionalidadeId acessoFuncionalidadeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idGrupo")
    @JoinColumn(name = "id_grupo")
    private AcessoGrupo acessoGrupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idFuncionalidade")
    @JoinColumn(name = "id_funcionalidade")
    private AcessoFuncionalidade acessoFuncionalidade;

}
