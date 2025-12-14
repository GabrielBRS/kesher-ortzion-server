package com.ortzion_technology.ortzion_telecom_server.security.criptografia;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TokenEncryptorDecryptorService {

    public String gerarSenhaAleatoria() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

}
