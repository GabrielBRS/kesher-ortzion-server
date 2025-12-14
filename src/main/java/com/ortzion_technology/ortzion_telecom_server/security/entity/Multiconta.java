package com.ortzion_technology.ortzion_telecom_server.security.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Departamento;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Pessoa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "multiconta", schema = "security")
@SequenceGenerator(
        name = "seq_id_multiconta_generator",
        sequenceName = "security.seq_id_multiconta",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Multiconta {

    @EmbeddedId
    private MulticontaId multicontaId ;

    @ManyToOne
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario")
    private AcessoUsuario acessoUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idDepartamento")
    @JoinColumn(name = "id_departamento")
    private Departamento departamento;

    @Column(name = "status_multiconta")
    private Integer statusMulticonta = 1;

    @Column(name = "email")
    private String email;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "username")
    private String username;

    @OneToMany(
            mappedBy = "multiconta",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private Set<AcessoGrupoMulticonta> acessoGrupoMulticonta = new HashSet<>();

    public Integer getTipoPessoa() {
        return this.multicontaId != null ? this.multicontaId.getTipoPessoa() : null;
    }

    public void setTipoPessoa(Integer tipoPessoa) {
        if (this.multicontaId == null) {
            this.multicontaId = new MulticontaId();
        }
        this.multicontaId.setTipoPessoa(tipoPessoa);
    }

    public Long getIdSubjectus() {
        return this.multicontaId != null ? this.multicontaId.getIdSubjectus() : null;
    }

    public void setIdSubjectus(Long idSubjectus) {
        if (this.multicontaId == null) {
            this.multicontaId = new MulticontaId();
        }
        this.multicontaId.setIdSubjectus(idSubjectus);
    }

}
