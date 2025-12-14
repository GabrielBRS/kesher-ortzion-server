package com.ortzion_technology.ortzion_telecom_server.entity.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ClientePessoaBigDataDTO {

    private Long idClientePessoaBigData;

    private Integer tipoPessoa;

    private Long idSubjectus;

    private String telefoneCompletoDestinatario;

    private String emailDestinatario;

    private String documentoDestinatario;

    private String nomeDestinatario;

    private String sobrenomeDestinatario;

    private String sexoDestinatario;

    private String naturalidadeDestinatario;

    private String nacionalidadeDestinatario;

    private LocalDate dataNascimentoDestinatario;

    private String nomePaiDestinatario;

    private String nomeMaeDestinatario;

    private String cepDestinatario;

    private String logradouroDestinatario;

    private String numeroDestinatario;

    private String complementoDestinatario;

    private String bairroDestinatario;

    private String cidadeDestinatario;

    private String estadoDestinatario;

    private Long idControladoraDados;

    private Long idEncarregadoDados;

    private Long idOperadoraDados;

    private Boolean autorizacaoPosseDados;

    private Boolean autorizacaoPosseDadosTerceiros;

}
