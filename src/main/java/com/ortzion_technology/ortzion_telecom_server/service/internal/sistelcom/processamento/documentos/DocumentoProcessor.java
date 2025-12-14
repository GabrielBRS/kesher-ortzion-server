package com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.documentos;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.PedidoDisparoCanalMensageriaSMSRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.SistelcomDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DocumentoProcessor {
    SistelcomDTO processamento(MultipartFile arquivo, PedidoDisparoCanalMensageriaSMSRequest pedido) throws IOException;
    String getTipoSuportado();
}
