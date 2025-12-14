package com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.documentos;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.PedidoDisparoCanalMensageriaEmailRequest;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.PedidoDisparoCanalMensageriaSMSRequest;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.PedidoDisparoCanalMensageriaWhatsapRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.SistelcomDTO;
import com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.documentos.email.DocumentoExcelEmailService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.documentos.email.DocumentoHtmlEmailService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.documentos.whatsapt.DocumentoExcelWhatsapService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class TransformadorArquivosMetadosService {

    private final DocumentoExcelEmailService documentoExcelEmail;
    private final DocumentoExcelWhatsapService  documentoExcelWhatsapService;
    private final DocumentoHtmlEmailService documentoHtmlEmailService;
    private final DocumentoProcessorFactory smsProcessorFactory;

    public TransformadorArquivosMetadosService(DocumentoExcelEmailService documentoExcelEmail, DocumentoExcelWhatsapService documentoExcelWhatsapService, DocumentoHtmlEmailService documentoHtmlEmailService, DocumentoProcessorFactory smsProcessorFactory) {
        this.documentoExcelEmail = documentoExcelEmail;
        this.documentoExcelWhatsapService = documentoExcelWhatsapService;
        this.documentoHtmlEmailService = documentoHtmlEmailService;
        this.smsProcessorFactory = smsProcessorFactory;
    }

    public SistelcomDTO transformarArquivoParaEnviarSMS(MultipartFile arquivo, PedidoDisparoCanalMensageriaSMSRequest pedido) throws IOException {
        String extensao = FilenameUtils.getExtension(arquivo.getOriginalFilename());

        if (extensao == null || extensao.isEmpty()) {
            throw new IllegalArgumentException("Arquivo com extensão inválida ou inexistente.");
        }

        DocumentoProcessor processor = smsProcessorFactory.getProcessor(extensao);
        return processor.processamento(arquivo, pedido);
    }

    public SistelcomDTO transformarArquivoParaEnviarEmail(MultipartFile excel, PedidoDisparoCanalMensageriaEmailRequest pedidoDisparoCanalMensageriaEmailRequest, MultipartFile modeloHtmlEmail) {
        if (!excel.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
            throw new IllegalArgumentException("Arquivo de contatos de e-mail deve ser .xlsx");
        }
        if (modeloHtmlEmail != null && !modeloHtmlEmail.getOriginalFilename().toLowerCase().endsWith(".html") && !modeloHtmlEmail.getOriginalFilename().toLowerCase().endsWith(".htm")) {
            throw new IllegalArgumentException("O arquivo de modelo de e-mail deve ser .html ou .htm.");
        }
        return documentoExcelEmail.processamento(excel, pedidoDisparoCanalMensageriaEmailRequest, modeloHtmlEmail);
    }

    public SistelcomDTO transformarArquivoParaEnviarWhatsapp(MultipartFile excel, PedidoDisparoCanalMensageriaWhatsapRequest pedidoDisparoCanalMensageriaWhatsapRequest) {
        if (!excel.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
            throw new IllegalArgumentException("Arquivo de contatos de WhatsApp deve ser .xlsx");
        }
        return documentoExcelWhatsapService.processamento(excel, pedidoDisparoCanalMensageriaWhatsapRequest);
    }

}