package com.ortzion_technology.ortzion_telecom_server.security.vo;

import com.ortzion_technology.ortzion_telecom_server.security.entity.Multiconta;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MulticontaVO {

    private Long idUsuario;

    private Integer tipoPessoa;

    private Long idSubjectus;

    private Integer idDepartamento;

    private Long idColaborador;

    private Integer statusUsuario;

    private String nomeExibicao;

    private String nomePessoa;

    private String nomeEmpresa;

    private String nomeDepartamento;

    private String email;

    private String telefone;

    public MulticontaVO(Multiconta multiconta) {
        this.idUsuario = multiconta.getMulticontaId().getIdUsuario();
        this.tipoPessoa = multiconta.getMulticontaId().getTipoPessoa();
        this.idSubjectus = multiconta.getMulticontaId().getIdSubjectus();
        this.idDepartamento = Optional.ofNullable(multiconta.getDepartamento().getIdDepartamento()).orElse(null);
    }

    public MulticontaVO(Long idUsuario, Integer tipoPessoa, Long idSubjectus,
                        Integer idDepartamento, Integer statusMulticonta, String username,
                        String nomePessoa, String sobrenome,String nomeEmpresa, String nomeDepartamento,
                        String email, String telefone) {

        this.idUsuario = idUsuario;
        this.tipoPessoa = tipoPessoa;
        this.idSubjectus = idSubjectus;
        this.idDepartamento = idDepartamento;

        this.statusUsuario = statusMulticonta;
        this.nomeExibicao = username;

        this.nomePessoa = nomePessoa + " " + sobrenome;
        this.nomeEmpresa = nomeEmpresa;
        this.nomeDepartamento = nomeDepartamento;
        this.email = email;

        this.telefone = telefone;

    }

}
