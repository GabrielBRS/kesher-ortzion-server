package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.*;
import com.ortzion_technology.ortzion_telecom_server.security.dto.AcessoGrupoDTO;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaDTO;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CadastroColaboradorRequest {

    @JsonAlias("multiconta")
    private MulticontaRequestDTO multiconta;

    @JsonAlias("colaborador")
    private ColaboradorDTO colaborador;

    @JsonAlias("pessoa")
    private PessoaDTO pessoa;

    @JsonAlias("enderecoPessoa")
    private EnderecoDTO enderecoPessoa;

    @JsonAlias("contatoPessoa")
    private ContatoDTO contatoPessoa;

    @JsonAlias("empresa")
    private EmpresaDTO empresa;

    @JsonAlias("departamento")
    private DepartamentoDTO departamento;

    @JsonAlias("multicontaColaborador")
    MulticontaDTO multicontaColaborador;

    @JsonAlias("acessoGrupo")
    private List<AcessoGrupoDTO> acessoGrupo;

}
