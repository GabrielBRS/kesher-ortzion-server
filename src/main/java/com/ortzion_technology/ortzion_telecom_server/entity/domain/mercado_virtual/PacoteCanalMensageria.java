package com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "pacote_canal_mensageria", schema = "mercado_virtual")
@SequenceGenerator(
        name = "seq_id_pacote_canal_mensageria_generator",
        sequenceName = "mercado_virtual.seq_id_pacote_canal_mensageria",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PacoteCanalMensageria implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pacote_canal_mensageria")
    private Integer idPacoteCanalMensageria;

    @Column(name = "tipo_pacote_canal_mensageria")
    private Integer tipoPacoteCanalMensageria;

    @Column(name = "nome_pacote_canal_mensageria")
    private String nomePacoteCanalMensageria;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "descricao_pacote_canal_mensageria",
            schema = "mercado_virtual",
            joinColumns = @JoinColumn(name = "id_pacote_canal_mensageria")
    )
    @Column(name = "descricao_item")
    private Set<String> descricoes = new HashSet<>();

    @Column(name = "preco_pacote_canal_venda")
    private BigDecimal precoPacoteCanalVenda;

    @Column(name = "preco_unitario_venda", precision = 19, scale = 4)
    private BigDecimal precoUnitarioVenda;

    @OneToMany(mappedBy = "pacoteCanalMensageria", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<ItemPacoteCanalMensageria> itemPacoteCanalMensageria = new ArrayList<>();

}
