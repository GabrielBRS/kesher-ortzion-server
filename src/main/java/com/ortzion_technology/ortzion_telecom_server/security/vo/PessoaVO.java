package com.ortzion_technology.ortzion_telecom_server.security.vo;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Contato;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Endereco;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Pessoa;
import lombok.*;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PessoaVO {

    private Long idPessoa;

    private String nome;

    private String sobrenome;

    private String nomeCompleto;

    private String email;

    private String sexo;

    private String documento;

    private String rg;

    private String rgOrgaoEmissor;

    private LocalDate rgDataEmissao;

    private String naturalidade;

    private String nacionalidade;

    private String situacaoMigratoria;

    private String passaporte;

    private LocalDate dataNascimento;

    private String nomePai;

    private String nomeMae;

    private LocalDate dataCadastro;

    private Endereco endereco;

    private Contato contato;

    public PessoaVO(Pessoa pessoa) {
        this.idPessoa = Optional.ofNullable(pessoa.getIdPessoa()).orElse(null);
        this.nome = Optional.ofNullable(pessoa.getNome()).orElse(null);
        this.sobrenome = Optional.ofNullable(pessoa.getSobrenome()).orElse(null);
        this.nomeCompleto = Optional.ofNullable(pessoa.getNomeCompleto()).orElse(null);
        this.email = Optional.ofNullable(pessoa.getEmail()).orElse(null);
        this.sexo = Optional.ofNullable(pessoa.getSexo()).orElse(null);
        this.documento = Optional.ofNullable(pessoa.getDocumento()).orElse(null);
        this.rg = Optional.ofNullable(pessoa.getDocumento()).orElse(null);
        this.rgOrgaoEmissor = Optional.ofNullable(pessoa.getRgOrgaoEmissor()).orElse(null);
        this.rgDataEmissao = Optional.ofNullable(pessoa.getRgDataEmissao()).orElse(null);
        this.naturalidade = Optional.ofNullable(pessoa.getNaturalidade()).orElse(null);
        this.nacionalidade = Optional.ofNullable(pessoa.getNacionalidade()).orElse(null);
        this.situacaoMigratoria = Optional.ofNullable(pessoa.getSituacaoMigratoria()).orElse(null);
        this.passaporte = Optional.ofNullable(pessoa.getPassaporte()).orElse(null);
        this.dataNascimento = Optional.ofNullable(pessoa.getDataNascimento()).orElse(null);
        this.nomePai = Optional.ofNullable(pessoa.getNomePai()).orElse(null);
        this.nomeMae = Optional.ofNullable(pessoa.getNomeMae()).orElse(null);
        this.dataCadastro = Optional.ofNullable(pessoa.getDataCadastro()).orElse(null);
        this.endereco = Optional.ofNullable(pessoa.getEndereco()).orElse(null);
        this.contato = Optional.ofNullable(pessoa.getContato()).orElse(null);
    }

}
