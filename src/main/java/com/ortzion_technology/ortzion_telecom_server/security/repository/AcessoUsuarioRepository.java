package com.ortzion_technology.ortzion_telecom_server.security.repository;

import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcessoUsuarioRepository extends JpaRepository<AcessoUsuario, Long> {

    @EntityGraph(attributePaths = {"multicontas", "multicontas.acessoGrupoMulticonta"})
    Optional<AcessoUsuario> findByDocumentoUsuario(String documentoUsuario);

    @Query("SELECT u FROM AcessoUsuario u " +
            "LEFT JOIN FETCH u.multicontas mc " +
            "LEFT JOIN FETCH mc.acessoGrupoMulticonta agm " +
            "WHERE u.documentoUsuario = :documentoUsuario")
    Optional<AcessoUsuario> findByDocumentoUsuarioWithMulticontasGraph(@Param("documentoUsuario") String documentoUsuario);

//    @Query("SELECT u FROM AcessoUsuario u LEFT JOIN FETCH u.multicontas WHERE u.documentoUsuario = :documento")
//    Optional<AcessoUsuario> findByDocumentoUsuarioWithMulticontas(@Param("documento") String documento);

    Optional<AcessoUsuario> findByTokenRedefinicao(String token);

}
