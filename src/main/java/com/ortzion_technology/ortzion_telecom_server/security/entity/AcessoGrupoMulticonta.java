package com.ortzion_technology.ortzion_telecom_server.security.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Entity
@Table(name = "acesso_grupo_multiconta", schema = "security")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AcessoGrupoMulticonta implements Serializable {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private AcessoGrupoMulticontaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idGrupo")
    @JoinColumn(name = "id_grupo", nullable = false)
    private AcessoGrupo acessoGrupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "tipo_pessoa", referencedColumnName = "tipo_pessoa", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "id_subjectus", referencedColumnName = "id_subjectus", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "id_departamento", referencedColumnName = "id_departamento", nullable = false, insertable = false, updatable = false)
    })
    private Multiconta multiconta;

}
