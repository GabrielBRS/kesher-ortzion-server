package com.ortzion_technology.ortzion_telecom_server.entity.domain.analytics;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "dashboard", schema = "analytics")
@SequenceGenerator(
        name = "seq_id_dashboard_generator",
        sequenceName = "analytics.seq_id_dashboard",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Dashboard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dashboard")
    private Long idDashboard;

    @Column(name = "tipo_pessoa")
    private Integer tipoPessoa;

    @Column(name = "id_subjectus")
    private Long idSubjectus;

    @Column(name = "id_departamento")
    private Integer idDepartamento;

    @Column(name = "id_colaborador")
    private Long idColaborador;

    @Column(name = "id_canal_mensageria")
    private Integer idCanalMensageria;

    @Column(name = "solicitado")
    private Long solicitado = 0L;

    @Column(name = "invalidos")
    private Long invalidos = 0L;

    @Column(name = "validos")
    private Long validos = 0L;

    @Column(name = "enviados")
    private Long enviados = 0L;

    @Column(name = "cancelado")
    private Long cancelado = 0L;

    @Column(name = "entregue")
    private Long entregue = 0L;

    @Column(name = "nao_entregue")
    private Long naoEntregue = 0L;

    @Column(name = "disponivel")
    private Long disponivel = 0L;

    @Column(name = "consumido")
    private Long consumido = 0L;

    @Column(name = "higienizado")
    private Long higienizado = 0L;

}
