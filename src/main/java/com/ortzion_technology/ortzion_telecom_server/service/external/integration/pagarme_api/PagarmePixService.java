package com.ortzion_technology.ortzion_telecom_server.service.external.integration.pagarme_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ServiceException;
import com.ortzion_technology.ortzion_telecom_server.configuration.pagarme_stone.PagarmeConfiguration;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Pessoa;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.Compra;
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
public class PagarmePixService {

    private final RestTemplate restTemplate;
    private final AcessoUsuarioRepository acessoUsuarioRepository;
    private final PagarmeConfiguration pagarmeConfiguration;
    private final ObjectMapper objectMapper;
    private final SecurityService securityService;
    private final PessoaService pessoaService;

    public PagarmePixService(RestTemplate restTemplate, AcessoUsuarioRepository acessoUsuarioRepository, PagarmeConfiguration pagarmeConfiguration, ObjectMapper objectMapper, SecurityService securityService, PessoaService pessoaService) {
        this.restTemplate = restTemplate;
        this.acessoUsuarioRepository = acessoUsuarioRepository;
        this.pagarmeConfiguration = pagarmeConfiguration;
        this.objectMapper = objectMapper;
        this.securityService = securityService;
        this.pessoaService = pessoaService;
    }

    public PagarmeResponseDTO criarPedidoComPix(Compra compra, AcessoUsuario usuario) throws ServiceException {

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
                .map(item -> {
                    int amountEmCentavos = item.getPrecoUnitarioNoMomentoDaCompra()
                            .multiply(BigDecimal.valueOf(100))
                            .intValue();
                    return Map.<String, Object>of(
                            "amount", amountEmCentavos,
                            "description", item.getPacoteCanalMensageria().getNomePacoteCanalMensageria(),
                            "quantity", item.getQuantidade(),
                            "code", item.getPacoteCanalMensageria().getIdPacoteCanalMensageria().toString()
                    );
                })
                .collect(Collectors.toList());

        Map<String, Object> payload = Map.of(
//                "closed", true,
                "customer", Map.of(
                        "name", Optional.ofNullable(usuarioLogadoDTO.getNome()).orElse("Cliente") + " " + Optional.ofNullable(usuarioLogadoDTO.getSobrenome()).orElse("Teste"),
                        "type", "individual",
                        "email", Optional.ofNullable(usuarioLogadoDTO.getEmail()).orElse("teste@gmail.com"),
                        "document", Optional.ofNullable(usuarioLogadoDTO.getDocumento()).orElse("22820546013"),
                        "phones", Map.of(
                                "home_phone", Map.of(
                                        "country_code", "55",
                                        "area_code", Optional.ofNullable(usuarioLogadoDTO.getAreaCode()).orElse("11"),
                                        "number", Optional.ofNullable(usuarioLogadoDTO.getTelefone()).orElse("933071581")
                                )
                        )
                ),
                "items", itemsPayload,
                "payments", List.of(
                        Map.of(
                                "payment_method", "pix",
                                "pix", Map.of(
                                        "expires_in", 86400,
                                        "additional_information", List.of(
                                                Map.of("name", "Valor Total", "value", compra.getValorTotalCalculado().toString())
                                        )
                                )
                        )
                )
        );

        ResponseEntity<Map> responseEntity = criarPedido(payload);

        try {
            // Usamos o objectMapper para formatar o Map em um JSON legível (Pretty Print)
            String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseEntity.getBody());
            System.out.println("\n=======================================================");
            System.out.println("RESPOSTA COMPLETA DA PAGAR.ME PARA PIX:");
            System.out.println(jsonResponse);
            System.out.println("=======================================================\n");
        } catch (Exception e) {
            System.err.println("Erro ao logar resposta da Pagar.me: " + e.getMessage());
        }

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