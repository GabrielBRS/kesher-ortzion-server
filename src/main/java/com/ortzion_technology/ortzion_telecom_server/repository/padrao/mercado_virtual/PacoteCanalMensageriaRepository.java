package com.ortzion_technology.ortzion_telecom_server.repository.padrao.mercado_virtual;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.CanalMensageria;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.PacoteCanalMensageria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacoteCanalMensageriaRepository extends JpaRepository<PacoteCanalMensageria, Integer> {

    @Query("SELECT p FROM PacoteCanalMensageria p " +
            "LEFT JOIN FETCH p.descricoes d " +
            "LEFT JOIN FETCH p.itemPacoteCanalMensageria i")
    List<PacoteCanalMensageria> pegarTodosPacoteCanalMensageria();

    @Query("SELECT p FROM PacoteCanalMensageria p " +
            "LEFT JOIN FETCH p.descricoes d " +
            "LEFT JOIN FETCH p.itemPacoteCanalMensageria i " +
            "WHERE p.tipoPacoteCanalMensageria = :tipoPacote ")
    List<PacoteCanalMensageria> pegarTodosPacoteCanaisMensageriaPorTipo(
            @Param("tipoPacote") Integer tipoPacote
    );

    @Query(value = """
    SELECT pcm
    FROM PacoteCanalMensageria pcm
    WHERE pcm.idPacoteCanalMensageria = :idPacoteCanalMensageria
      OR pcm.nomePacoteCanalMensageria ILIKE CONCAT('%', :nomePacoteCanalMensageria, '%')
    """, nativeQuery = false)
    List<PacoteCanalMensageria> pegarPacoteCanalMensageriaPorIdOuNome(@Param("idPacoteCanalMensageria") Integer idPacoteCanalMensageria, @Param("nomePacoteCanalMensageria") String nomePacoteCanalMensageria);

    @Query(
            """
             select pcm from PacoteCanalMensageria pcm where pcm.idPacoteCanalMensageria in (:idPacotesCanaisMensagerias)
            """
    )
    List<PacoteCanalMensageria> pegarPorIdsDePacotes(@Param("idPacotesCanaisMensagerias") List<Integer> idPacotesCanaisMensagerias);

}
