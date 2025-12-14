package com.ortzion_technology.ortzion_telecom_server.controller.v1.api.internal.mercado_virtual;

import com.ortzion_technology.ortzion_telecom_server.configuration.banco_inter.BancoInterService;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CarrinhoComprasRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.Compra;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.security.service.SecurityService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual.CompraService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.processar_pagamentos.OrdemDeCompraService;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/compra")
public class CompraController {

    private final CompraService compraService;
    private final OrdemDeCompraService ordemDeCompraService;
    private final SecurityService securityService;
    private final BancoInterService bancoInterService;

    public CompraController(CompraService compraService, OrdemDeCompraService ordemDeCompraService, SecurityService securityService, BancoInterService bancoInterService) {
        this.compraService = compraService;
        this.ordemDeCompraService = ordemDeCompraService;
        this.securityService = securityService;
        this.bancoInterService = bancoInterService;
    }

    @RequestMapping(value = "/pegar", method = RequestMethod.POST)
    public ResponseEntity<?> pegarHistoricoCompra(@RequestBody CarrinhoComprasRequest carrinhoComprasRequest) {
        AcessoUsuario usuario = securityService.getUsuarioLogado();
        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }
        securityService.verificarAcessoPermissoesMulticonta(usuario, carrinhoComprasRequest.getMulticontaRequestDTO());
        List<Compra> compra = this.compraService.pegarHistoricoCompra(carrinhoComprasRequest, usuario);
        return ResponseEntity.ok(compra);
    }

    @RequestMapping(value = "/pegar/conta", method = RequestMethod.POST)
    public ResponseEntity<?> pegarHistoricoCompraPorConta(@RequestBody MulticontaRequestDTO multiconta) {
        AcessoUsuario usuario = securityService.getUsuarioLogado();
        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }
        securityService.verificarAcessoPermissoesMulticonta(usuario, multiconta);
        List<Compra> compra = this.compraService.pegarHistoricoCompraPorConta(multiconta, usuario);
        return ResponseEntity.ok(compra);
    }

    @RequestMapping(value = "/cadastrar/compra", method = RequestMethod.POST)
    public ResponseEntity<?> cadastrarPedidoCompra(@RequestBody CarrinhoComprasRequest carrinhoComprasRequest) {

        if (carrinhoComprasRequest == null || carrinhoComprasRequest.getFormaDePagamento() == null || carrinhoComprasRequest.getFormaDePagamento().getIdFormaPagamento() == null) {
            return ResponseEntity.badRequest().body("Forma de pagamento inválida.");
        }

        if (carrinhoComprasRequest.getItensCarrinho() == null) {
            return ResponseEntity.badRequest().body("Carrinho vazio.");
        }

        AcessoUsuario usuario = securityService.getUsuarioLogado();
        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        securityService.verificarAcessoPermissoesMulticonta(usuario, carrinhoComprasRequest.getMulticontaRequestDTO());

        try {
            JSONObject objectResponse = ordemDeCompraService.criarOrdemCompra(carrinhoComprasRequest, usuario);
            return ResponseEntity.ok(objectResponse.toMap());

        } catch (HttpClientErrorException e) {
            System.err.println("Erro ao processar pagamento: " + e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", "Erro ao processar pagamento", "detail", e.getResponseBodyAsString()));
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("message", "Erro interno ao processar compra", "error", e.getMessage()));
        }

    }

    @GetMapping("/boleto/{codigoSolicitacao}/pdf")
    public ResponseEntity<?> baixarPdfBoleto(@PathVariable String codigoSolicitacao) {
        try {
            String pdfBase64 = bancoInterService.recuperarPdfBoleto(codigoSolicitacao);

            if (pdfBase64 != null) {
                return ResponseEntity.ok(Map.of("pdf", pdfBase64));
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Erro ao baixar PDF", "error", e.getMessage()));
        }
    }

}