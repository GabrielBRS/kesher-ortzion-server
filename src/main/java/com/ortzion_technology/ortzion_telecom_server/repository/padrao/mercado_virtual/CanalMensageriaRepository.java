package com.ortzion_technology.ortzion_telecom_server.repository.padrao.mercado_virtual;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.CanalMensageria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CanalMensageriaRepository extends JpaRepository<CanalMensageria, Integer> {

    @Query(value = """
    SELECT cm
    FROM CanalMensageria cm
    """, nativeQuery = false)
    List<CanalMensageria> pegarTodosCanaisMensageria();

    @Query(value = """
    SELECT cm
    FROM CanalMensageria cm
    WHERE cm.idCanalMensageria = :idCanalMensageria
      OR cm.nomeCanalMensageria ILIKE CONCAT('%', :nomeCanalMensageria, '%')
    """, nativeQuery = false)
    List<CanalMensageria> pegarCanalMEnsageriaPorIdOuNome(@Param("idCanalMensageria") Integer idCanalMensageria, @Param("nomeCanalMensageria") String nomeCanalMensageria);

}
