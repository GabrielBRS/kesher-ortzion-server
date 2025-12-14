package com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual;

import com.ortzion_technology.ortzion_telecom_server.exception.EstoqueInsuficienteException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "estoque_mercadoria_virtual", schema = "mercado_virtual")
@SequenceGenerator(
        name = "seq_id_estoque_mercadoria_virtual_generator",
        sequenceName = "mercado_virtual.seq_id_estoque_mercadoria_virtual",
        initialValue = 1,
        allocationSize = 1
)
public class EstoqueMercadoriaVirtual implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estoque_mercadoria_virtual")
    private Long idEstoqueMercadoriaVirtual;

    @Column(name = "tipo_pessoa")
    private Integer tipoPessoa;

    @Column(name = "id_subjectus")
    private Long idSubjectus;

    @Column(name = "id_departamento")
    private Integer idDepartamento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_canal_mensageria", nullable = false)
    private CanalMensageria canalMensageria;

    @Column(name = "quantidade_estoque_canal_mensageria")
    private Long quantidadeEstoqueCanalMensageria = 0L;

    @Column(name = "id_configuracao_estoque_mercadoria_virtual")
    private Long idConfiguracaoEstoqueMercadoriaVirtual;

    public void acrescentar(Long quantidadeParaAcrescentar) {
        if (quantidadeParaAcrescentar == null || quantidadeParaAcrescentar <= 0) {
            throw new IllegalArgumentException("A quantidade a ser acrescentada deve ser positiva.");
        }
        this.quantidadeEstoqueCanalMensageria += quantidadeParaAcrescentar;
    }

    public void subtrair(Long quantidadeParaSubtrair) {
        if (quantidadeParaSubtrair == null || quantidadeParaSubtrair <= 0) {
            throw new IllegalArgumentException("A quantidade a ser subtraída deve ser positiva.");
        }
        if (this.quantidadeEstoqueCanalMensageria < quantidadeParaSubtrair) {
            throw new EstoqueInsuficienteException(
                    "Tentativa de subtrair " + quantidadeParaSubtrair +
                            " unidades, mas apenas " + this.quantidadeEstoqueCanalMensageria +
                            " estão disponíveis no estoque."
            );
        }
        this.quantidadeEstoqueCanalMensageria -= quantidadeParaSubtrair;
    }


}
