package com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual;

import com.ortzion_technology.ortzion_telecom_server.configuration.mercado_virtual.MercadoVirtualCacheService;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CarrinhoComprasRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.Compra;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.ItemPedido;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.PacoteCanalMensageria;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.mercado_virtual.CompraRepository;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.mercado_virtual.PacoteCanalMensageriaRepository;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.mercado_virtual.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);
    private final PedidoRepository pedidoRepository;
    private final CompraRepository compraRepository;
    private final MercadoVirtualCacheService mercadoVirtualCacheService;
    private final PacoteCanalMensageriaRepository pacoteRepository; // Injetado para Fallback

    public PedidoService(PedidoRepository pedidoRepository,
                         CompraRepository compraRepository,
                         MercadoVirtualCacheService mercadoVirtualCacheService,
                         PacoteCanalMensageriaRepository pacoteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.compraRepository = compraRepository;
        this.mercadoVirtualCacheService = mercadoVirtualCacheService;
        this.pacoteRepository = pacoteRepository;
    }

    public List<ItemPedido> pegarTodosContratoComerciais() {
        return pedidoRepository.findAll();
    }

    public ItemPedido salvar(ItemPedido pedidos) {
        return pedidoRepository.save(pedidos);
    }

    @Transactional
    public Compra criarPedidos(List<CarrinhoComprasRequest.ItensCarrinho> itensCarrinho, Compra compra) {

        if (itensCarrinho == null || itensCarrinho.isEmpty()) {
            return compraRepository.save(compra);
        }

        for (var itemDoCarrinho : itensCarrinho) {
            if (itemDoCarrinho.getIdPacoteCanalMensageria() == null) continue;

            Integer idPacote = Integer.parseInt(itemDoCarrinho.getIdPacoteCanalMensageria());

            PacoteCanalMensageria pacote = mercadoVirtualCacheService.getPacote(idPacote);

            if (pacote == null) {
                log.warn("Pacote ID {} não encontrado no cache. Buscando no banco de dados...", idPacote);
                pacote = pacoteRepository.findById(idPacote).orElse(null);
            }

            if (pacote != null) {
                ItemPedido itemPedido = new ItemPedido();
                itemPedido.setPacoteCanalMensageria(pacote);
                itemPedido.setCompra(compra);
                itemPedido.setQuantidade(Long.valueOf(itemDoCarrinho.getQuantidadeMercadoriaVirtual()));

                BigDecimal preco = pacote.getPrecoPacoteCanalVenda() != null ?
                        pacote.getPrecoPacoteCanalVenda() : BigDecimal.ZERO;
                itemPedido.setPrecoUnitarioNoMomentoDaCompra(preco);

                compra.getItemPedidos().add(itemPedido);
            } else {
                log.error("Pacote ID {} não encontrado nem no cache nem no banco!", idPacote);
            }
        }

        calcularValorTotal(compra);

        return compraRepository.save(compra);
    }

    public Compra calcularValorTotal(Compra compra) {
        if (compra == null || compra.getItemPedidos() == null) {
            return compra;
        }

        BigDecimal valorTotalCalculado = compra.getItemPedidos().stream()
                .filter(Objects::nonNull)
                .map(ItemPedido::getValorTotalItem)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        compra.setValorCompra(valorTotalCalculado);

        return compra;
    }

}