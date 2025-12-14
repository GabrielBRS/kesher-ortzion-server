package com.ortzion_technology.ortzion_telecom_server.repository.dao.contrato;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public abstract class AbstractDAO<T, I> {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return entityManager;
    }

}
