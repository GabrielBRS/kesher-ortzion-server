package com.ortzion_technology.ortzion_telecom_server.service.internal.processar_pagamentos;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.banco_digital.MeioPagamento;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.Compra;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.banco_digital.MeioPagamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MeioPagamentoService {

    private final MeioPagamentoRepository meioPagamentoRepository;

    public MeioPagamentoService(MeioPagamentoRepository meioPagamentoRepository) {
        this.meioPagamentoRepository = meioPagamentoRepository;
    }

    public MeioPagamento salvarMeioPagamento(MeioPagamento meioPagamento) {
        return meioPagamentoRepository.save(meioPagamento);
    }

    public MeioPagamento atualizar(MeioPagamento meioPagamento) {
        return meioPagamentoRepository.save(meioPagamento);
    }

    public MeioPagamento criarMeioPagamento(Integer idMeioPagamento, Compra compra) {
        MeioPagamento novoMeioPagamento = new MeioPagamento();
        novoMeioPagamento.setFormaPagamento(idMeioPagamento);
        novoMeioPagamento.setCompra(compra);
        novoMeioPagamento.setDataPagamento(LocalDateTime.now());
        return meioPagamentoRepository.save(novoMeioPagamento);
    }


    public MeioPagamento pegarMeioPagamentoPorIdTransacaoInter(String idTransacao) {
        return meioPagamentoRepository.buscarPorQualquerIdInter(idTransacao);
    }

}
