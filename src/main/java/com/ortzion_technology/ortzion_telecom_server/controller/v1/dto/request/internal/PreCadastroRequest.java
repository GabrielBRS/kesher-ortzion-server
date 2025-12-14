package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PreCadastroRequest {

    @JsonAlias("multiconta")
    private MulticontaRequestDTO multicontaRequestDTO;

    //PESSOA

    private String nome;

    private String sobrenome;

    private String nomeCompleto;

    private String documento;

    private String email;

    private String telefoneCodigoArea;

    private String telefoneCodigoPais;

    private String telefone;

    //EMPRESA

    private String razaoSocial;

    private String nomeFantasia;

    private String documentoEmpresa;

    private String emailEmpresa;

    private String telefoneCodigoAreaEmpresa;

    private String telefoneCodigoPaisEmpresa;

    private String telefoneEmpresa;


    //SUPER

    private Integer tipoPreCadastro;
    private Integer statusPreCadastro;


}
