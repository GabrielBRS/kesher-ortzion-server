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
@Table(name = "item_pacote_canal_mensageria", schema = "mercado_virtual")
@SequenceGenerator(
        name = "seq_id_item_pacote_canal_mensageria_generator",
        sequenceName = "mercado_virtual.seq_id_item_pacote_canal_mensageria",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemPacoteCanalMensageria implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item_pacote_canal_mensageria")
    private Integer itemPacoteCanalMensageria;

    @Column(name = "quantidade_creditos")
    private Long quantidade;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pacote_canal_mensageria")
    @JsonBackReference
    private PacoteCanalMensageria pacoteCanalMensageria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_canal_mensageria")
    private CanalMensageria canalMensageria;

}
