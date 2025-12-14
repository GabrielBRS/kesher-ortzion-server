package com.ortzion_technology.ortzion_telecom_server.security.dto;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Departamento;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Empresa;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Pessoa;
import com.ortzion_technology.ortzion_telecom_server.security.entity.Multiconta;
import com.ortzion_technology.ortzion_telecom_server.security.entity.MulticontaId;
import com.ortzion_technology.ortzion_telecom_server.security.vo.DepartamentoVO;
import com.ortzion_technology.ortzion_telecom_server.security.vo.EmpresaVO;
import com.ortzion_technology.ortzion_telecom_server.security.vo.MulticontaVO;
import com.ortzion_technology.ortzion_telecom_server.security.vo.PessoaVO;
import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MulticontaDTO {

    private String email;

    private String telefone;

    private String username;

}
