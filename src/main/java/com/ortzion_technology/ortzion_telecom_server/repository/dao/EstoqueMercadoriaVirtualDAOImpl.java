package com.ortzion_technology.ortzion_telecom_server.repository.dao;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.EstoqueMercadoriaVirtual;
import com.ortzion_technology.ortzion_telecom_server.repository.dao.contrato.EstoqueMercadoriaVirtualDAO;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EstoqueMercadoriaVirtualDAOImpl implements EstoqueMercadoriaVirtualDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<EstoqueMercadoriaVirtual> pegarEstoqueMercadoriaVirtual(Integer tipoPessoa, Long idSubjectus, Integer idDepartamento) {

        String sql = "SELECT * FROM mercado_virtual.estoque_mercadoria_virtual emv " +
                "WHERE 1=1 " +
                "AND emv.tipo_pessoa = :tipoPessoa " +
                "AND emv.id_subjectus = :idSubjectus " +
                "AND emv.id_departamento = :idDepartamento";

        Query query = entityManager.createNativeQuery(sql, EstoqueMercadoriaVirtual.class);
        query.setParameter("tipoPessoa", tipoPessoa);
        query.setParameter("idSubjectus", idSubjectus);
        query.setParameter("idDepartamento", idDepartamento);

        return query.getResultList();
    }

    @Override
    public EstoqueMercadoriaVirtual pegarEstoquesMulticontaPorCanalMensageria(
            Integer tipoPessoa, Long idSubjectus, Integer idDepartamento, Integer idCanalMensageria) {

        String jpql = "SELECT e FROM EstoqueMercadoriaVirtual e " +
                "WHERE e.tipoPessoa = :tipoPessoa " +
                "AND e.idSubjectus = :idSubjectus " +
                "AND e.idDepartamento = :idDepartamento " +
                "AND e.canalMensageria.idCanalMensageria = :idCanalMensageria";

        TypedQuery<EstoqueMercadoriaVirtual> query = entityManager.createQuery(jpql, EstoqueMercadoriaVirtual.class);
        query.setParameter("tipoPessoa", tipoPessoa);
        query.setParameter("idSubjectus", idSubjectus);
        query.setParameter("idDepartamento", idDepartamento);
        query.setParameter("idCanalMensageria", idCanalMensageria);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    @Override
    public List<EstoqueMercadoriaVirtual> pegarTodosEstoquesMulticonta(Integer tipoPessoa, Long idSubjectus, Integer idDepartamento) {
        String jpql = "SELECT e FROM EstoqueMercadoriaVirtual e " +
                "WHERE e.tipoPessoa = :tipoPessoa " +
                "AND e.idSubjectus = :idSubjectus " +
                "AND e.idDepartamento = :idDepartamento";

        TypedQuery<EstoqueMercadoriaVirtual> query = entityManager.createQuery(jpql, EstoqueMercadoriaVirtual.class);
        query.setParameter("tipoPessoa", tipoPessoa);
        query.setParameter("idSubjectus", idSubjectus);
        query.setParameter("idDepartamento", idDepartamento);

        return query.getResultList();
    }

}