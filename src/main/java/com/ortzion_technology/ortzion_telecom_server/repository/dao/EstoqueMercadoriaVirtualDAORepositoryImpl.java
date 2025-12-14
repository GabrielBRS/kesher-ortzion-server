package com.ortzion_technology.ortzion_telecom_server.repository.dao;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.EstoqueMercadoriaVirtual;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EstoqueMercadoriaVirtualDAORepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public List<EstoqueMercadoriaVirtual> pegarEstoqueMercadoriaVirtual(Integer tipoPessoa, Long idSubjectus, Integer idDepartamento) {

        TypedQuery<EstoqueMercadoriaVirtual> query = entityManager.createQuery(
                "SELECT emv FROM EstoqueMercadoriaVirtual emv " +
                        "WHERE emv.tipoPessoa = :tipoPessoa " +
                        "AND emv.idSubjectus = :idSubjectus " +
                        "AND emv.idDepartamento = :idDepartamento ",
                EstoqueMercadoriaVirtual.class
        );
        query.setParameter("tipoPessoa", tipoPessoa);
        query.setParameter("idSubjectus", idSubjectus);
        query.setParameter("idDepartamento", idDepartamento);
        return query.getResultList();
    }

}
