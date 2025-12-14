package com.ortzion_technology.ortzion_telecom_server.security.repository;

import com.ortzion_technology.ortzion_telecom_server.security.entity.Multiconta;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaResponseDTO;
import com.ortzion_technology.ortzion_telecom_server.security.entity.MulticontaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MulticontaRepository extends JpaRepository<Multiconta, MulticontaId> {

    @Query(
            "SELECT new com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaResponseDTO(m.multicontaId.idUsuario, m.multicontaId.tipoPessoa, m.multicontaId.idSubjectus, m.multicontaId.idDepartamento, m.statusMulticonta, m.username, p, e, d) " +
                    "FROM Multiconta m " +
                    "LEFT JOIN Pessoa p ON p.idPessoa = m.multicontaId.idSubjectus " +
                    "LEFT JOIN Empresa e ON e.idEmpresa = m.multicontaId.idSubjectus " +
                    "LEFT JOIN Departamento d ON d.idDepartamento = m.multicontaId.idDepartamento " +
                    "WHERE m.multicontaId.idUsuario = :idAcessoUsuario"
    )
    List<MulticontaResponseDTO> pegarTodasMulticontasPorAcessoUsuario(@Param("idAcessoUsuario") Long idAcessoUsuario);

    @Query(
            "SELECT new com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaResponseDTO(m.multicontaId.idUsuario, m.multicontaId.tipoPessoa, m.multicontaId.idSubjectus, m.multicontaId.idDepartamento, m.statusMulticonta, m.username, p, e, d) " +
                    "FROM Multiconta m " +
                    "LEFT JOIN Pessoa p ON p.idPessoa = m.multicontaId.idSubjectus " +
                    "LEFT JOIN Empresa e ON e.idEmpresa = m.multicontaId.idSubjectus " +
                    "LEFT JOIN Departamento d ON d.idDepartamento = m.multicontaId.idDepartamento " +
                    "WHERE m.multicontaId.idUsuario = :idUsuario"
    )
    List<MulticontaResponseDTO> pegarTodasMulticontasPorMulticonta(@Param("idUsuario") Long idUsuario);

    List<Multiconta> findByMulticontaId_IdUsuario(Long idUsuario);

    @Query(
            "SELECT new com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaResponseDTO(m.multicontaId.idUsuario, m.multicontaId.tipoPessoa, m.multicontaId.idSubjectus, m.multicontaId.idDepartamento, m.statusMulticonta, m.username, p, e, d) " +
                    "FROM Multiconta m " +
                    "LEFT JOIN Pessoa p ON p.idPessoa = m.multicontaId.idSubjectus " +
                    "LEFT JOIN Empresa e ON e.idEmpresa = m.multicontaId.idSubjectus " +
                    "LEFT JOIN Departamento d ON d.idDepartamento = m.multicontaId.idDepartamento " +
                    "WHERE " +
                    " m.multicontaId.tipoPessoa = :tipoPessoa " +
                    " AND m.multicontaId.idSubjectus = :idEmpresa " +
                    " AND m.multicontaId.idDepartamento = :idDepartamento "
    )
    List<MulticontaResponseDTO> pegarMulticontasPorIdEmpresaEIdDepartamento(@Param("tipoPessoa") Integer tipoPessoa, @Param("idEmpresa") Long idEmpresa, @Param("idDepartamento") Integer idDepartamento);


    @Query(
            "SELECT m " +
                    "FROM Multiconta m " +
                    "WHERE " +
                    " m.multicontaId.idUsuario = :idUsuario " +
                    " AND m.multicontaId.tipoPessoa = :tipoPessoa " +
                    " AND m.multicontaId.idSubjectus = :idEmpresa " +
                    " AND m.multicontaId.idDepartamento = :idDepartamento "
    )
    Optional<Multiconta> pegarMulticontaPorChaveCompleta(
            @Param("idUsuario") Long idUsuario,
            @Param("tipoPessoa") Integer tipoPessoa,
            @Param("idEmpresa") Long idEmpresa,
            @Param("idDepartamento") Integer idDepartamento);

    @Query(
            "SELECT m " +
                    "FROM Multiconta m " +
                    "WHERE " +
                    " m.username = :nomeUsuario "
    )
    Optional<Multiconta> buscarMulticontaPorNomeUsuario(
            @Param("nomeUsuario") String nomeUsuario);

}