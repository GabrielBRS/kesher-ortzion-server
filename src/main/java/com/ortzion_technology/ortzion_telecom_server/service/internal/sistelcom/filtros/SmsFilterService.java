package com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.filtros;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class SmsFilterService {

    private static final Logger logger = LoggerFactory.getLogger(SmsFilterService.class);

    private static final Pattern DIACRITICS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    @Value("${sms.filter.blocklist.path}")
    private Resource blocklistResource;

    private final Set<String> blockedWords = new HashSet<>();
    private final Set<String> blockedPhrases = new HashSet<>();

    @PostConstruct
    public void initializeBlocklists() {
        if (blocklistResource == null || !blocklistResource.exists()) {
            logger.warn("Arquivo de blocklist de SMS não encontrado ou não configurado. O filtro não estará ativo.");
            return;
        }

        logger.info("Carregando lista de bloqueio de SMS do arquivo: {}", blocklistResource.getFilename());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(blocklistResource.getInputStream(), StandardCharsets.UTF_8))) {
            reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .forEach(line -> {
                        String normalizedLine = normalizeString(line);
                        if (normalizedLine.contains(" ")) {
                            blockedPhrases.add(normalizedLine);
                        } else {
                            blockedWords.add(normalizedLine);
                        }
                    });
        } catch (IOException e) {
            logger.error("Falha ao ler o arquivo de blocklist de SMS.", e);
            throw new RuntimeException("Não foi possível carregar a blocklist de SMS.", e);
        }

        logger.info("Blocklist de SMS carregada: {} palavras e {} frases.", blockedWords.size(), blockedPhrases.size());
    }

    public boolean isBlocked(String message) {
        if (message == null || message.trim().isEmpty()) {
            return false;
        }

        String normalizedMessage = normalizeString(message);

        for (String phrase : blockedPhrases) {
            if (normalizedMessage.contains(phrase)) {
                logger.debug("SMS bloqueado pela frase: '{}'", phrase);
                return true;
            }
        }

        String[] wordsInMessage = normalizedMessage.split("\\s+");
        for (String word : wordsInMessage) {
            if (blockedWords.contains(word)) {
                logger.debug("SMS bloqueado pela palavra: '{}'", word);
                return true;
            }
        }

        return false;
    }

    private String normalizeString(String input) {
        if (input == null) {
            return "";
        }

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return DIACRITICS_PATTERN.matcher(normalized)
                .replaceAll("")
                .toLowerCase()
                .replaceAll("[.,!?;:]", "");
    }

}