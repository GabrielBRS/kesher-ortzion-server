package com.ortzion_technology.ortzion_telecom_server.security.vo;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Contato;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Empresa;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Endereco;
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
public class EmpresaVO {

    private Long idEmpresa;

    private String razaoSocial;

    private String nomeFantasia;

    private String telefoneCodigoArea;

    private String telefoneCodigoPais;

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

    private LocalDate dataCadastro;

    private Endereco endereco;

    private Contato contato;

    public EmpresaVO(Empresa empresa) {
        this.idEmpresa = Optional.ofNullable(empresa.getIdEmpresa()).orElse(null);
        this.razaoSocial = Optional.ofNullable(empresa.getRazaoSocial()).orElse(null);
        this.nomeFantasia = Optional.ofNullable(empresa.getNomeFantasia()).orElse(null);
        this.telefoneCodigoArea = Optional.ofNullable(empresa.getTelefoneCodigoArea()).orElse(null);
        this.telefoneCodigoPais = Optional.ofNullable(empresa.getTelefoneCodigoPais()).orElse(null);
        this.telefone = Optional.ofNullable(empresa.getTelefone()).orElse(null);
        this.email = Optional.ofNullable(empresa.getEmail()).orElse(null);
        this.cnpj = Optional.ofNullable(empresa.getCnpj()).orElse(null);
        this.dataFundacao = Optional.ofNullable(empresa.getDataFundacao()).orElse(null);
        this.inscricaoEstadual = Optional.ofNullable(empresa.getInscricaoEstadual()).orElse(null);
        this.inscricaoMunicipal = Optional.ofNullable(empresa.getInscricaoEstadual()).orElse(null);
        this.naturezaJuridica = Optional.ofNullable(empresa.getNaturezaJuridica()).orElse(null);
        this.setorAtuacao = Optional.ofNullable(empresa.getSetorAtuacao()).orElse(null);
        this.porteEmpresa = Optional.ofNullable(empresa.getPorteEmpresa()).orElse(null);
        this.tipoEmpresa = Optional.ofNullable(empresa.getTipoEmpresa()).orElse(null);
        this.dataCadastro = Optional.ofNullable(empresa.getDataCadastro()).orElse(null);
        this.endereco = Optional.ofNullable(empresa.getEndereco()).orElse(null);
        this.contato = Optional.ofNullable(empresa.getContato()).orElse(null);
    }

}
