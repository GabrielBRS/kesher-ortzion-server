package com.ortzion_technology.ortzion_telecom_server.repository.padrao.banco_digital;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.banco_digital.MeioPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MeioPagamentoRepository extends JpaRepository<MeioPagamento, Long> {

    @Query("SELECT mp FROM MeioPagamento mp WHERE mp.transacaoIdInter = :transacaoIdInter")
    MeioPagamento pegarMeioPagamentoPorIdTransacaoInter(@Param("transacaoIdInter") String transacaoIdInter);

    @Query("SELECT mp FROM MeioPagamento mp WHERE mp.transacaoIdInter = :id OR mp.codigoPagarme = :id OR mp.idPagarme = :id")
    MeioPagamento buscarPorQualquerIdInter(@Param("id") String id);

}
