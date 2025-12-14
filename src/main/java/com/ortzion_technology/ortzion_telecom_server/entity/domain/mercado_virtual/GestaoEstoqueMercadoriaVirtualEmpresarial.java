package com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Colaborador;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Departamento;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Empresa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "gestao_estoque_mercadoria_virtual_empresarial", schema = "mercado_virtual",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id_empresa", "id_departamento", "id_colaborador", "id_estoque_mercadoria_virtual"})
        })
@SequenceGenerator(
        name = "seq_id_gestao_estoque_mercadoria_virtual_empresarial_generator",
        sequenceName = "mercado_virtual.seq_id_gestao_estoque_mercadoria_virtual_empresarial",
        initialValue = 1,
        allocationSize = 1
)
public class GestaoEstoqueMercadoriaVirtualEmpresarial implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gestao_estoque_mercadoria_virtual_empresarial")
    private Long idGestaoEstoqueMercadoriaVirtualEmpresarial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa")
    @JsonBackReference("empresa-gestao-ref")
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_departamento")
    @JsonBackReference("departamento-gestao-ref")
    private Departamento departamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_colaborador")
    @JsonBackReference("colaborador-gestao-ref")
    private Colaborador colaborador;

    @Column(name = "status")
    private Integer status;

    @Column(name = "limite_diario")
    private Long limiteDiario;

    @Column(name = "saldo")
    private Long saldo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estoque_mercadoria_virtual", referencedColumnName = "id_estoque_mercadoria_virtual")
    @JsonBackReference("estoque-gestao-ref")
    private EstoqueMercadoriaVirtual estoqueMercadoriaVirtual;

}