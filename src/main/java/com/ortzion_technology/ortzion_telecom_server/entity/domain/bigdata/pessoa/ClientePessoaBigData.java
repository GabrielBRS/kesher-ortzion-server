package com.ortzion_technology.ortzion_telecom_server.entity.domain.bigdata.pessoa;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "cliente_pessoa_bigdata", schema = "bigdata")
@SequenceGenerator(
        name = "seq_id_cliente_pessoa_bigdata_generator",
        sequenceName = "bigdata.seq_id_cliente_pessoa_bigdata",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ClientePessoaBigData {

    @Id
    @Column(name = "id_cliente_pessoa_bigdata")
    private Long idClientePessoaBigData;

    @Column(name = "id_usuario")
    private Long idUsuarioInclusao;

    @Column(name = "tipo_pessoa")
    private Integer tipoPessoa;

    @Column(name = "id_subjectus")
    private Long idSubjectus;

    @Column(name = "nome_empresa", unique = true)
    private String nomeEmpresa;

    @Column(name = "telefone_cliente", unique = true)
    private String telefoneCompletoCliente;

    @Column(name = "email_cliente", unique = true)
    private String emailCliente;

    @Column(name = "documento_cliente")
    private String documentoCliente;

    @Column(name = "nome_cliente")
    private String nomeCliente;

    @Column(name = "sobrenome_cliente")
    private String sobrenomeCliente;

    @Column(name = "sexo_cliente")
    private String sexoCliente;

    @Column(name = "naturalidade_cliente")
    private String naturalidadeCliente;

    @Column(name = "nacionalidade_cliente")
    private String nacionalidadeCliente;

    @Column(name = "data_nascimento_cliente")
    private LocalDate dataNascimentoCliente;

    @Column(name = "nome_pai_cliente")
    private String nomePaiCliente;

    @Column(name = "nome_mae_cliente")
    private String nomeMaeCliente;

    @Column(name = "cep_cliente")
    private String cepCliente;

    @Column(name = "logradouro_cliente")
    private String logradouroCliente;

    @Column(name = "numero_cliente")
    private String numeroCliente;

    @Column(name = "complemento_cliente")
    private String complementoCliente;

    @Column(name = "bairro_cliente")
    private String bairroCliente;

    @Column(name = "cidade_cliente")
    private String cidadeCliente;

    @Column(name = "estado_cliente")
    private String estadoCliente;

    @Column(name = "id_controladora_dados")
    private Long idControladoraDados;

    @Column(name = "nome_controladora_dados")
    private String nomeControladoraDados;

    @Column(name = "id_encarregado_dados")
    private Long idEncarregadoDados;

    @Column(name = "nome_encarregado_dados")
    private String nomeEncarregadoDados;

    @Column(name = "id_operadora_dados")
    private Long idOperadoraDados;

    @Column(name = "nome_operadora_dados")
    private String nomeOperadoraDados;

    @Column(name = "autorizacao_posse_dados")
    private Boolean autorizacaoPosseDados;

    @Column(name = "autorizacao_posse_dados_terceiros")
    private Boolean autorizacaoPosseDadosTerceiros;

}
