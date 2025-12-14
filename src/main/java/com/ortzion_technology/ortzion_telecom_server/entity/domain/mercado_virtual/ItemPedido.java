package com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "item_pedido", schema = "mercado_virtual")
@SequenceGenerator(
        name = "seq_id_item_pedido_generator",
        sequenceName = "mercado_virtual.seq_id_item_pedido",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemPedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item_pedido")
    private Integer idItemPedido;

    @Column(name = "preco_unitario_compra", nullable = false)
    private BigDecimal precoUnitarioNoMomentoDaCompra;

    @Column(name = "quantidade", nullable = false)
    private Long quantidade;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_pacote_canal_mensageria", nullable = false)
    private PacoteCanalMensageria pacoteCanalMensageria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_compra")
    @JsonBackReference
    private Compra compra;

    @Transient
    public BigDecimal getValorTotalItem() {
        if (this.precoUnitarioNoMomentoDaCompra == null || this.quantidade == null) {
            return BigDecimal.ZERO;
        }
        return this.precoUnitarioNoMomentoDaCompra.multiply(BigDecimal.valueOf(this.quantidade));
    }

}
