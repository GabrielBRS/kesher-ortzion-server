package com.ortzion_technology.ortzion_telecom_server.security.repository.custom;

import com.ortzion_technology.ortzion_telecom_server.security.entity.Multiconta;
import com.ortzion_technology.ortzion_telecom_server.security.vo.MulticontaVO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MulticontaRepositoryCustomImpl implements MulticontaRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MulticontaVO> pegarTodasMulticontasPorIdUsuario(Long idAcessoUsuario) {

        String jpql = "SELECT new com.ortzion_technology.ortzion_telecom_server.security.vo.MulticontaVO(m.multicontaId.idUsuario, m.multicontaId.tipoPessoa, m.multicontaId.idSubjectus, m.multicontaId.idDepartamento, m.statusMulticonta, m.username, p.nome, p.sobrenome, e.nomeFantasia, d.nomeDepartamento, m.email, m.telefone) " +
                "FROM Multiconta m " +
                "LEFT JOIN Pessoa p ON p.idPessoa = m.multicontaId.idSubjectus " +
                "LEFT JOIN Empresa e ON e.idEmpresa = m.multicontaId.idSubjectus " +
                "LEFT JOIN Departamento d ON d.idDepartamento = m.multicontaId.idDepartamento " +
                "WHERE m.multicontaId.idUsuario = :idAcessoUsuario";

        return entityManager.createQuery(jpql, MulticontaVO.class)
                .setParameter("idAcessoUsuario", idAcessoUsuario)
                .getResultList();
    }

    @Override
    public Optional<Multiconta> pegarMulticontaPerfilEmpresarialColaborador(Long idUsuario, Integer tipoPessoa, Long idEmpresa, Integer idDepartamento) {

        String jpql = "SELECT m " +
                "FROM Multiconta m " +
                "WHERE m.multicontaId.idUsuario = :idUsuario " +
                "AND m.multicontaId.tipoPessoa = :tipoPessoa " +
                "AND m.multicontaId.idSubjectus = :idEmpresa " +
                "AND m.multicontaId.idDepartamento = :idDepartamento";

        try {

            Multiconta result = entityManager.createQuery(jpql, Multiconta.class)
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("tipoPessoa", tipoPessoa)
                    .setParameter("idEmpresa", idEmpresa)
                    .setParameter("idDepartamento", idDepartamento)
                    .getSingleResult();

            return Optional.of(result);

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

}