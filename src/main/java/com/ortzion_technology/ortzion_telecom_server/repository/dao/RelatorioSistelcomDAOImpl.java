package com.ortzion_technology.ortzion_telecom_server.repository.dao;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.RelatorioSistelcomRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.RelatorioAnaliticoDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.RelatorioSinteticoDTO;
import com.ortzion_technology.ortzion_telecom_server.repository.abstrato.customizado.RelatorioSistelcomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class RelatorioSistelcomDAOImpl implements RelatorioSistelcomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return (timestamp != null) ? timestamp.toLocalDateTime() : null;
    }

    @Override
    public List<RelatorioSinteticoDTO> buscarDadosSinteticos(RelatorioSistelcomRequest request) {

        String baseSql =
                " SELECT DISTINCT " +
                        "   cm.campanha, " +
                        "   cm.nome_emissor, " +
                        "   cm.nome_departamento_emissor, " +
                        "   cm.canal_mensageria, " +
                        "   cm.ordem_fatiamento_lote, " +
                        "   cm.total, " +
                        "   cm.enviados, " +
                        "   cm.entregue, " +
                        "   cm.nao_entregue, " +
                        "   cm.higienizado, " +
                        "   cm.cancelado, " +
                        "   cm.retornos, " +
                        "   cm.data_inicio, " +
                        "   cm.data_agendada, " +
                        "   cm.data_envio, " +
                        "   cm.code, " +
                        "   cm.description, " +
                        "   cm.hash, " +
                        "   cm.external_id, " +
                        "   cm.valor_tarifado " +
                        " FROM " +
                        "   sistema_telecomunicacoes.campanha_mensageria cm ";

        StringBuilder sql = new StringBuilder(baseSql);
        Map<String, Object> parameters = new HashMap<>();
        List<String> whereConditions = new ArrayList<>();

        if (Objects.nonNull(request.getDataInicio()) && Objects.nonNull(request.getDataFim())) {
            String dateClause = "(cm.data_inicio BETWEEN :dataInicio AND :dataFim " +
                    "OR cm.data_agendada BETWEEN :dataInicio AND :dataFim " +
                    "OR cm.data_envio BETWEEN :dataInicio AND :dataFim)";
            whereConditions.add(dateClause);
            parameters.put("dataInicio", request.getDataInicio().atStartOfDay());
            parameters.put("dataFim", request.getDataFim().atTime(23, 59, 59));
        }

        if (request.getMulticontaRequestDTO() != null) {
            if (Objects.nonNull(request.getMulticontaRequestDTO().getIdUsuario())) {
                whereConditions.add("cm.id_usuario_envio = :idUsuarioEnvio");
                parameters.put("idUsuarioEnvio", request.getMulticontaRequestDTO().getIdUsuario());
            }
            if (Objects.nonNull(request.getMulticontaRequestDTO().getTipoPessoa())) {
                whereConditions.add("cm.tipo_pessoa = :tipoPessoa");
                parameters.put("tipoPessoa", request.getMulticontaRequestDTO().getTipoPessoa());
            }
            if (Objects.nonNull(request.getMulticontaRequestDTO().getIdSubjectus())) {
                whereConditions.add("cm.id_subjectus = :idSubjectus");
                parameters.put("idSubjectus", request.getMulticontaRequestDTO().getIdSubjectus());
            }
            if (Objects.nonNull(request.getMulticontaRequestDTO().getIdDepartamento())) {
                whereConditions.add("cm.id_departamento = :idDepartamento");
                parameters.put("idDepartamento", request.getMulticontaRequestDTO().getIdDepartamento());
            }
        }

        if (!whereConditions.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", whereConditions));
        }

        Query query = entityManager.createNativeQuery(sql.toString(), Tuple.class);
        parameters.forEach(query::setParameter);

        @SuppressWarnings("unchecked")
        List<Tuple> results = query.getResultList();

        return results.stream()
                .map(tuple -> new RelatorioSinteticoDTO(
                        tuple.get("campanha", String.class),
                        tuple.get("nome_emissor", String.class),
                        tuple.get("nome_departamento_emissor", String.class),
                        tuple.get("canal_mensageria", String.class),
                        tuple.get("ordem_fatiamento_lote", Integer.class),
                        tuple.get("total", Long.class),
                        tuple.get("enviados", Long.class),
                        tuple.get("entregue", Long.class),
                        tuple.get("nao_entregue", Long.class),
                        tuple.get("higienizado", Long.class),
                        tuple.get("cancelado", Long.class),
                        tuple.get("retornos", Long.class),
                        toLocalDateTime(tuple.get("data_inicio", Timestamp.class)),
                        toLocalDateTime(tuple.get("data_agendada", Timestamp.class)),
                        toLocalDateTime(tuple.get("data_envio", Timestamp.class)),
                        tuple.get("code", String.class),
                        tuple.get("description", String.class),
                        tuple.get("hash", String.class),
                        tuple.get("external_id", String.class),
                        tuple.get("valor_tarifado", BigDecimal.class)
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<RelatorioAnaliticoDTO> buscarDadosAnaliticos(RelatorioSistelcomRequest request) {

        String baseSql =
                " SELECT " +
                        "   pac.descricao_campanha_mensageria, " +
                        "   pac.nome_emissor, " +
                        "   pac.nome_departamento_emissor, " +
                        "   pac.canal_mensageria, " +
                        "   pac.destino, " +
                        "   pac.mensagem_destinatario, " +
                        "   pac.retorno, " +
                        "   pac.fornecedor, " +
                        "   pac.data_inicio, " +
                        "   pac.data_agendada, " +
                        "   pac.data_envio, " +
                        "   pac.data_confirmacao, " +
                        "   pac.operadora, " +
                        "   pac.situacao, " +
                        "   pac.campo_info, " +
                        "   pac.identificador, " +
                        "   pac.plataforma_envio " +
                        " FROM " +
                        "   sistema_telecomunicacoes.publico_alvo_campanha pac ";

        StringBuilder sql = new StringBuilder(baseSql);
        Map<String, Object> parameters = new HashMap<>();
        List<String> whereConditions = new ArrayList<>();

        if (Objects.nonNull(request.getDataInicio()) && Objects.nonNull(request.getDataFim())) {
            String dateClause = "(pac.data_inicio BETWEEN :dataInicio AND :dataFim " +
                    "OR pac.data_agendada BETWEEN :dataInicio AND :dataFim " +
                    "OR pac.data_envio BETWEEN :dataInicio AND :dataFim)";
            whereConditions.add(dateClause);
            parameters.put("dataInicio", request.getDataInicio().atStartOfDay());
            parameters.put("dataFim", request.getDataFim().atTime(23, 59, 59));
        }

        if (request.getMulticontaRequestDTO() != null) {
            if (Objects.nonNull(request.getMulticontaRequestDTO().getIdUsuario())) {
                whereConditions.add("pac.id_usuario_envio = :idUsuarioEnvio");
                parameters.put("idUsuarioEnvio", request.getMulticontaRequestDTO().getIdUsuario());
            }
            if (Objects.nonNull(request.getMulticontaRequestDTO().getTipoPessoa())) {
                whereConditions.add("pac.tipo_pessoa = :tipoPessoa");
                parameters.put("tipoPessoa", request.getMulticontaRequestDTO().getTipoPessoa());
            }
            if (Objects.nonNull(request.getMulticontaRequestDTO().getIdSubjectus())) {
                whereConditions.add("pac.id_subjectus = :idSubjectus");
                parameters.put("idSubjectus", request.getMulticontaRequestDTO().getIdSubjectus());
            }
            if (Objects.nonNull(request.getMulticontaRequestDTO().getIdDepartamento())) {
                whereConditions.add("pac.id_departamento = :idDepartamento");
                parameters.put("idDepartamento", request.getMulticontaRequestDTO().getIdDepartamento());
            }
        }

        if (!whereConditions.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", whereConditions));
        }

        Query query = entityManager.createNativeQuery(sql.toString(), Tuple.class);
        parameters.forEach(query::setParameter);

        @SuppressWarnings("unchecked")
        List<Tuple> results = query.getResultList();

        return results.stream()
                .map(tuple -> new RelatorioAnaliticoDTO(
                        tuple.get("descricao_campanha_mensageria", String.class),
                        tuple.get("nome_emissor", String.class),
                        tuple.get("nome_departamento_emissor", String.class),
                        tuple.get("canal_mensageria", String.class),
                        tuple.get("destino", String.class),
                        tuple.get("mensagem_destinatario", String.class),
                        tuple.get("retorno", Integer.class),
                        tuple.get("fornecedor", String.class),
                        toLocalDateTime(tuple.get("data_inicio", Timestamp.class)),
                        toLocalDateTime(tuple.get("data_agendada", Timestamp.class)),
                        toLocalDateTime(tuple.get("data_envio", Timestamp.class)),
                        toLocalDateTime(tuple.get("data_confirmacao", Timestamp.class)),
                        tuple.get("operadora", String.class),
                        tuple.get("situacao", String.class),
                        tuple.get("campo_info", String.class),
                        tuple.get("identificador", String.class),
                        tuple.get("plataforma_envio", String.class)
                ))
                .collect(Collectors.toList());
    }

}