package com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.documentos.sms;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.PedidoDisparoCanalMensageriaSMSRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.SistelcomDTO;
import com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.filtros.SmsFilterService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@Component("csvSmsProcessor")
public class DocumentoCsvSMSService extends AbstractSmsDocumentProcessor {

    public DocumentoCsvSMSService(SmsFilterService smsFilterService) {
        super(smsFilterService);
    }

    @Override
    public String getTipoSuportado() {
        return "csv";
    }

    @Override
    public SistelcomDTO processamento(MultipartFile arquivo, PedidoDisparoCanalMensageriaSMSRequest pedido) throws IOException {
        List<String[]> linhas = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(arquivo.getInputStream()))) {
            reader.skip(1);
            linhas = reader.readAll();
        } catch (CsvException e) {
            throw new RuntimeException("Erro ao ler o arquivo CSV", e);
        }
        return processarLinhas(linhas, pedido);
    }
}