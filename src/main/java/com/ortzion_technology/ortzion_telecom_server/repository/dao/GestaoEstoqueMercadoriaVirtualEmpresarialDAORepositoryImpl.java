package com.ortzion_technology.ortzion_telecom_server.repository.dao;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.GestaoEstoqueMercadoriaVirtualEmpresarial;
import com.ortzion_technology.ortzion_telecom_server.repository.dao.contrato.GestaoEstoqueMercadoriaVirtualDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GestaoEstoqueMercadoriaVirtualEmpresarialDAORepositoryImpl implements GestaoEstoqueMercadoriaVirtualDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<GestaoEstoqueMercadoriaVirtualEmpresarial> pegarGestaoEstoqueMercadoriaVirtualEmpresarial(Long idUsuario, Integer tipoPessoa, Long idSubjectus, Integer idDepartamento) {
        return List.of();
    }

    @Override
    public GestaoEstoqueMercadoriaVirtualEmpresarial pegarGestaoEstoqueMercadoriaVirtualEmpresarialMulticontaPorCanalMensageria(Long idUsuario, Integer tipoPessoa, Long idSubjectus, Integer idDepartamento, Long idColaborador, Integer IdCanalMensageria) {
        return null;
    }

    @Override
    public List<GestaoEstoqueMercadoriaVirtualEmpresarial> pegarTodosGestaoEstoqueMercadoriaVirtualEmpresarialMulticonta(Long idUsuario, Integer tipoPessoa, Long idSubjectus, Integer idDepartamento, Long idColaborador, Integer IdCanalMensageria) {
        return List.of();
    }

}
