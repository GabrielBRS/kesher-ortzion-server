package com.ortzion_technology.ortzion_telecom_server.security.vo;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Contato;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Departamento;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Empresa;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Endereco;
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
public class DepartamentoVO {

    private Integer idDepartamento;

    private String nomeDepartamento;

    private String codigoDepartamento;

    public DepartamentoVO(Departamento departamento) {
        this.nomeDepartamento = departamento.getNomeDepartamento();
        this.idDepartamento = departamento.getIdDepartamento();
    }

}
