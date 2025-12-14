package com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.documentos.sms;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.PedidoDisparoCanalMensageriaSMSRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.SistelcomDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.vo.SMSVO;
import com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.filtros.SmsFilterService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component("excelSmsProcessor")
public class DocumentoExcelSMSService extends AbstractSmsDocumentProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DocumentoExcelSMSService.class);

    private static final int COL_DDI = 0;
    private static final int COL_DDD = 1;
    private static final int COL_TELEFONE = 2;
    private static final int COL_MENSAGEM = 3;

    public DocumentoExcelSMSService(SmsFilterService smsFilterService) {
        super(smsFilterService);
    }

    @Override
    public String getTipoSuportado() {
        return "xlsx";
    }

    @Override
    public SistelcomDTO processamento(MultipartFile arquivo, PedidoDisparoCanalMensageriaSMSRequest pedido) throws IOException {
        logger.info("Iniciando leitura do arquivo Excel: {}", arquivo.getOriginalFilename());
        List<String[]> linhas = new ArrayList<>();

        try (InputStream is = arquivo.getInputStream();
             XSSFWorkbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            boolean isHeader = true;
            for (Row row : sheet) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                if (row.getCell(COL_DDI) == null || getCellValueAsString(row.getCell(COL_DDI)).trim().isEmpty()) {
                    continue;
                }
                String[] colunas = new String[4];
                colunas[0] = getCellValueAsString(row.getCell(COL_DDI));
                colunas[1] = getCellValueAsString(row.getCell(COL_DDD));
                colunas[2] = getCellValueAsString(row.getCell(COL_TELEFONE));
                colunas[3] = getCellValueAsString(row.getCell(COL_MENSAGEM));
                linhas.add(colunas);
            }
        } catch (Exception e) {
            logger.error("Falha ao ler o arquivo Excel.", e);
            throw new IOException("Não foi possível ler o arquivo Excel. Verifique o formato.", e);
        }

        SistelcomDTO sistelcomDTO = super.processarLinhas(linhas, pedido);

        byte[] relatorioExcel = generateInvalidContactsExcel(sistelcomDTO);
        sistelcomDTO.setRelatoriosExcelInvalido(relatorioExcel);

        return sistelcomDTO;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> {
                try {
                    yield cell.getStringCellValue();
                } catch (IllegalStateException e) {
                    yield String.valueOf((long) cell.getNumericCellValue());
                }
            }
            default -> "";
        };
    }

    private byte[] generateInvalidContactsExcel(SistelcomDTO sistelcomDTO) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Contatos Inválidos");

            Row headerRow = sheet.createRow(0);
            String[] columns = {"Telefone", "Motivo da Invalidade"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            int rowNum = 1;
            List<SMSVO.Removidos> removidos = sistelcomDTO.getSmsRemovidos();
            if (removidos == null || removidos.isEmpty()) {
                Row row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue("Nenhum contato inválido encontrado.");
            } else {
                for (SMSVO.Removidos invalid : removidos) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(invalid.getNumero());
                    row.createCell(1).setCellValue(invalid.getMensagem());
                }
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }
}