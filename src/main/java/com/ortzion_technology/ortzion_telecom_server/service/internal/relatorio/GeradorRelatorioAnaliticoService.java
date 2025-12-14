package com.ortzion_technology.ortzion_telecom_server.service.internal.relatorio;

import com.ortzion_technology.ortzion_telecom_server.entity.dto.RelatorioAnaliticoDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class GeradorRelatorioAnaliticoService {

    private static final int MAX_ROWS_PER_SHEET = 20000;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final int NUM_COLUNAS_ANALITICO = 17;

    public byte[] gerarRelatorioAnaliticoExcel(List<RelatorioAnaliticoDTO> dtos) throws IOException {

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            CellStyle headerStyle = createHeaderStyle(workbook);

            int totalDtos = dtos.size();
            int dtoIndex = 0;
            int sheetNum = 1;

            while (dtoIndex < totalDtos) {
                Sheet sheet = workbook.createSheet("Página " + sheetNum);

                createAnaliticoHeaderRow(sheet, headerStyle);

                int rowNumInSheet = 1;
                while (rowNumInSheet <= MAX_ROWS_PER_SHEET && dtoIndex < totalDtos) {
                    RelatorioAnaliticoDTO dto = dtos.get(dtoIndex);
                    Row dataRow = sheet.createRow(rowNumInSheet);
                    fillAnaliticoDataRow(dataRow, dto);

                    rowNumInSheet++;
                    dtoIndex++;
                }

                autoSizeAnaliticoColumns(sheet);
                sheetNum++;
            }

            workbook.write(baos);
            return baos.toByteArray();
        }
    }

    private void createAnaliticoHeaderRow(Sheet sheet, CellStyle style) {
        String[] headers = {
                "Campanha", "Emissor", "Departamento Emissor", "Canal", "Destino",
                "Mensagem", "Retorno", "Fornecedor", "Data Início", "Data Agendada",
                "Data Envio", "Data Confirmação", "Operadora", "Situação", "Info",
                "Identificador", "Plataforma"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    }

    private void fillAnaliticoDataRow(Row row, RelatorioAnaliticoDTO dto) {
        int cellNum = 0;
        createCell(row, cellNum++, dto.getDescricaoCampanhaMensageria());
        createCell(row, cellNum++, dto.getNomeEmissor());
        createCell(row, cellNum++, dto.getNomeDepartamentoEmissor());
        createCell(row, cellNum++, dto.getCanalMensageria());
        createCell(row, cellNum++, dto.getDestino());
        createCell(row, cellNum++, dto.getMensagemDestinatario());
        createCell(row, cellNum++, dto.getRetorno());
        createCell(row, cellNum++, dto.getFornecedor());
        createCell(row, cellNum++, dto.getDataInicio());
        createCell(row, cellNum++, dto.getDataAgendada());
        createCell(row, cellNum++, dto.getDataEnvio());
        createCell(row, cellNum++, dto.getDataConfirmacao());
        createCell(row, cellNum++, dto.getOperadora());
        createCell(row, cellNum++, dto.getSituacao());
        createCell(row, cellNum++, dto.getCampoInfo());
        createCell(row, cellNum++, dto.getIdentificador());
        createCell(row, cellNum++, dto.getPlataformaEnvio());
    }

    private void autoSizeAnaliticoColumns(Sheet sheet) {
        for (int i = 0; i < NUM_COLUNAS_ANALITICO; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        return style;
    }

    private void createCell(Row row, int column, Object value) {
        Cell cell = row.createCell(column);
        if (value == null) {
            cell.setBlank();
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) value).doubleValue());
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue(((LocalDateTime) value).format(FORMATTER));
        } else {
            cell.setCellValue(value.toString());
        }
    }

}