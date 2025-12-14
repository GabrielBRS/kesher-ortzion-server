package com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CadastroEmpresaRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.GestaoEstoqueMercadoriaVirtualEmpresarial;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "empresa", schema = "cadastral")
@SequenceGenerator(
        name = "seq_id_empresa_generator",
        sequenceName = "cadastral.seq_id_empresa",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Long idEmpresa;

    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;

    @Column(name = "nome_fantasia")
    private String nomeFantasia;

    @Column(name = "telefone_codigo_pais")
    private String telefoneCodigoPais;

    @Column(name = "telefone_codigo_area", nullable = false)
    private String telefoneCodigoArea;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "cnpj", nullable = false)
    private String cnpj;

    @Column(name = "data_fundacao")
    private LocalDate dataFundacao;

    @Column(name = "inscricao_estadual")
    private String inscricaoEstadual;

    @Column(name = "inscricao_municipal")
    private String inscricaoMunicipal;

    @Column(name = "natureza_juridica")
    private String naturezaJuridica;

    @Column(name = "setor_atuacao")
    private String setorAtuacao;

    @Column(name = "porte_empresa")
    private String porteEmpresa;

    @Column(name = "tipo_empresa")
    private String tipoEmpresa;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_endereco", referencedColumnName = "idEndereco")
    private Endereco endereco;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_contato", referencedColumnName = "idContato")
    private Contato contato;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("empresa-gestao-ref")
    private List<GestaoEstoqueMercadoriaVirtualEmpresarial> gestaoEstoqueMercadoriaVirtualEmpresarial;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference("empresa-colaborador-ref")
    private List<Colaborador> colaboradores;

    @Column(name = "id_usuario_inclusao")
    private Long idUsuarioInclusao;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "empresa_departamento",
            schema = "cadastral",
            joinColumns = @JoinColumn(name = "id_empresa"),
            inverseJoinColumns = @JoinColumn(name = "id_departamento")
    )
    @JsonManagedReference("empresa-departamento-ref")
    private List<Departamento> departamentos;

    public Empresa(CadastroEmpresaRequest.CadastroEmpresa empresaRequest) {
        this.razaoSocial = empresaRequest.getRazaoSocial();
        this.nomeFantasia = empresaRequest.getNomeFantasia();
        this.email = empresaRequest.getEmail();
        this.cnpj = empresaRequest.getCnpj();
        this.dataFundacao = empresaRequest.getDataFundacao();
        this.inscricaoEstadual = empresaRequest.getInscricaoEstadual();
        this.inscricaoMunicipal = empresaRequest.getInscricaoMunicipal();
        this.naturezaJuridica = empresaRequest.getNaturezaJuridica();
        this.setorAtuacao = empresaRequest.getSetorAtuacao();
        this.porteEmpresa = empresaRequest.getPorteEmpresa();
        this.tipoEmpresa = empresaRequest.getTipoEmpresa();

        if (empresaRequest.getContato() != null) {
            this.telefoneCodigoPais = empresaRequest.getContato().getCountryCode();
            this.telefoneCodigoArea = empresaRequest.getContato().getAreaCode();
            this.telefone = empresaRequest.getContato().getNumber();
        }
        else if (empresaRequest.getTelefone() != null) {
            String telPuro = empresaRequest.getTelefone().replaceAll("\\D", "");

            if (telPuro.length() >= 12) {
                this.telefoneCodigoPais = telPuro.substring(0, 2);
                this.telefoneCodigoArea = telPuro.substring(2, 4);
                this.telefone = telPuro.substring(4);
            } else if (telPuro.length() >= 10) {
                this.telefoneCodigoPais = "55";
                this.telefoneCodigoArea = telPuro.substring(0, 2);
                this.telefone = telPuro.substring(2);
            } else {
                this.telefoneCodigoPais = "00";
                this.telefoneCodigoArea = "00";
                this.telefone = telPuro;
            }
        }
    }
}