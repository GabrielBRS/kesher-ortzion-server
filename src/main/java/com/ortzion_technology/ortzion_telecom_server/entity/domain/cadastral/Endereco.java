package com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CadastroEmpresaRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.ContatoDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.EnderecoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "endereco", schema = "cadastral")
@SequenceGenerator(
        name = "seq_id_endereco_generator",
        sequenceName = "cadastral.seq_id_endereco",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Endereco implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEndereco;

    @Column(name = "logradouro")
    private String logradouro;

    @Column(name = "numero")
    private String numero;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "complemento")
    private String complemento;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "cep")
    private String cep;

    @Column(name = "estado")
    private String estado;

    @Column(name = "pais")
    private String pais;

    @OneToOne(mappedBy = "endereco", fetch = FetchType.LAZY)
    @JsonIgnore
    private Empresa empresa;

    @OneToOne(mappedBy = "endereco", fetch = FetchType.LAZY)
    @JsonIgnore
    private Pessoa pessoa;

    public Endereco(CadastroEmpresaRequest.CadastroEmpresa.EnderecoEmpresa enderecoRequest) {
        this.cep = enderecoRequest.getCep();
        this.logradouro = enderecoRequest.getLogradouro();
        this.numero = enderecoRequest.getNumero();
        this.complemento = enderecoRequest.getComplemento();
        this.bairro = enderecoRequest.getBairro();
        this.cidade = enderecoRequest.getCidade();
        this.estado = enderecoRequest.getEstado();
        this.pais = enderecoRequest.getPais();
    }

    public Endereco(EnderecoDTO enderecoDTO) {
        this.cep = enderecoDTO.getCep() != null ? enderecoDTO.getCep() : null;
        this.logradouro = enderecoDTO.getLogradouro() != null ? enderecoDTO.getLogradouro() : null;
        this.numero = enderecoDTO.getNumero() != null ? enderecoDTO.getNumero() : null;
        this.complemento = enderecoDTO.getComplemento() != null ? enderecoDTO.getComplemento() : null;
        this.bairro = enderecoDTO.getBairro() != null ? enderecoDTO.getBairro() : null;
        this.cidade = enderecoDTO.getCidade() != null ? enderecoDTO.getCidade() : null;
        this.estado = enderecoDTO.getEstado() != null ? enderecoDTO.getEstado() : null;
        this.pais = enderecoDTO.getPais() != null ? enderecoDTO.getPais() : null;
    }

}
