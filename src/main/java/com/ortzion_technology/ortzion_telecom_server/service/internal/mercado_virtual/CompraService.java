package com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CarrinhoComprasRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.Compra;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.entity.enums.log.StatusPagamentoEnum;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.mercado_virtual.CompraRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompraService {

    private final CompraRepository compraRepository;

    public CompraService(CompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    public List<Compra> pegarHistoricoCompra(CarrinhoComprasRequest carrinhoComprasRequest, AcessoUsuario usuario) {
        return this.compraRepository.pegarHistoricoCompra(carrinhoComprasRequest.getMulticontaRequestDTO().getIdUsuario(), carrinhoComprasRequest.getMulticontaRequestDTO().getTipoPessoa(), carrinhoComprasRequest.getMulticontaRequestDTO().getIdSubjectus(), carrinhoComprasRequest.getMulticontaRequestDTO().getIdDepartamento());
    }

    public List<Compra> pegarHistoricoCompraPorConta(MulticontaRequestDTO multiconta, AcessoUsuario usuario) {
        return this.compraRepository.pegarHistoricoCompra(multiconta.getIdUsuario(), multiconta.getTipoPessoa(), multiconta.getIdSubjectus(), multiconta.getIdDepartamento());
    }

    public Compra salvar(Compra pedidos){
        return compraRepository.save(pedidos);
    }

    public Compra criarHistoricoCompra(CarrinhoComprasRequest carrinhoComprasRequest, AcessoUsuario usuario) {

        Long idColaborador = carrinhoComprasRequest.getMulticontaRequestDTO().getIdColaborador() != null ? carrinhoComprasRequest.getMulticontaRequestDTO().getIdColaborador() : null;

        Compra compra = new Compra();
        compra.setTipoPessoa(carrinhoComprasRequest.getMulticontaRequestDTO().getTipoPessoa());
        compra.setIdSubjectus(carrinhoComprasRequest.getMulticontaRequestDTO().getIdSubjectus());
        compra.setIdDepartamento(carrinhoComprasRequest.getMulticontaRequestDTO().getIdDepartamento());
        compra.setIdColaborador(idColaborador);
        compra.setDataCompra(LocalDateTime.now());
        compra.setStatusPagamento(StatusPagamentoEnum.PROCESSANDO.getCodigoNumerico());
        compra.setIdUsuarioLogado(usuario.getIdUsuario());

        return compraRepository.save(compra);

    }

    public Compra pegarHistoricoCompraWebhook(String idPagarme, String codigoPagarme){
        return this.compraRepository.pegarHistoricoCompraWebhook(idPagarme, codigoPagarme);
    }

    public List<Compra> pegarCartoesParaProcessar(Integer idStatusCartao){
        return this.compraRepository.pegarCartoesParaProcessar(idStatusCartao);
    }

    public List<Compra> pegarComprasPagasParaProcessar(Integer idStatusCartao){
        return this.compraRepository.pegarComprasPagasParaProcessar(idStatusCartao);
    }

    public Compra pegarCompraPorTransacaoInter(String transacaoId) {
        return this.compraRepository.findByMeioPagamento_TransacaoIdInter(transacaoId).orElse(null);
    }

    public Compra buscarCompraRecente(Integer tipoPessoa, Long idSubjectus, BigDecimal valorTotal) {

        LocalDateTime limite = LocalDateTime.now().minusMinutes(10);

        List<Compra> encontradas = compraRepository.findDuplicidadeRecente(
                tipoPessoa,
                idSubjectus,
                valorTotal,
                StatusPagamentoEnum.PROCESSANDO.getCodigoNumerico(),
                limite
        );

        if (!encontradas.isEmpty()) {
            return encontradas.get(0);
        }
        return null;
    }

}
