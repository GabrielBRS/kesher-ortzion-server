package com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "lgpd", schema = "cadastral")
@SequenceGenerator(
        name = "seq_id_lgpd_generator",
        sequenceName = "suporte.seq_id_lgpd",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LGPD implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lgpd")
    private Long idLgpd;

    @Column(name = "autorizacao_posse_dados")
    private Boolean autorizacaoPosseDados;

    @Column(name = "autorizacao_posse_dados_terceiros")
    private Boolean autorizacaoPosseDadosTerceiros;

}