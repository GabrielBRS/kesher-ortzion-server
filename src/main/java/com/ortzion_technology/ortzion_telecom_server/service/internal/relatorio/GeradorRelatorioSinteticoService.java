package com.ortzion_technology.ortzion_telecom_server.service.internal.relatorio;

import com.ortzion_technology.ortzion_telecom_server.entity.dto.RelatorioSinteticoDTO;
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
public class GeradorRelatorioSinteticoService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // ATUALIZADO: O número total de colunas agora é 20
    private static final int NUM_COLUNAS_SINTETICO = 20;

    public byte[] gerarRelatorioSinteticoExcel(List<RelatorioSinteticoDTO> dtos) throws IOException {

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            CellStyle headerStyle = createHeaderStyle(workbook);
            Sheet sheet = workbook.createSheet("Relatório Sintético");

            createSinteticoHeaderRow(sheet, headerStyle);

            int rowNum = 1;
            for (RelatorioSinteticoDTO dto : dtos) {
                Row dataRow = sheet.createRow(rowNum++);
                fillSinteticoDataRow(dataRow, dto); // Este método foi corrigido
            }

            autoSizeSinteticoColumns(sheet); // Este método foi corrigido

            workbook.write(baos);
            return baos.toByteArray();
        }
    }

    // ATUALIZADO: Cabeçalhos correspondem à nova query
    private void createSinteticoHeaderRow(Sheet sheet, CellStyle style) {
        String[] headers = {
                "Campanha", "Emissor", "Departamento Emissor", "Canal Mensageria",
                "Ordem Lote", "Total", "Enviados", "Entregue", "Não Entregue",
                "Higienizado", "Cancelado", "Retornos", "Data Início", "Data Agendada",
                "Data Envio", "Code", "Descrição", "Hash", "External ID", "Valor Tarifado"
        };

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    }

    // ATUALIZADO: Métodos Get() correspondem ao novo DTO e à ordem da query
    private void fillSinteticoDataRow(Row row, RelatorioSinteticoDTO dto) {
        int cellNum = 0;
        createCell(row, cellNum++, dto.getCampanha());
        createCell(row, cellNum++, dto.getNomeEmissor());
        createCell(row, cellNum++, dto.getNomeDepartamentoEmissor());
        createCell(row, cellNum++, dto.getCanalMensageria());
        createCell(row, cellNum++, dto.getOrdemFatiamentoLote());
        createCell(row, cellNum++, dto.getTotal());
        createCell(row, cellNum++, dto.getEnviados());
        createCell(row, cellNum++, dto.getEntregue());
        createCell(row, cellNum++, dto.getNaoEntregue());
        createCell(row, cellNum++, dto.getHigienizado());
        createCell(row, cellNum++, dto.getCancelado());
        createCell(row, cellNum++, dto.getRetornos());
        createCell(row, cellNum++, dto.getDataInicio());
        createCell(row, cellNum++, dto.getDataAgendada());
        createCell(row, cellNum++, dto.getDataEnvio());
        createCell(row, cellNum++, dto.getCode());
        createCell(row, cellNum++, dto.getDescription());
        createCell(row, cellNum++, dto.getHash());
        createCell(row, cellNum++, dto.getExternalId());
        createCell(row, cellNum++, dto.getValorTarifado());
    }

    // ATUALIZADO: Autosize para 20 colunas
    private void autoSizeSinteticoColumns(Sheet sheet) {
        for (int i = 0; i < NUM_COLUNAS_SINTETICO; i++) {
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