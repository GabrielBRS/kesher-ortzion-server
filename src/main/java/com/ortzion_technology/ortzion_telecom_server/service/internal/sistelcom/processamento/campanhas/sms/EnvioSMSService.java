package com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.campanhas.sms;

import com.google.protobuf.ServiceException;
import com.ortzion_technology.ortzion_telecom_server.configuration.pingoo_mobi.PingooMobiConfiguration;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.sistelcom.*;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.pingoo.SistelcomResponseDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.enums.log.StatusEnvioMensagemEnum;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class EnvioSMSService {

    private final RestTemplate restTemplate;
    private final JSONObject configuracoes;

    public EnvioSMSService(RestTemplate restTemplate, PingooMobiConfiguration pingooMobiConfiguration) {
        this.restTemplate = restTemplate;
        this.configuracoes = new JSONObject();
        this.configuracoes.put("token", pingooMobiConfiguration.token());
        this.configuracoes.put("user", pingooMobiConfiguration.user());
        this.configuracoes.put("url", pingooMobiConfiguration.apiUrlSms());
    }

    public CampanhaMensageria enviarSMSEmLote(List<PublicoAlvoCampanha> publicoAlvoCampanhas, CampanhaMensageria campanha) throws ServiceException {

        if(publicoAlvoCampanhas.isEmpty()){
            throw new ServiceException("não há contatos para processar");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<Map<String, Object>> contatos = new ArrayList<>();
        int contador = 1;

        for (PublicoAlvoCampanha publicoAlvoCampanha : publicoAlvoCampanhas) {
            Map<String, Object> contato = new HashMap<>();
            contato.put("number", publicoAlvoCampanha.getDestino());
            contato.put("message", publicoAlvoCampanha.getMensagemDestinatario());
            contato.put("externalid", "campanha-" + campanha.getIdCampanhaMensageria() + "-alvo-" + publicoAlvoCampanha.getIdPublicoAlvoCampanha() + contador++);
            contatos.add(contato);
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("user", this.configuracoes.get("user"));
        payload.put("contact", contatos);
        payload.put("type", 2);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        String urlComToken = this.configuracoes.get("url") + "?token=" + this.configuracoes.get("token");

        try {
            ResponseEntity<SistelcomResponseDTO[]> response = restTemplate.postForEntity(urlComToken, entity, SistelcomResponseDTO[].class);
            SistelcomResponseDTO[] responseArray = response.getBody();

            if (responseArray != null && responseArray.length > 0) {

                SistelcomResponseDTO sistelcomData = responseArray[0];

                for (PublicoAlvoCampanha publicoAlvoCampanha : publicoAlvoCampanhas) {
                    publicoAlvoCampanha.setStatus("ENTREGUE");
                    publicoAlvoCampanha.setIdStatusMensagem(StatusEnvioMensagemEnum.ENTREGUE.getCodigoNumerico());
                    publicoAlvoCampanha.setSituacao("ENTREGUE");
                    publicoAlvoCampanha.setDataEnvio(LocalDateTime.now());
                }

                //Relatorio Sintetico
                campanha.setEntregue((long) publicoAlvoCampanhas.size());
                campanha.setEnviados((long) publicoAlvoCampanhas.size());
                campanha.setNaoEntregue(0L);
                campanha.setCode(sistelcomData.getCode());
                campanha.setDescription(sistelcomData.getDescription());
                campanha.setHash(sistelcomData.getHash());
                campanha.setExternalId(sistelcomData.getExternalId());
                campanha.setDataEnvio(LocalDateTime.now());

                campanha.setPublicosAlvoCampanha(publicoAlvoCampanhas);

                return campanha;

            }else{
                throw new IllegalStateException("Resposta da API de SMS vazia ou inválida.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar SMS: " + e.getMessage(), e);
        }

    }

}