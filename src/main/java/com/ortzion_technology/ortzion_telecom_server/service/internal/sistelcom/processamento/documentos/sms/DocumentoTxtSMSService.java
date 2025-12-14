package com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.documentos.sms;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.PedidoDisparoCanalMensageriaSMSRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.SistelcomDTO;
import com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.filtros.SmsFilterService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component("txtSmsProcessor")
public class DocumentoTxtSMSService extends AbstractSmsDocumentProcessor {

    public DocumentoTxtSMSService(SmsFilterService smsFilterService) {
        super(smsFilterService);
    }

    @Override
    public String getTipoSuportado() {
        return "txt";
    }

    @Override
    public SistelcomDTO processamento(MultipartFile arquivo, PedidoDisparoCanalMensageriaSMSRequest pedido) throws IOException {
        List<String[]> linhas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(arquivo.getInputStream()))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    linhas.add(new String[]{linha.trim()});
                }
            }
        }
        return processarLinhas(linhas, pedido);
    }
}
