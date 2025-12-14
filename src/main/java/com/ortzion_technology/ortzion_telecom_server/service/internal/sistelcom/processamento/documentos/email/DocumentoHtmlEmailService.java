package com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.documentos.email;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import com.ortzion_technology.ortzion_telecom_server.entity.vo.EmailVO;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class DocumentoHtmlEmailService {

    public EmailVO.Destinatario processamentoHtml(MultipartFile htmlFile, EmailVO.Destinatario destinatario) {
        EmailVO.Destinatario mensagem = new EmailVO.Destinatario(); // You're creating a new destinatario here

        if (htmlFile.isEmpty()) {
            throw new IllegalArgumentException("O arquivo HTML está vazio.");
        }
        if (!htmlFile.getOriginalFilename().toLowerCase().endsWith(".html") && !htmlFile.getOriginalFilename().toLowerCase().endsWith(".htm")) {
            throw new IllegalArgumentException("Formato de arquivo não suportado. Por favor, envie um arquivo .html ou .htm.");
        }

        try (InputStream inputStream = htmlFile.getInputStream()) {
            String conteudoHtml = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

            conteudoHtml = conteudoHtml.replace("<span>#nome</span>", "<span>" + destinatario.getNomeDestinatario() + "</span>");
            conteudoHtml = conteudoHtml.replace("<span>#dataAtual</span>", "<span>" + LocalDate.now() + "</span>");

            mensagem.setBodyEmail(conteudoHtml.trim()); // Set the processed HTML as the bodyEmail of the NEW destinatario
            mensagem.setEmailDestinatario(destinatario.getEmailDestinatario()); // You might want to copy other fields too
            mensagem.setNomeDestinatario(destinatario.getNomeDestinatario());
            mensagem.setTituloEmail(destinatario.getTituloEmail());


            return mensagem;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar o arquivo HTML: " + e.getMessage(), e);
        }
    }

}

