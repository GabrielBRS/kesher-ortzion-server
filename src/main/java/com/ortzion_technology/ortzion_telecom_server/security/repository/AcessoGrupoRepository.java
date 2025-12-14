package com.ortzion_technology.ortzion_telecom_server.security.repository;

import com.ortzion_technology.ortzion_telecom_server.security.dto.AcessoGrupoDTO;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface AcessoGrupoRepository extends JpaRepository<AcessoGrupo, Long> {

    AcessoGrupo getByIdGrupo(Long idGrupo);

    Set<AcessoGrupo> getByDescricaoGrupoUsuario(String descricaoGrupoUsuario);

    @Query(
            """
             select ag from AcessoGrupo ag where ag.idGrupo in (:idGrupo)
            """
    )
    List<AcessoGrupo> buscarAcessoGrupoPorId(@Param("idGrupo") List<Long> idsGrupo);

}
