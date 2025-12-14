package com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "canal_mensageria", schema = "mercado_virtual")
@SequenceGenerator(
        name = "seq_id_canal_mensageria_generator",
        sequenceName = "mercado_virtual.seq_id_canal_mensageria",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CanalMensageria implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_canal_mensageria")
    private Integer idCanalMensageria;

    @Column(name = "nome_canal_mensageria")
    private String nomeCanalMensageria;

    @Column(name = "descricao_canal_mensageria")
    private String descricaoCanalMensageria;

    @Column(name = "preco_compra")
    private BigDecimal precoCompra;

    @Column(name = "preco_minimo_venda")
    private BigDecimal precoMinimoVenda;

    @OneToMany(mappedBy = "canalMensageria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<EstoqueMercadoriaVirtual> estoques;

}
