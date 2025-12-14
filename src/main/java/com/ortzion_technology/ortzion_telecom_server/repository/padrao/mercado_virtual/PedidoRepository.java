package com.ortzion_technology.ortzion_telecom_server.repository.padrao.mercado_virtual;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<ItemPedido, Long> {

    @Query(
            """
             SELECT DISTINCT p FROM ItemPedido p
             JOIN p.pacoteCanalMensageria pcm
             WHERE pcm.idPacoteCanalMensageria IN (:idsDosPacotes)
            """
    )
    List<ItemPedido> pegarPedidosPorIdsDePacotes(List<Integer> idsDosPacotes);

}
