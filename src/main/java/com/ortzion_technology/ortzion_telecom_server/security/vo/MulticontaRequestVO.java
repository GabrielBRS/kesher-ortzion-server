package com.ortzion_technology.ortzion_telecom_server.security.vo;

import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MulticontaRequestVO {

    private MulticontaRequestDTO multiconta;

    private MulticontaVO multicontaVO;
    private PessoaVO pessoaVO;
    private EmpresaVO empresaVO;
    private DepartamentoVO departamentoVO;

}
