package com.ortzion_technology.ortzion_telecom_server.repository.padrao.sistelcom;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.sistelcom.PublicoAlvoCampanha;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PublicoAlvoCampanhaRepository extends JpaRepository<PublicoAlvoCampanha, Long> {

    @Query(
            value = """
                select pac from PublicoAlvoCampanha pac where pac.campanhaMensageria.idCampanhaMensageria = :idCampanhaMensageria
            """, nativeQuery = false
    )
    List<PublicoAlvoCampanha> pegarPublicoAlvoCampanha(@Param("idCampanhaMensageria")  Long idCampanhaMensageria);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
            value = """
            SELECT pac FROM PublicoAlvoCampanha pac
            WHERE pac.idStatusMensagem = :idStatusMensagem
            AND pac.campanhaMensageria.idCampanhaMensageria = :idCampanhaMensageria
            AND pac.dataAgendada <= :dataAtual
            ORDER BY pac.dataAgendada ASC
            """
    )
    List<PublicoAlvoCampanha> pegarPublicoAlvoCampanhaPendentesParaProcessar(
            @Param("idStatusMensagem") Integer idStatusMensagem,
            @Param("idCampanhaMensageria") Long idCampanhaMensageria,
            @Param("dataAtual") LocalDateTime dataAtual,
            Pageable pageable
    );

    @Modifying
    @Query(
            value = """
        UPDATE PublicoAlvoCampanha pac
        SET pac.idStatusMensagem = :statusFalha,
            pac.status = 'FALHOU',
            pac.dataEnvio = :dataFalha
        WHERE pac.campanhaMensageria.idCampanhaMensageria = :idCampanha
        AND pac.idStatusMensagem IN (:statusProcessando, :statusEnviando)
        """
    )
    void marcarPendentesComoFalha(
            @Param("idCampanha") Long idCampanha,
            @Param("statusFalha") Integer statusFalha,
            @Param("statusProcessando") Integer statusProcessando,
            @Param("statusEnviando") Integer statusEnviando,
            @Param("dataFalha") LocalDateTime dataFalha
    );

}
