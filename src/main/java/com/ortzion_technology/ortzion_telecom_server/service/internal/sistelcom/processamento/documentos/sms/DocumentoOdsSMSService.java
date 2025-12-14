package com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.documentos.sms;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.PedidoDisparoCanalMensageriaSMSRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.SistelcomDTO;
import com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.filtros.SmsFilterService;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component("odsSmsProcessor")
public class DocumentoOdsSMSService extends AbstractSmsDocumentProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DocumentoOdsSMSService.class);

    private static final int COL_DDI = 0;
    private static final int COL_DDD = 1;
    private static final int COL_TELEFONE = 2;
    private static final int COL_MENSAGEM = 3;

    public DocumentoOdsSMSService(SmsFilterService smsFilterService) {
        super(smsFilterService);
    }

    @Override
    public String getTipoSuportado() {
        return "ods";
    }

    @Override
    public SistelcomDTO processamento(MultipartFile arquivo, PedidoDisparoCanalMensageriaSMSRequest pedido) throws IOException {
        logger.info("Iniciando leitura do arquivo ODS: {}", arquivo.getOriginalFilename());
        List<String[]> linhas = new ArrayList<>();

        try (InputStream is = arquivo.getInputStream()) {
            SpreadsheetDocument spreadsheetDoc = SpreadsheetDocument.loadDocument(is);
            Table sheet = spreadsheetDoc.getSheetByIndex(0);

            boolean isHeader = true;
            for (Row row : sheet.getRowList()) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                if (row.getCellCount() == 0 || row.getCellByIndex(COL_DDI).getStringValue().trim().isEmpty()) {
                    continue;
                }

                String[] colunas = new String[4];
                colunas[0] = getCellValueAsString(row, COL_DDI);
                colunas[1] = getCellValueAsString(row, COL_DDD);
                colunas[2] = getCellValueAsString(row, COL_TELEFONE);
                colunas[3] = getCellValueAsString(row, COL_MENSAGEM);
                linhas.add(colunas);
            }

        } catch (Exception e) {
            logger.error("Falha ao ler o arquivo ODS.", e);
            throw new IOException("Não foi possível ler o arquivo ODS. Verifique o formato.", e);
        }

        return super.processarLinhas(linhas, pedido);
    }

    private String getCellValueAsString(Row row, int index) {
        if (index < row.getCellCount()) {
            return row.getCellByIndex(index).getStringValue();
        }
        return "";
    }
}
