package com.ortzion_technology.ortzion_telecom_server.security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Table(name = "acesso_grupo", schema = "security")
@SequenceGenerator(
        name = "seq_id_acesso_grupo_generator",
        sequenceName = "security.seq_id_acesso_grupo",
        initialValue = 1,
        allocationSize = 1
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcessoGrupo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = IDENTITY, generator = "seq_id_acesso_grupo_generator")
    @Column(name = "id_grupo", unique = true, nullable = false)
    private Long idGrupo;

    @Column(name = "descricao_grupo", nullable = false)
    private String descricaoGrupoUsuario;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "acessoGrupo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<AcessoGrupoFuncionalidade> acessoGrupoFuncionalidades = new HashSet<>();

    @OneToMany(
            mappedBy = "acessoGrupo",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private Set<AcessoGrupoMulticonta> acessoGrupoMulticonta = new HashSet<>();

}
