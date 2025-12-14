package com.ortzion_technology.ortzion_telecom_server.repository.padrao.mercado_virtual;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

    @Query(
            """
                select hc from Compra hc
                            where
                            hc.idUsuarioLogado = :idUsuario
                            and hc.tipoPessoa = :tipoPessoa
                            and hc.idSubjectus = :idSubject
                            and hc.idDepartamento = :idDepartamento
            """
    )
    List<Compra> pegarHistoricoCompra(
            @Param("idUsuario") Long idUsuario,
            @Param("tipoPessoa") Integer tipoPessoa,
            @Param("idSubject") Long idSubject,
            @Param("idDepartamento") Integer idDepartamento);

    @Query(
            """
             select hc from Compra hc
                         where hc.meioPagamento.idPagarme = :idPagarme
                         and hc.meioPagamento.codigoPagarme = :codigoPagarme
            """
    )
    Compra pegarHistoricoCompraWebhook(@Param("idPagarme") String idPagarme, @Param("codigoPagarme") String codigoPagarme);

    @Query(
            """
             select hc from Compra hc where hc.meioPagamento.statusProcessamentoPagamento = :idStatusCartao
            """
    )
    List<Compra> pegarCartoesParaProcessar(@Param("idStatusCartao") Integer idStatusCartao);

    Optional<Compra> findByMeioPagamento_TransacaoIdInter(String transacaoIdInter);

    @Query(
            """
             select hc from Compra hc where hc.meioPagamento.statusProcessamentoPagamento = :idStatusCompra
            """
    )
    List<Compra> pegarComprasPagasParaProcessar(@Param("idStatusCompra") Integer idStatusCompra);

    @Modifying
    @Query("DELETE FROM Compra c WHERE c.statusPagamento = :statusPendente AND c.dataCompra < :dataLimite")
    int deletarComprasAntigasNaoPagas(@Param("dataLimite") LocalDateTime dataLimite,
                                      @Param("statusPendente") Integer statusPendente);

    @Query("SELECT c FROM Compra c WHERE c.tipoPessoa = :tipoPessoa AND c.idSubjectus = :idSubjectus AND c.statusPagamento = :statusPendente AND c.valorCompra = :valor AND c.dataCompra >= :dataLimite ORDER BY c.dataCompra DESC")
    List<Compra> findDuplicidadeRecente(@Param("tipoPessoa") Integer tipoPessoa,
                                        @Param("idSubjectus") Long idSubjectus,
                                        @Param("valor") BigDecimal valor,
                                        @Param("statusPendente") Integer statusPendente,
                                        @Param("dataLimite") LocalDateTime dataLimite);

}
