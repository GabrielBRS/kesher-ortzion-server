package com.ortzion_technology.ortzion_telecom_server.entity.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PessoaDTO {

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

}
