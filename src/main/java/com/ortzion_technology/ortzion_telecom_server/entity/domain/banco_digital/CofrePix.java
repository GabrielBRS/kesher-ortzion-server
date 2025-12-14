package com.ortzion_technology.ortzion_telecom_server.entity.domain.banco_digital;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "cofre_pix", schema = "banco_digital")
@SequenceGenerator(
        name = "seq_id_cofre_pix_generator",
        sequenceName = "cadastral.seq_id_cofre_pix",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Immutable
public class CofrePix implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cofre_pix")
    private Long idCofrePix;

    @Column(name = "chave_pix")
    private String chavePix;

    @Column(name = "data_cadastro_chave_pix")
    private LocalDateTime dataCadastroChavePix;

    @Column(name = "status_chave_pix")
    private Integer statusChavePix;

    @Column(name = "data_revogacao_chave_pix")
    private LocalDateTime dataRevogacaoChavePix;

    @Column(name = "nome_banco_digital")
    private String nomeBancoDigital;

}
