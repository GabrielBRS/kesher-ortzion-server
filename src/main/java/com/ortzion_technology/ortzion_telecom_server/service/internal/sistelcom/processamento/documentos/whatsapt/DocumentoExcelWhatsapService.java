package com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.documentos.whatsapt;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.PedidoDisparoCanalMensageriaWhatsapRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.SistelcomDTO;
import com.ortzion_technology.ortzion_telecom_server.entity.vo.WhatsappVO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
public class DocumentoExcelWhatsapService {

    public SistelcomDTO processamento(MultipartFile excel, PedidoDisparoCanalMensageriaWhatsapRequest pedidoDisparoCanalMensageriaWhatsapRequest) {

        List<WhatsappVO.Destinatario> destinatarios = new ArrayList<>();
        List<WhatsappVO.Removidos> higienizadas = new ArrayList<>();
        SistelcomDTO sistelcomDTO = new SistelcomDTO();

        try (InputStream is = excel.getInputStream();
             XSSFWorkbook workbook = new XSSFWorkbook(is)) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            boolean isHeader = true;

            for (Row row : sheet) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                Cell telefoneCompletoCell = row.getCell(0);
                Cell nomeCompletoCell = row.getCell(1);
                Cell mensagemWhatsappCell = row.getCell(2);

                String telefoneCompleto = getCellValueAsString(telefoneCompletoCell).trim();
                String nomeCompleto = getCellValueAsString(nomeCompletoCell).trim();
                String mensagemWhatsapp = getCellValueAsString(mensagemWhatsappCell).trim();

                if (telefoneCompleto.length() == 13) {
                    WhatsappVO.Destinatario novoDestinatario = new WhatsappVO.Destinatario();
                    novoDestinatario.setNumero(telefoneCompleto);
                    novoDestinatario.setNomeCompleto(nomeCompleto);

                    if(!mensagemWhatsapp.isEmpty() && pedidoDisparoCanalMensageriaWhatsapRequest.getConteudoMensagemUnico() == true) {
                        novoDestinatario.setMensagem(mensagemWhatsapp);
                        destinatarios.add(novoDestinatario);
                    }else if(pedidoDisparoCanalMensageriaWhatsapRequest.getConteudoMensagem() != null){
                        novoDestinatario.setMensagem(pedidoDisparoCanalMensageriaWhatsapRequest.getConteudoMensagem());
                        destinatarios.add(novoDestinatario);
                    }else{
                        WhatsappVO.Removidos destinatarioHigienizado = new WhatsappVO.Removidos();
                        destinatarioHigienizado.setNumero(Optional.ofNullable(telefoneCompleto).orElse("***"));
                        destinatarioHigienizado.setNomeCompleto(Optional.ofNullable(nomeCompleto).orElse("***"));
                        destinatarioHigienizado.setMensagem(Optional.ofNullable(mensagemWhatsapp).orElse("***"));
                        higienizadas.add(destinatarioHigienizado);
                    }

                }else{
                    WhatsappVO.Removidos destinatarioHigienizado = new WhatsappVO.Removidos();
                    destinatarioHigienizado.setNumero(Optional.ofNullable(telefoneCompleto).orElse("***"));
                    destinatarioHigienizado.setNomeCompleto(Optional.ofNullable(nomeCompleto).orElse("***"));
                    destinatarioHigienizado.setMensagem(Optional.ofNullable(mensagemWhatsapp).orElse("***"));
                    higienizadas.add(destinatarioHigienizado);
                }

            }

            sistelcomDTO.setInvalidos(Optional.ofNullable(((long) higienizadas.size())).orElse(0L));
            sistelcomDTO.setValidos(Optional.ofNullable(((long) destinatarios.size())).orElse(0L));
            sistelcomDTO.setTotal( Optional.ofNullable(((long) destinatarios.size())).orElse(0L) + Optional.ofNullable(((long) higienizadas.size())).orElse(0L) );

            sistelcomDTO.setWhatsappDestinatarios(destinatarios);
            sistelcomDTO.setWhatsappRemovidos(higienizadas);

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
