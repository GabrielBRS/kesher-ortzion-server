package com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.documentos;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DocumentoProcessorFactory {

    private final Map<String, DocumentoProcessor> processors;

    public DocumentoProcessorFactory(List<DocumentoProcessor> processorList) {
        this.processors = processorList.stream()
                .collect(Collectors.toMap(DocumentoProcessor::getTipoSuportado, Function.identity()));
    }

    public DocumentoProcessor getProcessor(String tipoArquivo) {
        DocumentoProcessor processor = processors.get(tipoArquivo.toLowerCase());
        if (processor == null) {
            throw new IllegalArgumentException("Tipo de arquivo não suportado: " + tipoArquivo);
        }
        return processor;
    }
}
