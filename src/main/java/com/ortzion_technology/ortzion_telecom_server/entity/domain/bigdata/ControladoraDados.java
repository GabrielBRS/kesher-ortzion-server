package com.ortzion_technology.ortzion_telecom_server.entity.domain.bigdata;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "controladora_dados", schema = "bigdata")
@SequenceGenerator(
        name = "seq_id_controladora_dados_generator",
        sequenceName = "bigdata.seq_id_controladora_dados",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ControladoraDados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_controladora_dados")
    private Long idControladoraDados;

    @Column(name = "id_subjectus")
    private Long idSubjectus;

    @Column(name = "documento_subjectus")
    private String documentoSubjectus;

    @Column(name = "status")
    private String status;

    @Column(name = "data_entrada")
    private LocalDateTime dataEntrada;

}
