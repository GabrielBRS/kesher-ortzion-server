package com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.PreCadastroRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.registro_acervo_documentos.RegistroAcervoPreCadastro;
import com.ortzion_technology.ortzion_telecom_server.utils.DocumentoUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pre_cadastro", schema = "cadastral")
@SequenceGenerator(
        name = "seq_id_pre_cadastro_generator",
        sequenceName = "suporte.seq_id_pre_cadastro",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PreCadastro implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seq_id_pre_cadastro_generator")
    @Column(name = "id_pre_cadastro", unique = true, nullable = false)
    private Long idPreCadastro;

    //PESSOA

    @Column(name = "nome")
    private String nome;

    @Column(name = "sobrenome")
    private String sobrenome;

    @Column(name = "nome_completo")
    private String nomeCompleto;

    @Column(name = "documento")
    private String documento;

    @Column(name = "email")
    private String email;

    @Column(name = "telefone_codigo_pais")
    private String telefoneCodigoPais;

    @Column(name = "telefone_codigo_area")
    private String telefoneCodigoArea;

    @Column(name = "telefone")
    private String telefone;

    //EMPRESA

    @Column(name = "razao_social")
    private String razaoSocial;

    @Column(name = "nome_fantasia")
    private String nomeFantasia;

    @Column(name = "documento_empresa")
    private String documentoEmpresa;

    @Column(name = "email_empresa")
    private String emailEmpresa;

    @Column(name = "telefone_codigo_area_empresa")
    private String telefoneCodigoAreaEmpresa;

    @Column(name = "telefone_codigo_pais_empresa")
    private String telefoneCodigoPaisEmpresa;

    @Column(name = "telefone_empresa")
    private String telefoneEmpresa;

    //Configuracao

    @Column(name = "tipo_pre_cadastro")
    private Integer tipoPreCadastro;

    @Column(name = "data_pre_cadastro")
    private LocalDateTime dataPreCadastro;

    @Column(name = "data_alteracao_pre_cadastro")
    private LocalDateTime dataAlteracaoPreCadastro;

    @Column(name = "status_pre_cadastro")
    private Integer statusPreCadastro;

    @OneToMany(mappedBy = "preCadastro", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RegistroAcervoPreCadastro> arquivos;

    public PreCadastro(PreCadastroRequest preCadastroRequest) {
        this.nome = preCadastroRequest.getNome();
        this.sobrenome = preCadastroRequest.getSobrenome();
        this.nomeCompleto = preCadastroRequest.getNomeCompleto();
        this.documento = DocumentoUtils.desformatadorDocumento(preCadastroRequest.getDocumento());
        this.email = preCadastroRequest.getEmail();
        this.telefoneCodigoArea = DocumentoUtils.desformatadorTelefone(preCadastroRequest.getTelefoneCodigoArea());
        this.telefoneCodigoPais = DocumentoUtils.desformatadorTelefone(preCadastroRequest.getTelefoneCodigoPais());
        this.telefone = DocumentoUtils.desformatadorTelefone(preCadastroRequest.getTelefone());
        this.razaoSocial = preCadastroRequest.getRazaoSocial();
        this.nomeFantasia = preCadastroRequest.getNomeFantasia();
        this.documentoEmpresa = DocumentoUtils.desformatadorDocumento(preCadastroRequest.getDocumentoEmpresa());
        this.emailEmpresa = preCadastroRequest.getEmailEmpresa();
        this.telefoneCodigoAreaEmpresa = DocumentoUtils.desformatadorTelefone(preCadastroRequest.getTelefoneCodigoAreaEmpresa());
        this.telefoneCodigoPaisEmpresa = DocumentoUtils.desformatadorTelefone(preCadastroRequest.getTelefoneCodigoPaisEmpresa());
        this.telefoneEmpresa = DocumentoUtils.desformatadorTelefone(preCadastroRequest.getTelefoneEmpresa());
        this.tipoPreCadastro = preCadastroRequest.getTipoPreCadastro();
    }

}
