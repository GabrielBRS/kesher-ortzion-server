package com.ortzion_technology.ortzion_telecom_server.service.internal.relatorio;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.EstoqueMercadoriaVirtual;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.sistelcom.CampanhaMensageria;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.sistelcom.PublicoAlvoCampanha;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.SistelcomDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.vo.SMSVO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class GeradorRelatorioService {

    public byte[] generatePdfRelatorioEnvio(SistelcomDTO sistelcomDTO, EstoqueMercadoriaVirtual estoqueMercadoriaVirtual, CampanhaMensageria campanhaMensageria, List<PublicoAlvoCampanha> publicoAlvoCampanha, Integer idCanalMensageria) throws IOException {
        int totalSolicitadas = Optional.ofNullable(sistelcomDTO.getTotal()).map(Long::intValue).orElse(0);
        int totalValidas = Optional.ofNullable(sistelcomDTO.getValidos()).map(Long::intValue).orElse(0);
        int totalInvalidas = Optional.ofNullable(sistelcomDTO.getInvalidos()).map(Long::intValue).orElse(0);
        int totalHigienizadas = Optional.ofNullable(sistelcomDTO.getHigienizados()).map(Long::intValue).orElse(0);

        Long estoqueAtual = Optional.ofNullable(estoqueMercadoriaVirtual.getQuantidadeEstoqueCanalMensageria()).orElse(0L);

        String nomeCampanha = (campanhaMensageria != null)
                ? Optional.ofNullable(campanhaMensageria.getCampanha()).orElse("sem campanha")
                : "sem campanha";

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            InputStream logoStream = getClass().getClassLoader().getResourceAsStream("static/img/logo-grande.jpeg");
            if (logoStream == null) {
                throw new IOException("Não foi possível encontrar a logo: static/img/logo-grande.jpeg no classpath.");
            }

            PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, logoStream.readAllBytes(), "logo.jpeg");

            String canalMensageria = "Não definido";
            switch (idCanalMensageria) {
                case 1: canalMensageria = "SMS"; break;
                case 2: canalMensageria = "E-mail"; break;
                case 3: canalMensageria = "WhatsApp"; break;
            }


            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float logoX = 50f;
                float logoY = page.getMediaBox().getHeight() - 100f;
                float logoWidth = 150f;
                float logoHeight = 50f;

                contentStream.drawImage(pdImage, logoX, logoY, logoWidth, logoHeight);

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                contentStream.newLineAtOffset(logoX, logoY - 50f);
                contentStream.showText("Relatório de Envio de Mensagens");
                contentStream.endText();

                float leftMargin = 55f;
                float currentY = logoY - 80f;
                float lineSpacing = 20f;
                float sectionSpacing = 30f;

                writeLine(contentStream, "Campanha:", nomeCampanha, leftMargin, currentY);
                currentY -= lineSpacing;
                writeLine(contentStream, "Canal de Envio:", canalMensageria, leftMargin, currentY);
                currentY -= sectionSpacing;

                writeLine(contentStream, "Total de Mensagens Solicitadas:", String.valueOf(totalSolicitadas), leftMargin, currentY);
                currentY -= lineSpacing;
                writeLine(contentStream, "Total de Mensagens Válidas (para envio):", String.valueOf(totalValidas), leftMargin, currentY);
                currentY -= lineSpacing;
                writeLine(contentStream, "Total de Mensagens Inválidas (erros de formato):", String.valueOf(totalInvalidas), leftMargin, currentY);
                currentY -= lineSpacing;
                writeLine(contentStream, "Total de Mensagens Higienizadas (conteúdo impróprio):", String.valueOf(totalHigienizadas), leftMargin, currentY);

                currentY -= sectionSpacing;

                String descricaoEstoque = "Estoque de Mensagens Disponível ("+canalMensageria+"):";
                writeLine(contentStream, descricaoEstoque, String.valueOf(estoqueAtual), leftMargin, currentY);
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();
        }
    }

    private void writeLine(PDPageContentStream contentStream, String label, String value, float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(label);
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.showText(" " + value);
        contentStream.endText();
    }

    public byte[] generateExcelRelatorioValidos(SistelcomDTO sistelcomDTO) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Contatos Válidos");

            Row headerRow = sheet.createRow(0);
            String[] columns = {"Telefone Completo Destinatário", "Nome Completo Destinatário", "Mensagem"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            int rowNum = 1;
            List<SMSVO.Destinatario> validos = sistelcomDTO.getSmsDestinatarios();
            if (validos == null || validos.isEmpty()) {
                Row row = sheet.createRow(rowNum);
                row.createCell(0).setCellValue("Nenhum contato válido encontrado.");
            } else {
                for (SMSVO.Destinatario valido : validos) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(valido.getNumero());
                    row.createCell(1).setCellValue(valido.getNomeCompleto());
                    row.createCell(2).setCellValue(valido.getMensagem());
                }
            }

            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);

            return out.toByteArray();
        }
    }

    public byte[] gerarZipRelatorios(SistelcomDTO sistelcomDTO, byte[] pdfContents, byte[] validosExcelContents) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (ZipOutputStream zos = new ZipOutputStream(baos)) {

            if (pdfContents != null && pdfContents.length > 0) {
                ZipEntry pdfEntry = new ZipEntry("Relatorio_Principal_Envio.pdf");
                zos.putNextEntry(pdfEntry);
                zos.write(pdfContents);
                zos.closeEntry();
            }

            if (validosExcelContents != null && validosExcelContents.length > 0) {
                ZipEntry validosEntry = new ZipEntry("Relatorio_Contatos_Validos.xlsx");
                zos.putNextEntry(validosEntry);
                zos.write(validosExcelContents);
                zos.closeEntry();
            }

            byte[] relatorioInvalidos = sistelcomDTO.getRelatoriosExcelInvalido();
            if (relatorioInvalidos != null && relatorioInvalidos.length > 0) {
                ZipEntry invalidosEntry = new ZipEntry("Relatorio_Contatos_Invalidos.xlsx");
                zos.putNextEntry(invalidosEntry);
                zos.write(relatorioInvalidos);
                zos.closeEntry();
            }
        }
        return baos.toByteArray();
    }

}
