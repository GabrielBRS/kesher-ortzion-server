package com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.documentos.email;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.PedidoDisparoCanalMensageriaEmailRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.SistelcomDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.vo.EmailVO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;


@Component
public class DocumentoExcelEmailService {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public SistelcomDTO processamento(MultipartFile excel, PedidoDisparoCanalMensageriaEmailRequest pedidoDisparoCanalMensageriaEmailRequest, MultipartFile modeloHtmlEmail) {

        List<EmailVO.Destinatario> destinatarios = new ArrayList<>();
        List<EmailVO.Removidos> higienizadas = new ArrayList<>();
        SistelcomDTO sistelcomDTO = new SistelcomDTO();

        String htmlTemplateContent = "";
        if (modeloHtmlEmail != null && !modeloHtmlEmail.isEmpty()) {
            try {
                htmlTemplateContent = StreamUtils.copyToString(modeloHtmlEmail.getInputStream(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao ler o arquivo HTML do modelo de e-mail", e);
            }
        }

        try (InputStream is = excel.getInputStream();
             XSSFWorkbook workbook = new XSSFWorkbook(is)) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            boolean isHeader = true;

            for (Row row : sheet) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                Cell emailDestinatarioCell = row.getCell(0);
                Cell nomeCompletoDestinatarioCell = row.getCell(1);
                Cell bodyEmailCell = row.getCell(2);
                Cell emailRemetenteCell = row.getCell(3);
                Cell nomeCompletoRemetenteCell = row.getCell(4);
                Cell tituloEmailCell = row.getCell(5);

                String emailDestinatario = getCellValueAsString(emailDestinatarioCell).trim();
                String nomeCompletoDestinatario = getCellValueAsString(nomeCompletoDestinatarioCell).trim();
                String bodyEmail = getCellValueAsString(bodyEmailCell).trim();
                String emailRemetente = getCellValueAsString(emailRemetenteCell).trim();
                String nomeCompletoRemetente = getCellValueAsString(nomeCompletoRemetenteCell).trim();
                String tituloEmail = getCellValueAsString(tituloEmailCell).trim();

                if (emailDestinatario.matches(EMAIL_REGEX)) {
                    if(!bodyEmail.isEmpty() && pedidoDisparoCanalMensageriaEmailRequest.getConteudoMensagemUnico() == true) {
                        EmailVO.Destinatario novoDestinatario = new EmailVO.Destinatario();
                        novoDestinatario.setEmailDestinatario(emailDestinatario);
                        novoDestinatario.setNomeDestinatario(nomeCompletoDestinatario);
                        novoDestinatario.setEmailRemetente(emailRemetente);
                        novoDestinatario.setNomeRemetente(nomeCompletoRemetente);
                        novoDestinatario.setTituloEmail(tituloEmail);
                        novoDestinatario.setBodyEmail(bodyEmail);
                        destinatarios.add(novoDestinatario);
                    }else if(!htmlTemplateContent.isEmpty()){
                        EmailVO.Destinatario novoDestinatario = new EmailVO.Destinatario();
                        novoDestinatario.setEmailDestinatario(emailDestinatario);
                        novoDestinatario.setNomeDestinatario(nomeCompletoDestinatario);
                        novoDestinatario.setEmailRemetente(emailRemetente);
                        novoDestinatario.setNomeRemetente(nomeCompletoRemetente);
                        novoDestinatario.setTituloEmail(tituloEmail);
                        novoDestinatario.setBodyEmail(htmlTemplateContent);
                        destinatarios.add(novoDestinatario);
                    }else{
                        EmailVO.Removidos destinatarioHigienizado = new EmailVO.Removidos();
                        destinatarioHigienizado.setEmailDestinatario(Optional.ofNullable(emailDestinatario).orElse("***"));
                        destinatarioHigienizado.setNomeDestinatario(Optional.ofNullable(nomeCompletoRemetente).orElse("***"));
                        destinatarioHigienizado.setTituloEmail(Optional.ofNullable(tituloEmail).orElse("***"));
                        destinatarioHigienizado.setBodyEmail(Optional.ofNullable(htmlTemplateContent).orElse("***"));
                        destinatarioHigienizado.setEmailRemetente(Optional.ofNullable(emailRemetente).orElse("***"));
                        destinatarioHigienizado.setNomeRemetente(Optional.ofNullable(nomeCompletoRemetente).orElse("***"));
                        higienizadas.add(destinatarioHigienizado);
                    }

                }else{
                    EmailVO.Removidos destinatarioHigienizado = new EmailVO.Removidos();
                    destinatarioHigienizado.setEmailDestinatario(Optional.ofNullable(emailDestinatario).orElse("***"));
                    destinatarioHigienizado.setNomeDestinatario(Optional.ofNullable(nomeCompletoRemetente).orElse("***"));
                    destinatarioHigienizado.setTituloEmail(Optional.ofNullable(tituloEmail).orElse("***"));
                    destinatarioHigienizado.setBodyEmail(Optional.ofNullable(bodyEmail).orElse("***"));
                    destinatarioHigienizado.setEmailRemetente(Optional.ofNullable(emailRemetente).orElse("***"));
                    destinatarioHigienizado.setNomeRemetente(Optional.ofNullable(nomeCompletoRemetente).orElse("***"));
                    higienizadas.add(destinatarioHigienizado);
                }

            }

            sistelcomDTO.setInvalidos(Optional.ofNullable(((long) higienizadas.size())).orElse(0L));
            sistelcomDTO.setValidos(Optional.ofNullable(((long) destinatarios.size())).orElse(0L));
            sistelcomDTO.setTotal( Optional.ofNullable(((long) destinatarios.size())).orElse(0L) + Optional.ofNullable(((long) higienizadas.size())).orElse(0L) );

            sistelcomDTO.setEmailDestinatarios(destinatarios);
            sistelcomDTO.setEmailRemovidos(higienizadas);

            return sistelcomDTO;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar arquivo Excel para SMS", e);
        }

    }

    // Método auxiliar para leitura robusta de células (copiado do DocumentoExcelEmailService)
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue()); // Para números longos como telefones
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> {
                try {
                    yield cell.getStringCellValue();
                } catch (IllegalStateException e) {
                    yield String.valueOf(cell.getNumericCellValue());
                }
            }
            case BLANK -> "";
            default -> "";
        };
    }
}