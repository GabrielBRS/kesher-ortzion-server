package com.ortzion_technology.ortzion_telecom_server.security.entity;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "acesso_funcionalidade", schema = "security")
@SequenceGenerator(
        name = "seq_id_acesso_funcionalidade",
        sequenceName = "security.seq_id_acesso_funcionalidade",
        initialValue = 1,
        allocationSize = 1
)
public class AcessoFuncionalidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_funcionalidade", unique = true, nullable = false)
    private Long idFuncionalidadeUsuario;

    @Column(name = "descricao_funcionalidade", nullable = false)
    private String descricaoFuncionalidade;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_role", nullable = false)
    private Role role;

    public AcessoFuncionalidade() {}

    public AcessoFuncionalidade(Long idFuncionalidadeUsuario, String descricaoFuncionalidade, Role role) {
        this.idFuncionalidadeUsuario = idFuncionalidadeUsuario;
        this.descricaoFuncionalidade = descricaoFuncionalidade;
        this.role = role;
    }

    public Long getIdFuncionalidadeUsuario() {
        return idFuncionalidadeUsuario;
    }

    public void setIdFuncionalidadeUsuario(Long idFuncionalidadeUsuario) {
        this.idFuncionalidadeUsuario = idFuncionalidadeUsuario;
    }

    public String getDescricaoFuncionalidade() {
        return descricaoFuncionalidade;
    }

    public void setDescricaoFuncionalidade(String descricaoFuncionalidade) {
        this.descricaoFuncionalidade = descricaoFuncionalidade;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
