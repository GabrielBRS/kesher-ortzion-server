package com.ortzion_technology.ortzion_telecom_server.security.dto;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.*;
import com.ortzion_technology.ortzion_telecom_server.security.entity.Multiconta;
import com.ortzion_technology.ortzion_telecom_server.security.entity.MulticontaId;
import com.ortzion_technology.ortzion_telecom_server.security.vo.DepartamentoVO;
import com.ortzion_technology.ortzion_telecom_server.security.vo.EmpresaVO;
import com.ortzion_technology.ortzion_telecom_server.security.vo.MulticontaVO;
import com.ortzion_technology.ortzion_telecom_server.security.vo.PessoaVO;
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
public class MulticontaResponseDTO {

    private MulticontaVO multicontaVO;
    private PessoaVO pessoaVO;
    private EmpresaVO empresaVO;
    private DepartamentoVO departamentoVO;

    public MulticontaResponseDTO(Long idUsuario, Integer tipoPessoa, Long idSubjectus, Integer idDepartamento,
                                 Integer statusMulticonta, String username,
                                 Pessoa pessoa, Empresa empresa, Departamento departamento) {

        Multiconta multicontaAgregada = new Multiconta();
        multicontaAgregada.setMulticontaId(new MulticontaId(idUsuario, tipoPessoa, idSubjectus, idDepartamento));
        multicontaAgregada.setStatusMulticonta(statusMulticonta);
        multicontaAgregada.setUsername(username);

        multicontaAgregada.setDepartamento(departamento);

        this.multicontaVO = new MulticontaVO(multicontaAgregada);
        this.pessoaVO = (pessoa != null) ? new PessoaVO(pessoa) : null;
        this.empresaVO = (empresa != null) ? new EmpresaVO(empresa) : null;
        this.departamentoVO = (departamento != null) ? new DepartamentoVO(departamento) : null;
    }

    public MulticontaResponseDTO(Long idUsuario, Integer tipoPessoa, Long idSubjectus, Integer idDepartamento,
                                 Integer statusMulticonta, String username) {

        Multiconta multicontaAgregada = new Multiconta();
        multicontaAgregada.setMulticontaId(new MulticontaId(idUsuario, tipoPessoa, idSubjectus, idDepartamento));
        multicontaAgregada.setStatusMulticonta(statusMulticonta);
        multicontaAgregada.setUsername(username);

        this.multicontaVO = new MulticontaVO(multicontaAgregada);

    }

}
