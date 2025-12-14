package com.ortzion_technology.ortzion_telecom_server.repository.dao.contrato;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.GestaoEstoqueMercadoriaVirtualEmpresarial;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GestaoEstoqueMercadoriaVirtualDAO {

    List<GestaoEstoqueMercadoriaVirtualEmpresarial> pegarGestaoEstoqueMercadoriaVirtualEmpresarial(Long idUsuario, Integer tipoPessoa, Long idSubjectus, Integer idDepartamento);

    GestaoEstoqueMercadoriaVirtualEmpresarial pegarGestaoEstoqueMercadoriaVirtualEmpresarialMulticontaPorCanalMensageria(Long idUsuario, Integer tipoPessoa, Long idSubjectus, Integer idDepartamento, Long idColaborador, Integer IdCanalMensageria);

    List<GestaoEstoqueMercadoriaVirtualEmpresarial> pegarTodosGestaoEstoqueMercadoriaVirtualEmpresarialMulticonta(Long idUsuario, Integer tipoPessoa, Long idSubjectus, Integer idDepartamento, Long idColaborador, Integer IdCanalMensageria);

}
