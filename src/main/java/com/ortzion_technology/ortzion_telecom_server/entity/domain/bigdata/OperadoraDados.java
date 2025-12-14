package com.ortzion_technology.ortzion_telecom_server.entity.domain.bigdata;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "operadora_dados", schema = "bigdata")
@SequenceGenerator(
        name = "seq_id_operadora_dados_generator",
        sequenceName = "bigdata.seq_id_operadora_dados",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OperadoraDados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_operadora_dados")
    private Long idOperadoraDados;

    @Column(name = "id_subjectus")
    private Long idSubjectus;

    @Column(name = "documento_subjectus")
    private String documentoSubjectus;

    @Column(name = "status")
    private String status;

    @Column(name = "data_entrada")
    private LocalDateTime dataEntrada;

}
