package com.ortzion_technology.ortzion_telecom_server.entity.domain.sistelcom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "campanha_mensageria", schema = "sistema_telecomunicacoes")
@SequenceGenerator(
        name = "seq_id_campanha_mensageria_generator",
        sequenceName = "sistema_telecomunicacoes.seq_id_campanha_mensageria",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@ToString(exclude = "publicosAlvoCampanha")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CampanhaMensageria implements Serializable {

    @Version
    @Column(name = "version")
    private Long version;

    @Id
    @Column(name = "id_campanha_mensageria")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_campanha_mensageria_generator")
    private Long idCampanhaMensageria;

    @Column(name = "id_usuario_envio")
    private Long idUsuarioEnvio;

    @Column(name = "tipo_pessoa")
    private Integer tipoPessoa;

    @Column(name = "id_subjectus")
    private Long idSubjectus;

    @Column(name = "nome_emissor")
    private String nomeEmissor;

    @Column(name = "id_departamento")
    private Integer idDepartamento;

    @Column(name = "nome_departamento_emissor")
    private String nomeDepartamentoEmissor;

    @Column(name = "id_colaborador")
    private Long idColaborador;

    //Mensagem
    @Column(name = "campanha")
    private String campanha;

    //Tipo mensagem
    @Column(name = "id_canal_mensageria")
    private Integer idCanalMensageria;

    @Column(name = "canal_mensageria")
    private String canalMensageria;

    @Column(name = "ordem_fatiamento_lote")
    private Integer ordemFatiamentoLote = 1;

    @Column(name = "id_prioridade_mensagem")
    private Integer idPrioridadeMensagem = 3;

    //Status mensagem
    @Column(name = "id_status")
    private Integer idStatusCampanha = 2;

    @Column(name = "total")
    private Long total = 0L;

    @Column(name = "valido")
    private Long valido = 0L;

    @Column(name = "invalido")
    private Long invalido = 0L;

    @Column(name = "cancelado")
    private Long cancelado = 0L;

    @Column(name = "enviados")
    private Long enviados = 0L;

    @Column(name = "entregue")
    private Long entregue = 0L;

    @Column(name = "nao_entregue")
    private Long naoEntregue = 0L;

    @Column(name = "higienizado")
    private Long higienizado = 0L;

    @Column(name = "retornos")
    private Long retornos = 0L;

    //Data
    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    @Column(name = "data_agendada")
    private LocalDateTime dataAgendada;

    @Column(name = "data_agendada_final")
    private LocalDateTime dataAgendadaFinal;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    @OneToMany(mappedBy = "campanhaMensageria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PublicoAlvoCampanha> publicosAlvoCampanha = new ArrayList<>();

    @Column(name = "valor_tarifado", precision = 10, scale = 4)
    private BigDecimal valorTarifado;

    //Resposta pingoo

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "description", length = 1024)
    private String description;

    @Column(name = "hash", length = 64)
    private String hash;

    @Column(name = "external_id", unique = true)
    private String externalId;

}
