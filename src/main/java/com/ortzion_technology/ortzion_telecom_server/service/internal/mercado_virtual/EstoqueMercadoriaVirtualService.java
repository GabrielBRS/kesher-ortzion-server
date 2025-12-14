package com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.CanalMensageria;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.EstoqueMercadoriaVirtual;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.Compra;
import com.ortzion_technology.ortzion_telecom_server.repository.dao.EstoqueMercadoriaVirtualDAOImpl;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.EstoqueMercadoriaVirtualDTO;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.mercado_virtual.EstoqueMercadoriaVirtualRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstoqueMercadoriaVirtualService {

    private final EstoqueMercadoriaVirtualRepository estoqueMercadoriaVirtualRepository;
    private final EstoqueMercadoriaVirtualDAOImpl estoqueMercadoriaVirtualDAOImpl;

    private static final Logger log = LoggerFactory.getLogger(EstoqueMercadoriaVirtualService.class);

    public EstoqueMercadoriaVirtualService(EstoqueMercadoriaVirtualRepository estoqueMercadoriaVirtualRepository, EstoqueMercadoriaVirtualDAOImpl estoqueMercadoriaVirtualDAOImpl) {
        this.estoqueMercadoriaVirtualRepository = estoqueMercadoriaVirtualRepository;
        this.estoqueMercadoriaVirtualDAOImpl = estoqueMercadoriaVirtualDAOImpl;
    }

    public EstoqueMercadoriaVirtual criarEstoque(EstoqueMercadoriaVirtual estoqueMercadoriaVirtual){
        return estoqueMercadoriaVirtualRepository.save(estoqueMercadoriaVirtual);
    }

    public List<EstoqueMercadoriaVirtual> pegarTodosEstoquesMulticonta(EstoqueMercadoriaVirtualDTO estoqueMercadoriaVirtualDTO, AcessoUsuario usuario) {
        return this.estoqueMercadoriaVirtualDAOImpl.pegarTodosEstoquesMulticonta(estoqueMercadoriaVirtualDTO.getMulticontaRequestDTO().getTipoPessoa(), estoqueMercadoriaVirtualDTO.getMulticontaRequestDTO().getIdSubjectus(), estoqueMercadoriaVirtualDTO.getMulticontaRequestDTO().getIdDepartamento());
    }

    public EstoqueMercadoriaVirtual pegarEstoquesMulticontaPorCanalMensageria(EstoqueMercadoriaVirtualDTO estoqueMercadoriaVirtualDTO, AcessoUsuario usuario) {
        return this.estoqueMercadoriaVirtualDAOImpl.pegarEstoquesMulticontaPorCanalMensageria(estoqueMercadoriaVirtualDTO.getMulticontaRequestDTO().getTipoPessoa(), estoqueMercadoriaVirtualDTO.getMulticontaRequestDTO().getIdSubjectus(), estoqueMercadoriaVirtualDTO.getMulticontaRequestDTO().getIdDepartamento(), estoqueMercadoriaVirtualDTO.getIdCanalMensageria());
    }

    public EstoqueMercadoriaVirtual acrescentarEstoque(
            EstoqueMercadoriaVirtual estoqueMercadoriaVirtual,
            Long acrescentarQuantidade) {

        Long quantidadeAtual = estoqueMercadoriaVirtual.getQuantidadeEstoqueCanalMensageria();
        Long novaQuantidade = quantidadeAtual + acrescentarQuantidade;
        estoqueMercadoriaVirtual.setQuantidadeEstoqueCanalMensageria(novaQuantidade);

        return this.estoqueMercadoriaVirtualRepository.save(estoqueMercadoriaVirtual);
    }

    @Transactional
    public EstoqueMercadoriaVirtual subtrairEstoque(EstoqueMercadoriaVirtual estoque, Long quantidadeParaSubtrair) {
        if (estoque == null || quantidadeParaSubtrair == null || quantidadeParaSubtrair <= 0) {
            log.warn("Tentativa de subtrair estoque com dados inválidos. Estoque ou quantidade nula/zerada.");
            return estoque;
        }
        long quantidadeAtual = estoque.getQuantidadeEstoqueCanalMensageria();
        long novaQuantidade = quantidadeAtual - quantidadeParaSubtrair;

        estoque.setQuantidadeEstoqueCanalMensageria(Math.max(0L, novaQuantidade));

        return estoqueMercadoriaVirtualRepository.save(estoque);
    }

    public EstoqueMercadoriaVirtual atualizarEstoque(EstoqueMercadoriaVirtual estoqueMercadoriaVirtual) {
        return this.estoqueMercadoriaVirtualRepository.save(estoqueMercadoriaVirtual);
    }

    public EstoqueMercadoriaVirtual pegarEstoquePorCanalMensageria(Compra compra, CanalMensageria canalMensageria) {
        return this.estoqueMercadoriaVirtualDAOImpl.pegarEstoquesMulticontaPorCanalMensageria(compra.getTipoPessoa(), compra.getIdSubjectus(), compra.getIdDepartamento() ,canalMensageria.getIdCanalMensageria());
    }

}