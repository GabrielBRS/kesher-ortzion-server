package com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.response.internal;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.PreCadastro;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreCadastroResponse {

    private String nome;

    private String sobrenome;

    private String documento;

    private String email;

    private String telefoneCodigoArea;

    private String telefoneCodigoPais;

    private String telefone;

    private String razaoSocial;

    private String nomeFantasia;

    private String documentoEmpresa;

    private String emailEmpresa;

    private String telefoneCodigoAreaEmpresa;

    private String telefoneCodigoPaisEmpresa;

    private String telefoneEmpresa;

    private Integer tipoPreCadastro;

    private LocalDateTime dataPreCadastro;

    private LocalDateTime dataAlteracaoPreCadastro;

    private Integer statusPreCadastro;

    private String senha;

    public PreCadastroResponse(PreCadastro preCadastro) {
        this.nome = preCadastro.getNome();
        this.sobrenome = preCadastro.getSobrenome();
        this.documento = preCadastro.getDocumento();
        this.email = preCadastro.getEmail();
        this.telefoneCodigoArea = preCadastro.getTelefoneCodigoArea();
        this.telefoneCodigoPais = preCadastro.getTelefoneCodigoPais();
        this.telefone = preCadastro.getTelefone();
        this.razaoSocial = preCadastro.getRazaoSocial();
        this.nomeFantasia = preCadastro.getNomeFantasia();
        this.documentoEmpresa = preCadastro.getDocumentoEmpresa();
        this.emailEmpresa = preCadastro.getEmailEmpresa();
        this.telefoneCodigoAreaEmpresa = preCadastro.getTelefoneCodigoAreaEmpresa();
        this.telefoneCodigoPaisEmpresa = preCadastro.getTelefoneCodigoPaisEmpresa();
        this.telefoneEmpresa = preCadastro.getTelefoneEmpresa();
        this.tipoPreCadastro = preCadastro.getTipoPreCadastro();
        this.dataPreCadastro = preCadastro.getDataPreCadastro();
        this.dataAlteracaoPreCadastro = preCadastro.getDataAlteracaoPreCadastro();
        this.statusPreCadastro = preCadastro.getStatusPreCadastro();
    }
}
