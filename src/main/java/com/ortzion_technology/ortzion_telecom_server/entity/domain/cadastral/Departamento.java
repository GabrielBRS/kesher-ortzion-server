package com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.GestaoEstoqueMercadoriaVirtualEmpresarial;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "departamento", schema = "cadastral")
@SequenceGenerator(
        name = "seq_id_departamento_generator",
        sequenceName = "cadastral.seq_id_departamento",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Departamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_departamento")
    private Integer idDepartamento;

    @Column(name = "codigo_departamento")
    private String codigoDepartamento;

    @Column(name = "nome_departamento")
    private String nomeDepartamento;

    @ManyToMany(mappedBy = "departamentos", fetch = FetchType.LAZY)
    @JsonBackReference("empresa-departamento-ref")
    private List<Empresa> empresas;

    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("departamento-colaborador-ref")
    private List<Colaborador> colaboradores;

    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("departamento-gestao-ref")
    private List<GestaoEstoqueMercadoriaVirtualEmpresarial> gestaoEstoqueMercadoriaVirtualEmpresarial;

}