package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CadastroPessoaRequest {

    @JsonAlias("multiconta")
    private MulticontaRequestDTO multiconta;

    private String nome;

    private String sobrenome;

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

    private String senhaNova;

    private String telefoneCodigoArea;

    private String telefoneCodigoPais;

    private String telefone;

    private String pais;
    private String estado;
    private String cidade;
    private String logradouro;
    private String bairro;
    private String numero;
    private String complemento;
    private String cep;

    private Boolean autorizacaoPosseDados;
    private Boolean autorizacaoPosseDadosTerceiros;

}
