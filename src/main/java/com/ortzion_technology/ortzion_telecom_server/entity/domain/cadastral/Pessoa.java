package com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CadastroColaboradorRequest;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CadastroEmpresaRequest;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CadastroPessoaRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.PessoaDTO;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.utils.DocumentoUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "pessoa", schema = "cadastral")
@SequenceGenerator(
        name = "seq_id_pessoa_generator",
        sequenceName = "cadastral.seq_id_pessoa",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pessoa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pessoa")
    private Long idPessoa;

    @Column(name = "nome")
    private String nome;

    @Column(name = "sobrenome")
    private String sobrenome;

    @Column(name = "nome_completo")
    private String nomeCompleto;

    @Column(name = "email")
    private String email;

    @Column(name = "sexo")
    private String sexo;

    @Column(name = "documento", unique = true, nullable = false)
    private String documento;

//    @OneToOne(mappedBy = "pessoa", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//    @JsonIgnore
//    private AcessoUsuario acessoUsuario;

    @Column(name = "rg")
    private String rg;

    @Column(name = "rg_orgao_emissor")
    private String rgOrgaoEmissor;

    @Column(name = "rg_data_emissao")
    private LocalDate rgDataEmissao;

    @Column(name = "naturalidade")
    private String naturalidade;

    @Column(name = "nacionalidade")
    private String nacionalidade;

    @Column(name = "situacao_migratoria")
    private String situacaoMigratoria;

    @Column(name = "passaporte")
    private String passaporte;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "nome_pai")
    private String nomePai;

    @Column(name = "nome_mae")
    private String nomeMae;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "id_endereco", referencedColumnName = "idEndereco")
    private Endereco endereco;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "id_contato", referencedColumnName = "idContato")
    private Contato contato;

    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference
    private List<Colaborador> funcoes;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "id_lgpd", referencedColumnName = "id_lgpd")
    private LGPD lgpd;

    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = false)
    @JoinColumn(name = "id_pre_cadastro", referencedColumnName = "id_pre_cadastro")
    private PreCadastro preCadastro;

    public Pessoa(CadastroPessoaRequest cadastroPessoaRequest) {
        this.nome = Optional.ofNullable(cadastroPessoaRequest.getNome()).orElse(null) ;
        this.sobrenome = Optional.ofNullable(cadastroPessoaRequest.getSobrenome()).orElse(null) ;
        this.email = Optional.ofNullable(cadastroPessoaRequest.getEmail()).orElse(null) ;
        this.sexo = Optional.ofNullable(cadastroPessoaRequest.getSexo()).orElse(null) ;
        this.documento = Optional.ofNullable(DocumentoUtils.desformatadorDocumento(cadastroPessoaRequest.getDocumento())).orElse(null) ;
        this.rg = Optional.ofNullable(cadastroPessoaRequest.getRg()).orElse(null) ;
        this.rgOrgaoEmissor = Optional.ofNullable(cadastroPessoaRequest.getRgOrgaoEmissor()).orElse(null) ;
        this.rgDataEmissao = Optional.ofNullable(cadastroPessoaRequest.getRgDataEmissao()).orElse(null) ;
        this.naturalidade = Optional.ofNullable(cadastroPessoaRequest.getNaturalidade()).orElse(null) ;
        this.nacionalidade = Optional.ofNullable(cadastroPessoaRequest.getNacionalidade()).orElse(null) ;
        this.situacaoMigratoria = Optional.ofNullable(cadastroPessoaRequest.getSituacaoMigratoria()).orElse(null) ;
        this.passaporte = Optional.ofNullable(cadastroPessoaRequest.getPassaporte()).orElse(null) ;
        this.dataNascimento = Optional.ofNullable(cadastroPessoaRequest.getDataNascimento()).orElse(null) ;
        this.nomePai = Optional.ofNullable(cadastroPessoaRequest.getNomePai()).orElse(null) ;
        this.nomeMae = Optional.ofNullable(cadastroPessoaRequest.getNomeMae()).orElse(null) ;
    }

    public Pessoa(PessoaDTO cadastroPessoaRequest) {
        this.nome = Optional.ofNullable(cadastroPessoaRequest.getNome()).orElse(null) ;
        this.sobrenome = Optional.ofNullable(cadastroPessoaRequest.getSobrenome()).orElse(null) ;
        this.email = Optional.ofNullable(cadastroPessoaRequest.getEmail()).orElse(null) ;
        this.sexo = Optional.ofNullable(cadastroPessoaRequest.getSexo()).orElse(null) ;
        this.documento = Optional.ofNullable(DocumentoUtils.desformatadorDocumento(cadastroPessoaRequest.getDocumento())).orElse(null) ;
        this.rg = Optional.ofNullable(cadastroPessoaRequest.getRg()).orElse(null) ;
        this.rgOrgaoEmissor = Optional.ofNullable(cadastroPessoaRequest.getRgOrgaoEmissor()).orElse(null) ;
        this.rgDataEmissao = Optional.ofNullable(cadastroPessoaRequest.getRgDataEmissao()).orElse(null) ;
        this.naturalidade = Optional.ofNullable(cadastroPessoaRequest.getNaturalidade()).orElse(null) ;
        this.nacionalidade = Optional.ofNullable(cadastroPessoaRequest.getNacionalidade()).orElse(null) ;
        this.situacaoMigratoria = Optional.ofNullable(cadastroPessoaRequest.getSituacaoMigratoria()).orElse(null) ;
        this.passaporte = Optional.ofNullable(cadastroPessoaRequest.getPassaporte()).orElse(null) ;
        this.dataNascimento = Optional.ofNullable(cadastroPessoaRequest.getDataNascimento()).orElse(null) ;
        this.nomePai = Optional.ofNullable(cadastroPessoaRequest.getNomePai()).orElse(null) ;
        this.nomeMae = Optional.ofNullable(cadastroPessoaRequest.getNomeMae()).orElse(null) ;
    }

}
