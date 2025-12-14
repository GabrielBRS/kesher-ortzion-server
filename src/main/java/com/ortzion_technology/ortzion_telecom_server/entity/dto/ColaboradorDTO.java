package com.ortzion_technology.ortzion_telecom_server.entity.dto;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ColaboradorDTO {

    private String telefoneCorporativo;
    private String emailCorporativo;
    private String loginCorporativo;
    private String documentoColaborador;

    private String cargo;
    private LocalDate dataAdmissao;
    private LocalDate dataDemissao;

    private PessoaDTO pessoa;

    private EnderecoDTO endereco;

    private ContatoDTO contato;

    private Boolean autorizacaoPosseDados;

    private Boolean autorizacaoPosseDadosTerceiros;

}
