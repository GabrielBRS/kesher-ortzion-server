package com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.GestaoEstoqueMercadoriaVirtualEmpresarial;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.ColaboradorDTO;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "colaborador", schema = "cadastral")
@SequenceGenerator(
        name = "seq_id_colaborador_generator",
        sequenceName = "cadastral.seq_id_colaborador",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Colaborador implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_colaborador")
    private Long idColaborador;

    @Column(name = "nome")
    private String nome;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "email")
    private String email;

    @Column(name = "cargo")
    private String cargo;

    @Column(name = "data_admissao")
    private LocalDate dataAdmissao;

    @Column(name = "data_demissao")
    private LocalDate dataDemissao;

    @Column(name = "status")
    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    @JsonBackReference("usuario-colaborador-ref")
    private AcessoUsuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "documento",
            referencedColumnName = "documento"
    )
    @JsonBackReference("pessoa-colaborador-ref")
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_empresa")
    @JsonBackReference("empresa-colaborador-ref")
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_departamento")
    @JsonBackReference("departamento-colaborador-ref")
    private Departamento departamento;

    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("colaborador-gestao-ref")
    private List<GestaoEstoqueMercadoriaVirtualEmpresarial> gestaoEstoqueMercadoriaVirtualEmpresarial;

    @Column(name = "id_usuario_inclusao")
    private Long idUsuarioInclusao;

    public Colaborador(ColaboradorDTO req, Pessoa pessoa, Departamento departamento) {
        this.pessoa = pessoa;
        this.departamento = departamento;
        this.cargo = req.getCargo();
        this.dataAdmissao = req.getDataAdmissao();
        this.dataDemissao = req.getDataDemissao();
    }

    public Colaborador(ColaboradorDTO req) {
        this.cargo = req.getCargo();
        this.dataAdmissao = req.getDataAdmissao();
        this.dataDemissao = req.getDataDemissao();
    }
}