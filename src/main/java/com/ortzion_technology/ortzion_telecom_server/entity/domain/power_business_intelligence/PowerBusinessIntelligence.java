package com.ortzion_technology.ortzion_telecom_server.entity.domain.power_business_intelligence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "relatorio", schema = "power_business_intelligence")
@SequenceGenerator(
        name = "seq_id_relatorio_generator",
        sequenceName = "power_business_intelligence.seq_id_relatorio",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PowerBusinessIntelligence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_relatorio")
    private Long idRelatorio;

    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "tipo_pessoa")
    private Integer tipoPessoa;

    @Column(name = "id_subjectus")
    private Long idSubjectus;

    @Column(name = "link_power_bi")
    private String linkPowerBi;

}
