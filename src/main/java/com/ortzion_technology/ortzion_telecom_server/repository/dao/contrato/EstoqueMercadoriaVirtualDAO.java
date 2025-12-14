package com.ortzion_technology.ortzion_telecom_server.repository.dao.contrato;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.EstoqueMercadoriaVirtual;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstoqueMercadoriaVirtualDAO {

    List<EstoqueMercadoriaVirtual> pegarEstoqueMercadoriaVirtual(Integer tipoPessoa, Long idSubjectus, Integer idDepartamento);

    EstoqueMercadoriaVirtual pegarEstoquesMulticontaPorCanalMensageria(Integer tipoPessoa, Long idSubjectus, Integer idDepartamento, Integer IdCanalMensageria);

    List<EstoqueMercadoriaVirtual> pegarTodosEstoquesMulticonta(Integer tipoPessoa, Long idSubjectus, Integer idDepartamento);

}
