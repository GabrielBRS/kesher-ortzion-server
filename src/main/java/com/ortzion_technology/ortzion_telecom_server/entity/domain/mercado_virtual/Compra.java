package com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.banco_digital.MeioPagamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "compra", schema = "mercado_virtual")
@SequenceGenerator(
        name = "seq_id_compra_generator",
        sequenceName = "mercado_virtual.seq_id_compra",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Compra implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_compra_generator")
    @Column(name = "id_compra")
    private Long idCompra;

    @Column(name = "tipo_pessoa")
    private Integer tipoPessoa;

    @Column(name = "id_subjectus")
    private Long idSubjectus;

    @Column(name = "id_departamento")
    private Integer idDepartamento;

    @Column(name = "id_colaborador")
    private Long idColaborador;

    @Column(name = "valor_compra")
    private BigDecimal valorCompra;

    @Column(name = "data_compra")
    private LocalDateTime dataCompra;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<ItemPedido> itemPedidos = new ArrayList<>();;

    @Column(name = "forma_pagamento")
    private Integer formaPagamento;

    @Column(name = "status_pagamento")
    private Integer statusPagamento;

    @OneToOne(cascade = CascadeType.ALL,  fetch = FetchType.EAGER)
    @JoinColumn(name = "id_meio_pagamento")
    @JsonManagedReference
    private MeioPagamento meioPagamento;

    @Column(name = "id_usuario_logado")
    private Long idUsuarioLogado;

    @Transient
    public BigDecimal getValorTotalCalculado() {
        if (this.itemPedidos == null) {
            return BigDecimal.ZERO;
        }
        return this.itemPedidos.stream()
                .filter(Objects::nonNull)
                .map(ItemPedido::getValorTotalItem)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
