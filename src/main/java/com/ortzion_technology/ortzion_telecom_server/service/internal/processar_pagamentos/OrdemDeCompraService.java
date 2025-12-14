package com.ortzion_technology.ortzion_telecom_server.service.internal.processar_pagamentos;

import com.google.protobuf.ServiceException;
import com.ortzion_technology.ortzion_telecom_server.configuration.banco_inter.BancoInterConfiguration;
import com.ortzion_technology.ortzion_telecom_server.configuration.banco_inter.BancoInterDTO;
import com.ortzion_technology.ortzion_telecom_server.configuration.banco_inter.BancoInterService;
import com.ortzion_technology.ortzion_telecom_server.configuration.mercado_virtual.MercadoVirtualCacheService;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CarrinhoComprasRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.banco_digital.MeioPagamento;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Contato;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Endereco;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Pessoa;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.Compra;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.pagarme.PagarmeResponseDTO;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.service.external.integration.pagarme_api.PagarmeCartaoService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.cadastral.PessoaService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual.CompraService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual.PedidoService;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.management.ServiceNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Service
public class OrdemDeCompraService {

    private static final Logger log = LoggerFactory.getLogger(OrdemDeCompraService.class);

    private static final int PIX = 1;
    private static final int CARTAO = 2;
    private static final int BOLETO = 3;

    private final PagarmeCartaoService pagarmeServiceCartao;
    private final BancoInterService bancoInterService;
    private final CompraService compraService;
    private final PedidoService pedidoService;
    private final MeioPagamentoService meioPagamentoService;
    private final PessoaService pessoaService;
    private final BancoInterConfiguration interConfig;
    private final MercadoVirtualCacheService cacheService;

    public OrdemDeCompraService(
            PagarmeCartaoService pagarmeServiceCartao,
            BancoInterService bancoInterService,
            CompraService compraService,
            PedidoService pedidoService,
            MeioPagamentoService meioPagamentoService,
            PessoaService pessoaService,
            BancoInterConfiguration interConfig, MercadoVirtualCacheService cacheService) {
        this.pagarmeServiceCartao = pagarmeServiceCartao;
        this.bancoInterService = bancoInterService;
        this.compraService = compraService;
        this.pedidoService = pedidoService;
        this.meioPagamentoService = meioPagamentoService;
        this.pessoaService = pessoaService;
        this.interConfig = interConfig;
        this.cacheService = cacheService;
    }

    private BigDecimal calcularValorTotal(CarrinhoComprasRequest req) {
        BigDecimal total = BigDecimal.ZERO;
        if (req.getItensCarrinho() != null) {
            for (var item : req.getItensCarrinho()) {
                if (item.getIdPacoteCanalMensageria() != null) {
                    var pacote = cacheService.getPacote(Integer.parseInt(item.getIdPacoteCanalMensageria()));
                    if (pacote != null && pacote.getPrecoPacoteCanalVenda() != null) {
                        BigDecimal qtd = new BigDecimal(item.getQuantidadeMercadoriaVirtual());
                        total = total.add(pacote.getPrecoPacoteCanalVenda().multiply(qtd));
                    }
                }
            }
        }
        return total;
    }

    @Transactional
    public JSONObject criarOrdemCompra(CarrinhoComprasRequest carrinhoComprasRequest, AcessoUsuario usuario) throws ServiceNotFoundException, ServiceException {

        BigDecimal valorTotalEstimado = calcularValorTotal(carrinhoComprasRequest);
        int idFormaPagamento = carrinhoComprasRequest.getFormaDePagamento().getIdFormaPagamento();
        int[] formasValidas = {PIX, CARTAO, BOLETO};

        if (Arrays.stream(formasValidas).noneMatch(id -> id == idFormaPagamento)) {
            throw new IllegalArgumentException("Forma de pagamento com ID " + idFormaPagamento + " não suportada.");
        }

        Compra compra = compraService.buscarCompraRecente(
                carrinhoComprasRequest.getMulticontaRequestDTO().getTipoPessoa(),
                carrinhoComprasRequest.getMulticontaRequestDTO().getIdSubjectus(),
                valorTotalEstimado
        );

        if (compra != null) {
            log.info("Compra duplicada detectada (ID {}). Verificando cache...", compra.getIdCompra());

            if (compra.getMeioPagamento() != null
                    && compra.getMeioPagamento().getFormaPagamento() == idFormaPagamento
                    && compra.getMeioPagamento().getDataExpiracao() != null
                    && compra.getMeioPagamento().getDataExpiracao().isAfter(LocalDateTime.now())) {

                log.info("Reutilizando pagamento em cache.");
                switch (idFormaPagamento) {
                    case PIX: return callbackComPixInter(compra);
                    case BOLETO: return callbackComBoletoInter(compra);
                    case CARTAO:
                        return callbackComCartaoPagarme(compra);
                }
            }

        } else {
            // Se não existe, cria nova
            compra = this.compraService.criarHistoricoCompra(carrinhoComprasRequest, usuario);
            compra = this.pedidoService.criarPedidos(carrinhoComprasRequest.getItensCarrinho(), compra);
        }

        MeioPagamento meioPagamento = compra.getMeioPagamento();
        if (meioPagamento == null) {
            meioPagamento = meioPagamentoService.criarMeioPagamento(idFormaPagamento, compra);
            compra.setMeioPagamento(meioPagamento);
        } else {
            meioPagamento.setFormaPagamento(idFormaPagamento);
        }

        Pessoa pessoa = pessoaService.pegarPessoaPorDocumento(usuario.getDocumentoUsuario());
        if (pessoa == null) {
            throw new ServiceException("Cadastro incompleto. CPF/CNPJ necessário.");
        }

        switch (idFormaPagamento) {
            case PIX: return pagamentoComPixInter(compra, pessoa, meioPagamento);
            case CARTAO: return pagamentoComCartaoPagarme(carrinhoComprasRequest, usuario, compra, meioPagamento);
            case BOLETO: return pagamentoComBoletoInter(compra, pessoa, meioPagamento);
            default: throw new ServiceNotFoundException("Forma de pagamento não suportada.");
        }
    }

    private JSONObject pagamentoComPixInter(Compra compra, Pessoa pessoa, MeioPagamento meioPagamento) {
        try {
            compra.setFormaPagamento(PIX);
            this.compraService.salvar(compra);

            BancoInterDTO.PixCobrancaImediataRequest request = new BancoInterDTO.PixCobrancaImediataRequest();
            BancoInterDTO.PixCobrancaImediataRequest.Calendario cal = new BancoInterDTO.PixCobrancaImediataRequest.Calendario();
            int expiracao = 3600;
            cal.setExpiracao(expiracao);
            request.setCalendario(cal);

            BancoInterDTO.PixCobrancaImediataRequest.Valor val = new BancoInterDTO.PixCobrancaImediataRequest.Valor();
            val.setOriginal(String.format("%.2f", compra.getValorTotalCalculado()).replace(",", "."));
            request.setValor(val);
            request.setChave(interConfig.chavePix());

            String nome = (pessoa.getNomeCompleto() != null && !pessoa.getNomeCompleto().isEmpty()) ? pessoa.getNomeCompleto() : "Cliente Consumidor";
            String doc = pessoa.getDocumento().replaceAll("\\D", "");
            BancoInterDTO.PixCobrancaImediataRequest.Devedor devedor = new BancoInterDTO.PixCobrancaImediataRequest.Devedor();
            devedor.setNome(nome);
            if (doc.length() > 11) devedor.setCnpj(doc); else devedor.setCpf(doc);
            request.setDevedor(devedor);
            request.setSolicitacaoPagador("Pagamento Pedido #" + compra.getIdCompra());

            BancoInterDTO.PixResponseFront pixPronto = bancoInterService.criarPix(request);

            meioPagamento.setTransacaoIdInter(pixPronto.getTxid());
            meioPagamento.setStatusInter("AGUARDANDO_PAGAMENTO");

            meioPagamento.setTextoPagamento(pixPronto.getPixCopiaECola());
            meioPagamento.setDataExpiracao(LocalDateTime.now().plusSeconds(expiracao));

            meioPagamentoService.salvarMeioPagamento(meioPagamento);

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "created");
            jsonResponse.put("txid", pixPronto.getTxid());
            jsonResponse.put("pixCopiaECola", pixPronto.getPixCopiaECola());
            jsonResponse.put("qrcodeBase64", pixPronto.getQrcodeBase64());
            return jsonResponse;

        } catch (Exception e) {
            log.error("Erro ao gerar PIX Inter", e);
            throw new RuntimeException("Erro ao gerar Pix: " + e.getMessage());
        }
    }

    private JSONObject pagamentoComBoletoInter(Compra compra, Pessoa pessoa, MeioPagamento meioPagamento) {
        compra.setFormaPagamento(BOLETO);
        this.compraService.salvar(compra);

        try {
            log.info("Iniciando Boleto Inter V3 para compra ID: {}", compra.getIdCompra());

            Endereco endereco = pessoa.getEndereco();
            Contato contato = pessoa.getContato();
            String nomePagador = (pessoa.getNomeCompleto() != null && !pessoa.getNomeCompleto().isEmpty()) ? pessoa.getNomeCompleto() : "Cliente Consumidor";
            String docLimpo = pessoa.getDocumento().replaceAll("\\D", "");
            String tipoPessoa = docLimpo.length() > 11 ? "JURIDICA" : "FISICA";

            String telefoneRaw = contato.getNumber().replaceAll("\\D", "");
            String ddd = "61";
            String numeroTel = telefoneRaw;
            if (telefoneRaw.length() >= 10) {
                if (telefoneRaw.startsWith("55") && telefoneRaw.length() > 11) telefoneRaw = telefoneRaw.substring(2);
                if (telefoneRaw.length() >= 2) { ddd = telefoneRaw.substring(0, 2); numeroTel = telefoneRaw.substring(2); }
            }
            if (numeroTel.length() > 9) numeroTel = numeroTel.substring(numeroTel.length() - 9);

            BancoInterDTO.Pagador pagador = new BancoInterDTO.Pagador(
                    tipoPessoa, nomePagador, docLimpo, pessoa.getEmail(), ddd, numeroTel,
                    endereco.getLogradouro(), endereco.getNumero() != null ? endereco.getNumero() : "SN",
                    endereco.getComplemento(), endereco.getBairro(), endereco.getCidade(), endereco.getEstado(), endereco.getCep().replaceAll("\\D", "")
            );

            BancoInterDTO.BoletoRequest boletoRequest = new BancoInterDTO.BoletoRequest(
                    "PED-" + compra.getIdCompra(),
                    compra.getValorTotalCalculado(),
                    LocalDate.now().plusDays(3).format(DateTimeFormatter.ISO_DATE),
                    0, pagador
            );

            String jsonRespostaInter = bancoInterService.emitirBoleto(boletoRequest);
            JSONObject jsonFull = new JSONObject(jsonRespostaInter);

            String codigoSolicitacao = jsonFull.optString("codigoSolicitacao");
            if ((codigoSolicitacao == null || codigoSolicitacao.isEmpty()) && jsonFull.has("cobranca")) {
                JSONObject cobrancaObj = jsonFull.optJSONObject("cobranca");
                if (cobrancaObj != null) codigoSolicitacao = cobrancaObj.optString("codigoSolicitacao");
            }

            JSONObject jsonBoleto = jsonFull.has("boleto") ? jsonFull.getJSONObject("boleto") : null;
            String nossoNumero = (jsonBoleto != null) ? jsonBoleto.optString("nossoNumero") : "";
            String linhaDigitavel = (jsonBoleto != null) ? jsonBoleto.optString("linhaDigitavel") : "";
            String codigoBarras = (jsonBoleto != null) ? jsonBoleto.optString("codigoBarras") : "";

            String txid = "";
            if (jsonFull.has("pix")) {
                txid = jsonFull.getJSONObject("pix").optString("txid");
            }

            // Salva IDs
            if (txid != null && !txid.isEmpty()) { meioPagamento.setTransacaoIdInter(txid); }
            else { meioPagamento.setTransacaoIdInter(nossoNumero); }

            meioPagamento.setCodigoPagarme(nossoNumero);
            meioPagamento.setIdPagarme(codigoSolicitacao);

            meioPagamento.setTextoPagamento(linhaDigitavel);
            meioPagamento.setDataExpiracao(LocalDate.now().plusDays(3).atTime(23, 59, 59));

            meioPagamento.setStatusInter("PENDING");
            meioPagamentoService.salvarMeioPagamento(meioPagamento);

            String pdfBase64 = null;
            if (codigoSolicitacao != null && !codigoSolicitacao.isEmpty()) {
                try {
                    pdfBase64 = bancoInterService.recuperarPdfBoleto(codigoSolicitacao);
                } catch (Exception ex) {
                    log.warn("PDF indisponível no momento: {}", ex.getMessage());
                }
            }

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "pending");
            jsonResponse.put("nossoNumero", nossoNumero);
            jsonResponse.put("codigoSolicitacao", codigoSolicitacao);
            jsonResponse.put("linhaDigitavel", linhaDigitavel);
            jsonResponse.put("codigoBarras", codigoBarras);
            jsonResponse.put("pdfBase64", pdfBase64);
            jsonResponse.put("mensagem", "Boleto gerado com sucesso.");

            return jsonResponse;
        } catch (Exception e) {
            log.error("Erro ao gerar Boleto Inter", e);
            throw new RuntimeException("Erro ao gerar Boleto: " + e.getMessage());
        }
    }

    private JSONObject pagamentoComCartaoPagarme(CarrinhoComprasRequest carrinhoComprasRequest, AcessoUsuario usuario, Compra compra, MeioPagamento meioPagamento) throws ServiceException {
        compra.setFormaPagamento(CARTAO);
        this.compraService.salvar(compra);
        PagarmeResponseDTO pagarmeResponse = pagarmeServiceCartao.criarPedidoComCartao(compra, carrinhoComprasRequest, usuario);
        meioPagamento.atualizarMeioPagamento(pagarmeResponse);
        this.meioPagamentoService.salvarMeioPagamento(meioPagamento);

        JSONObject response = new JSONObject();
        response.put("status", pagarmeResponse.getStatus());
        response.put("idPedido", pagarmeResponse.getId());
        return response;
    }

    private JSONObject callbackComPixInter(Compra compra) {
        try {
            MeioPagamento mp = compra.getMeioPagamento();
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "created");
            jsonResponse.put("txid", mp.getTransacaoIdInter());
            jsonResponse.put("pixCopiaECola", mp.getTextoPagamento());

            String qrCodeBase64 = bancoInterService.gerarImagemQrCode(mp.getTextoPagamento());
            jsonResponse.put("qrcodeBase64", qrCodeBase64);

            return jsonResponse;
        } catch (Exception e) {
            log.error("Erro ao recuperar Pix do cache", e);
            throw new RuntimeException("Erro interno ao recuperar Pix.");
        }
    }

    private JSONObject callbackComBoletoInter(Compra compra) {
        try {
            MeioPagamento mp = compra.getMeioPagamento();
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "pending");

            jsonResponse.put("nossoNumero", mp.getCodigoPagarme());
            jsonResponse.put("codigoSolicitacao", mp.getIdPagarme());
            jsonResponse.put("linhaDigitavel", mp.getTextoPagamento());
            jsonResponse.put("mensagem", "Boleto recuperado.");

            String pdfBase64 = null;
            if (mp.getIdPagarme() != null) {
                try {
                    pdfBase64 = bancoInterService.recuperarPdfBoleto(mp.getIdPagarme());
                } catch (Exception ex) {
                    log.warn("PDF indisponível no cache: {}", ex.getMessage());
                }
            }
            jsonResponse.put("pdfBase64", pdfBase64);

            return jsonResponse;
        } catch (Exception e) {
            log.error("Erro ao recuperar Boleto do cache", e);
            throw new RuntimeException("Erro interno ao recuperar Boleto.");
        }
    }

    private JSONObject callbackComCartaoPagarme(Compra compra) {
        MeioPagamento mp = compra.getMeioPagamento();
        JSONObject response = new JSONObject();
        response.put("status", mp.getStatusPagarme());
        response.put("idPedido", mp.getIdPagarme());
        return response;
    }

}