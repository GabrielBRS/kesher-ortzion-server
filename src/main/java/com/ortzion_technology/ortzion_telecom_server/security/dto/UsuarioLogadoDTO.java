package com.ortzion_technology.ortzion_telecom_server.security.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioLogadoDTO {

    private Long idUsuario;
    private String dataCadastro;
    private String loginUnico;
    private String nome;
    private String sobrenome;
    private String email;
    private String dataNascimento;
    private String documento;
    private String cep;
    private String pais;
    private String estado;
    private String cidade;
    private String logradouro;
    private String bairro;
    private String numero;
    private String complemento;
    private String countryCode;
    private String areaCode;
    private String telefone;
    private String codigoCompra;
}