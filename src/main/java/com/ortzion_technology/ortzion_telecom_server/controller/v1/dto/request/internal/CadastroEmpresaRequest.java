package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CadastroEmpresaRequest {

    @JsonAlias("multiconta")
    private MulticontaRequestDTO multiconta;

    private CadastroEmpresa dadosEmpresa;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class CadastroEmpresa implements Serializable {
        private static final long serialVersionUID = 1L;

        private String razaoSocial;
        private String nomeFantasia;
        private String telefone;
        private String email;
        private String cnpj;
        private LocalDate dataFundacao;
        private String inscricaoEstadual;
        private String inscricaoMunicipal;
        private String naturezaJuridica;
        private String setorAtuacao;
        private String porteEmpresa;
        private String tipoEmpresa;

        private EnderecoEmpresa endereco;
        private Contato contato;

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        @EqualsAndHashCode(onlyExplicitlyIncluded = true)
        public static class EnderecoEmpresa implements Serializable {
            private static final long serialVersionUID = 1L;
            private String cep;
            private String logradouro;
            private String numero;
            private String complemento;
            private String bairro;
            private String cidade;
            private String estado;
            private String pais;
        }

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        @EqualsAndHashCode(onlyExplicitlyIncluded = true)
        public static class Contato implements Serializable {
            private static final long serialVersionUID = 1L;
            private String countryCode;
            private String areaCode;
            private String number;
        }
    }

}