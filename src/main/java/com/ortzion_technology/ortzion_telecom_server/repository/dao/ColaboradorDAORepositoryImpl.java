package com.ortzion_technology.ortzion_telecom_server.repository.dao;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.FiltroColaboradorRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Colaborador;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ColaboradorDAORepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Colaborador> buscarColaboradores(FiltroColaboradorRequest filtroColaboradorRequest) {
        TypedQuery<Colaborador> query = entityManager.createQuery(
                "SELECT c FROM Colaborador c " +
                        "WHERE 1=1 " +
                        "AND c.empresa.idEmpresa = :idEmpresa " +
                        "AND c.departamento.idDepartamento = :idDepartamento ",
                Colaborador.class
        );
        query.setParameter("idEmpresa", filtroColaboradorRequest.getIdEmpresa());
        query.setParameter("idDepartamento", filtroColaboradorRequest.getIdDepartamento());
        return query.getResultList();
    }


}
