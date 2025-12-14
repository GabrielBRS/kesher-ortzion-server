package com.ortzion_technology.ortzion_telecom_server.repository.dao;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.sistelcom.CampanhaMensageria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CampanhaMensageriaDAORepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    public Page<CampanhaMensageria> pegarCampanhasPorMulticonta(
            Long idUsuario,
            Integer tipoPessoa,
            Long idSubjectus,
            Integer idDepartamento,
            LocalDateTime dataAtual,
            LocalDateTime dataFinal,
            Pageable pageable
    ) {

        StringBuilder jpql = new StringBuilder("SELECT c FROM CampanhaMensageria c WHERE 1=1");
        StringBuilder countJpql = new StringBuilder("SELECT COUNT(c) FROM CampanhaMensageria c WHERE 1=1");

        Map<String, Object> parameters = new HashMap<>();

        if (idUsuario != null) {
            jpql.append(" AND c.idUsuario = :idUsuario");
            countJpql.append(" AND c.idUsuario = :idUsuario");
            parameters.put("idUsuario", idUsuario);
        }

        if (tipoPessoa != null) {
            jpql.append(" AND c.tipoPessoa = :tipoPessoa");
            countJpql.append(" AND c.tipoPessoa = :tipoPessoa");
            parameters.put("tipoPessoa", tipoPessoa);
        }

        if (idSubjectus != null) {
            jpql.append(" AND c.idSubjectus = :idSubjectus");
            countJpql.append(" AND c.idSubjectus = :idSubjectus");
            parameters.put("idSubjectus", idSubjectus);
        }

        if (idDepartamento != null) {
            jpql.append(" AND c.idDepartamento = :idDepartamento");
            countJpql.append(" AND c.idDepartamento = :idDepartamento");
            parameters.put("idDepartamento", idDepartamento);
        }

        if (dataAtual != null) {
            jpql.append(" AND c.dataEnvio >= :dataAtual");
            countJpql.append(" AND c.dataEnvio >= :dataAtual");
            parameters.put("dataAtual", dataAtual);
        }

        if (dataFinal != null) {
            jpql.append(" AND c.dataEnvio <= :dataFinal");
            countJpql.append(" AND c.dataEnvio <= :dataFinal");
            parameters.put("dataFinal", dataFinal);
        }

        if (pageable.getSort().isSorted()) {
            jpql.append(" ORDER BY ");
            pageable.getSort().forEach(order ->
                    jpql.append("c.").append(order.getProperty()).append(" ").append(order.getDirection()).append(", ")
            );
            jpql.setLength(jpql.length() - 2);
        }

        TypedQuery<Long> countQuery = entityManager.createQuery(countJpql.toString(), Long.class);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }
        long totalRows = countQuery.getSingleResult();

        TypedQuery<CampanhaMensageria> query = entityManager.createQuery(jpql.toString(), CampanhaMensageria.class);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        query.setFirstResult(Math.toIntExact(pageable.getOffset()));
        query.setMaxResults(pageable.getPageSize());

        List<CampanhaMensageria> resultList = query.getResultList();

        return new PageImpl<>(resultList, pageable, totalRows);
    }

    public Page<CampanhaMensageria> pegarCampanhasPorColaborador(Long idUsuario, Integer tipoPessoa, Long idSubjectus, Integer idDepartamento, Long idColaborador, LocalDateTime dataAtual, LocalDateTime dataFinal, Pageable pageable) {
        return null;
    }
}