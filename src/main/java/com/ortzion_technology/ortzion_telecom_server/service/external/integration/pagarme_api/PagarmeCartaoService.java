package com.ortzion_technology.ortzion_telecom_server.service.external.integration.pagarme_api;

import com.fasterxml.jackson.databind.ObjectMapper; // CORREÇÃO: Import necessário
import com.google.protobuf.ServiceException;
import com.ortzion_technology.ortzion_telecom_server.configuration.pagarme_stone.PagarmeConfiguration;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CarrinhoComprasRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Pessoa;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.Compra; // CORREÇÃO: Usaremos este
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.pagarme.PagarmeResponseDTO;
import com.ortzion_technology.ortzion_telecom_server.security.dto.UsuarioLogadoDTO;
import com.ortzion_technology.ortzion_telecom_server.security.repository.AcessoUsuarioRepository;
import com.ortzion_technology.ortzion_telecom_server.security.service.SecurityService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.cadastral.PessoaService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PagarmeCartaoService {

    private final RestTemplate restTemplate;
    private final AcessoUsuarioRepository acessoUsuarioRepository;
    private final PagarmeConfiguration pagarmeConfiguration;
    private final ObjectMapper objectMapper;
    private final SecurityService securityService;
    private final PessoaService pessoaService;

    public PagarmeCartaoService(RestTemplate restTemplate, AcessoUsuarioRepository acessoUsuarioRepository, PagarmeConfiguration pagarmeConfiguration, ObjectMapper objectMapper, SecurityService securityService, PessoaService pessoaService) {
        this.restTemplate = restTemplate;
        this.acessoUsuarioRepository = acessoUsuarioRepository;
        this.pagarmeConfiguration = pagarmeConfiguration;
        this.objectMapper = objectMapper;
        this.securityService = securityService;
        this.pessoaService = pessoaService;
    }

    public PagarmeResponseDTO criarPedidoComCartao(Compra compra, CarrinhoComprasRequest carrinhoRequest, AcessoUsuario usuario) throws ServiceException {
        if (carrinhoRequest.getFormaDePagamento() == null || carrinhoRequest.getFormaDePagamento().getCartao() == null) {
            throw new IllegalArgumentException("Dados de pagamento (cartão) não foram fornecidos na requisição.");
        }
        CarrinhoComprasRequest.FormaDePagamento.Cartao cartaoInfo = carrinhoRequest.getFormaDePagamento().getCartao();

        String validadeRaw = cartaoInfo.getValidade();
        String mes;
        String ano;
        if (validadeRaw.contains("/")) {
            String[] validade = validadeRaw.split("/");
            mes = validade[0];
            ano = validade[1];
        } else if (validadeRaw.length() == 4) {
            mes = validadeRaw.substring(0, 2);
            ano = validadeRaw.substring(2);
        } else {
            throw new IllegalArgumentException("Formato de validade do cartão inválido: " + validadeRaw);
        }

        Optional<AcessoUsuario> acessoUsuario = acessoUsuarioRepository.findByDocumentoUsuario(usuario.getDocumentoUsuario());

        if (acessoUsuario.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado com permissões e dados.");
        }


        Pessoa pessoa = this.pessoaService.pegarPessoaPorDocumento(acessoUsuario.get().getDocumentoUsuario());

        if (pessoa == null) {
            throw new ServiceException("Dados incompletos");
        }

        UsuarioLogadoDTO usuarioLogadoDTO = this.securityService.montarUsuarioLogadoDTO(acessoUsuario.get(), pessoa);

        List<Map<String, Object>> itemsPayload = compra.getItemPedidos().stream()
                .map(item -> Map.<String, Object>of(
                        "amount", item.getPrecoUnitarioNoMomentoDaCompra().multiply(BigDecimal.valueOf(100)).intValue(),
                        "description", item.getPacoteCanalMensageria().getNomePacoteCanalMensageria(),
                        "quantity", item.getQuantidade().intValue(),
                        "code", item.getPacoteCanalMensageria().getIdPacoteCanalMensageria().toString()
                ))
                .collect(Collectors.toList());

        Map<String, Object> payload = Map.of(
                "closed", true,
                "items", itemsPayload,
                "customer", Map.of(
                        "name", String.format("%s %s", usuarioLogadoDTO.getNome(), usuarioLogadoDTO.getSobrenome()).trim(),
                        "email", usuarioLogadoDTO.getEmail(),
                        "document", usuarioLogadoDTO.getDocumento(),
                        "type", "individual"
                ),
                "payments", List.of(
                        Map.of(
                                "payment_method", "credit_card",
                                "credit_card", Map.of(
                                        "installments", Integer.parseInt(cartaoInfo.getParcelas()),
                                        "statement_descriptor", "Ortzion",
                                        "card", Map.of(
                                                "number", cartaoInfo.getNumeroCartao(),
                                                "holder_name", cartaoInfo.getNomeTitularCartao(),
                                                "exp_month", Integer.parseInt(mes),
                                                "exp_year", Integer.parseInt(ano),
                                                "cvv", cartaoInfo.getCvv(),
                                                "billing_address", Map.of(
                                                        "line_1", String.format("%s, %s, %s",
                                                                usuarioLogadoDTO.getLogradouro(),
                                                                usuarioLogadoDTO.getNumero(),
                                                                usuarioLogadoDTO.getBairro()),
                                                        "zip_code", usuarioLogadoDTO.getCep(),
                                                        "city", usuarioLogadoDTO.getCidade(),
                                                        "state", usuarioLogadoDTO.getEstado(),
                                                        "country", "BR" // Padrão Brasil
                                                )
                                        )
                                )
                        )
                )
        );

        ResponseEntity<Map> responseEntity = criarPedido(payload);
        return objectMapper.convertValue(responseEntity.getBody(), PagarmeResponseDTO.class);
    }

    private ResponseEntity<Map> criarPedido(Map<String, Object> payload) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(this.pagarmeConfiguration.key(), "");
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        return restTemplate.postForEntity(this.pagarmeConfiguration.url(), request, Map.class);
    }
}