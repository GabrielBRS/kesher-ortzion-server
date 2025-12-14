package com.ortzion_technology.ortzion_telecom_server.repository.dao;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.FiltroClientePessoaBigDataRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.bigdata.pessoa.ClientePessoaBigData;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.FiltroClientePessoaBigDataDTO;
import com.ortzion_technology.ortzion_telecom_server.repository.dao.contrato.ClientePessoaBigDataDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ClientePessoaBigDataDAORepositoryImpl implements ClientePessoaBigDataDAO {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<ClientePessoaBigData> pegarClientePessoaBigData(FiltroClientePessoaBigDataRequest filtroClientePessoaBigData) {
        return List.of();
    }

    @Override
    public List<ClientePessoaBigData> buscarPorFiltroDinamico(FiltroClientePessoaBigDataDTO filtro) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ClientePessoaBigData> cq = cb.createQuery(ClientePessoaBigData.class);
        Root<ClientePessoaBigData> clientePessoa = cq.from(ClientePessoaBigData.class);

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.hasText(filtro.getNomeDestinatario())) {
            predicates.add(cb.like(cb.lower(clientePessoa.get("nomeDestinatario")), "%" + filtro.getNomeDestinatario().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(filtro.getEmailDestinatario())) {
            predicates.add(cb.equal(clientePessoa.get("emailDestinatario"), filtro.getEmailDestinatario()));
        }
        if (StringUtils.hasText(filtro.getTelefoneCompletoDestinatario())) {
            predicates.add(cb.equal(clientePessoa.get("telefoneCompletoDestinatario"), filtro.getTelefoneCompletoDestinatario()));
        }
        if (StringUtils.hasText(filtro.getDocumentoDestinatario())) {
            predicates.add(cb.equal(clientePessoa.get("documentoDestinatario"), filtro.getDocumentoDestinatario()));
        }
        if (filtro.getIdSubjectus() != null) {
            predicates.add(cb.equal(clientePessoa.get("idSubjectus"), filtro.getIdSubjectus()));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        TypedQuery<ClientePessoaBigData> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

}
