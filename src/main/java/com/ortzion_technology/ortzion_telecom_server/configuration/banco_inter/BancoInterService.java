package com.ortzion_technology.ortzion_telecom_server.configuration.banco_inter;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class BancoInterService {

    private final RestTemplate interRestTemplate;
    private final BancoInterConfiguration config;
    private static final Logger log = LoggerFactory.getLogger(BancoInterService.class);

    private String accessToken;
    private LocalDateTime tokenExpiration;

    public BancoInterService(@Qualifier("interRestTemplate") RestTemplate interRestTemplate,
                             BancoInterConfiguration config) {
        this.interRestTemplate = interRestTemplate;
        this.config = config;
    }

    private synchronized String getAccessToken() {
        if (accessToken != null && tokenExpiration != null && LocalDateTime.now().isBefore(tokenExpiration)) {
            return accessToken;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", config.clientId());
        map.add("client_secret", config.clientSecret());
        map.add("grant_type", "client_credentials");
        map.add("scope", "pix.read pix.write cob.read cob.write cobv.read cobv.write webhook.read webhook.write boleto-cobranca.read boleto-cobranca.write payloadlocation.read payloadlocation.write");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<Map> response = interRestTemplate.postForEntity(config.authUrl(), request, Map.class);

            if (response.getBody() != null) {
                this.accessToken = (String) response.getBody().get("access_token");
                Integer expiresIn = (Integer) response.getBody().get("expires_in");
                this.tokenExpiration = LocalDateTime.now().plusSeconds(expiresIn - 600);
                return this.accessToken;
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Falha ao autenticar no Banco Inter: " + e.getResponseBodyAsString());
        }
        throw new RuntimeException("Falha crítica: Token nulo retornado pelo Inter");
    }

    public BancoInterDTO.PixResponseFront criarPix(BancoInterDTO.PixCobrancaImediataRequest payload) {
        String token = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String txid = UUID.randomUUID().toString().replace("-", "");

        HttpEntity<BancoInterDTO.PixCobrancaImediataRequest> request = new HttpEntity<>(payload, headers);

        try {
            String url = config.urlBase() + "/pix/v2/cob/" + txid;
            ResponseEntity<BancoInterDTO.PixCobrancaImediataResponse> response = interRestTemplate.exchange(
                    url, HttpMethod.PUT, request, BancoInterDTO.PixCobrancaImediataResponse.class
            );

            BancoInterDTO.PixCobrancaImediataResponse body = response.getBody();

            if (body != null && body.getPixCopiaECola() != null) {
                String base64Image = gerarImagemQrCode(body.getPixCopiaECola());
                BancoInterDTO.PixResponseFront frontResponse = new BancoInterDTO.PixResponseFront();
                frontResponse.setTxid(body.getTxid());
                frontResponse.setPixCopiaECola(body.getPixCopiaECola());
                frontResponse.setQrcodeBase64(base64Image);
                return frontResponse;
            }
            throw new RuntimeException("Inter retornou sucesso mas sem dados do Pix.");

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Erro Inter PIX: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar imagem do QR Code: " + e.getMessage());
        }
    }

    public String emitirBoleto(BancoInterDTO.BoletoRequest payload) {
        String token = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<BancoInterDTO.BoletoRequest> request = new HttpEntity<>(payload, headers);

        try {

            String urlPost = config.urlBase() + "/cobranca/v3/cobrancas";
            ResponseEntity<Map> responsePost = interRestTemplate.postForEntity(urlPost, request, Map.class);

            if (responsePost.getBody() == null || !responsePost.getBody().containsKey("codigoSolicitacao")) {
                throw new RuntimeException("Inter não retornou codigoSolicitacao na v3.");
            }

            String codigoSolicitacao = (String) responsePost.getBody().get("codigoSolicitacao");

            String urlGet = config.urlBase() + "/cobranca/v3/cobrancas/" + codigoSolicitacao;
            HttpEntity<Void> requestGet = new HttpEntity<>(headers);

            ResponseEntity<String> responseGet = interRestTemplate.exchange(urlGet, HttpMethod.GET, requestGet, String.class);

            return responseGet.getBody();

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Erro Inter Boleto V3: " + e.getResponseBodyAsString());
        }
    }

    public void configurarWebhook(String chavePix, String suaUrlDoWebhook) {
        String token = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payload = new HashMap<>();
        payload.put("webhookUrl", suaUrlDoWebhook);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

        try {
            String url = config.urlBase() + "/pix/v2/webhook/" + chavePix;
            interRestTemplate.exchange(url, HttpMethod.PUT, request, Void.class);
            System.out.println("SUCESSO: Webhook cadastrado no Inter para a URL: " + suaUrlDoWebhook);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Erro ao cadastrar Webhook: " + e.getResponseBodyAsString());
        }
    }

    public String consultarWebhook(String chavePix) {
        String token = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            String url = config.urlBase() + "/pix/v2/webhook/" + chavePix;
            ResponseEntity<Map> response = interRestTemplate.exchange(url, HttpMethod.GET, request, Map.class);
            if (response.getBody() != null) {
                return (String) response.getBody().get("webhookUrl");
            }
            return null;
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar webhook: " + e.getMessage());
        }
    }

    public String recuperarPdfBoleto(String codigoSolicitacao) {

        String token = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            String url = config.urlBase() + "/cobranca/v3/cobrancas/" + codigoSolicitacao + "/pdf";

            ResponseEntity<Map> response = interRestTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    Map.class
            );

            if (response.getBody() != null) {
                return (String) response.getBody().get("pdf");
            }
            return null;
        } catch (Exception e) {
            log.error("Erro ao recuperar PDF do boleto (V3). Código: {}", codigoSolicitacao, e);
            throw new RuntimeException("Erro ao recuperar PDF: " + e.getMessage());
        }

    }

    public String gerarImagemQrCode(String text) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(pngData);
    }

}