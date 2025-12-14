package com.ortzion_technology.ortzion_telecom_server.entity.domain.sistelcom;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "publico_alvo_campanha", schema = "sistema_telecomunicacoes")
@SequenceGenerator(
        name = "seq_id_publico_alvo_campanha_generator",
        sequenceName = "sistema_telecomunicacoes.seq_id_publico_alvo_campanha",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PublicoAlvoCampanha implements Serializable {

    @Id
    @Column(name = "id_publico_alvo_campanha")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_publico_alvo_campanha_generator")
    private Long idPublicoAlvoCampanha;

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

    @Column(name = "plataforma_envio")
    private String plataformaEnvio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_campanha_mensageria")
    private CampanhaMensageria campanhaMensageria;

    @Column(name = "descricao_campanha_mensageria")
    private String descricaoCampanhaMensageria;

    @Column(name = "id_canal_mensageria")
    private Integer idCanalMensageria;

    @Column(name = "canal_mensageria")
    private String canalMensageria;

    @Column(name = "destino")
    private String destino;

    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    @Column(name = "data_agendada")
    private LocalDateTime dataAgendada;

    @Column(name = "data_agendada_final")
    private LocalDateTime dataAgendadaFinal;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;

    @Column(name = "data_confirmacao")
    private LocalDateTime dataConfirmacao;

    @Column(name = "id_status")
    private Integer idStatusMensagem;

    @Column(name = "status")
    private String status;

    @Column(name = "mensagem_destinatario")
    private String mensagemDestinatario;

    @Column(name = "situacao")
    private String situacao;

    @Column(name = "id_fatiamento_lote")
    private Integer idFatiamentoLote = 1;

    @Column(name = "fornecedor")
    private String fornecedor;

    @Column(name = "identificador")
    private String identificador;

    @Column(name = "campo_info")
    private String campoInfo;

    @Column(name = "operadora")
    private String operadora;

    @Column(name = "nome_arquivo")
    private String nomeArquivo;

    @Column(name = "retorno")
    private Integer retorno = 0;


}
