package com.ortzion_technology.ortzion_telecom_server.repository.padrao.sistelcom;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.sistelcom.CampanhaMensageria;
import com.ortzion_technology.ortzion_telecom_server.repository.abstrato.customizado.CampanhaMensageriaRepositoryCustom;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CampanhaMensageriaRepository extends JpaRepository<CampanhaMensageria, Long>, CampanhaMensageriaRepositoryCustom {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
            value = """
            SELECT cm.idCampanhaMensageria FROM CampanhaMensageria cm
            WHERE cm.idStatusCampanha = :idStatusCampanha
            AND cm.idPrioridadeMensagem = :idPrioridadeMensagem
            AND cm.dataAgendada <= :dataAtual
            ORDER BY cm.dataAgendada ASC
            """
    )
    List<Long> pegarIdsCampanhasPendentesPrioridadeAlta(
            @Param("idStatusCampanha") Integer idStatusCampanha,
            @Param("idPrioridadeMensagem") Integer idPrioridadeMensagem,
            @Param("dataAtual") LocalDateTime dataAtual,
            Pageable pageable
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
            value = """
            SELECT cm.idCampanhaMensageria FROM CampanhaMensageria cm
            WHERE cm.idStatusCampanha = :idStatusCampanha
            AND cm.idPrioridadeMensagem = :idPrioridadeMensagem
            AND cm.dataAgendada <= :dataAtual
            ORDER BY cm.dataAgendada ASC
            """
    )
    List<Long> pegarIdsCampanhasPendentesPrioridadeMedia(
            @Param("idStatusCampanha") Integer idStatusCampanha,
            @Param("idPrioridadeMensagem") Integer idPrioridadeMensagem,
            @Param("dataAtual") LocalDateTime dataAtual,
            Pageable pageable
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
            value = """
            SELECT cm.idCampanhaMensageria FROM CampanhaMensageria cm
            WHERE cm.idStatusCampanha = :idStatusCampanha
            AND cm.idPrioridadeMensagem = :idPrioridadeMensagem
            AND cm.dataAgendada <= :dataAtual
            ORDER BY cm.dataAgendada ASC
            """
    )
    List<Long> pegarIdsCampanhasPendentesPrioridadeBaixa(
            @Param("idStatusCampanha") Integer idStatusCampanha,
            @Param("idPrioridadeMensagem") Integer idPrioridadeMensagem,
            @Param("dataAtual") LocalDateTime dataAtual,
            Pageable pageable
    );

    @Query(
            value = """
            SELECT DISTINCT cm FROM CampanhaMensageria cm
            LEFT JOIN FETCH cm.publicosAlvoCampanha
            WHERE cm.idCampanhaMensageria IN :ids
            ORDER BY cm.dataAgendada ASC
            """
    )
    List<CampanhaMensageria> pegarCampanhasComPublicoAlvoPorIds(@Param("ids") List<Long> ids);

}