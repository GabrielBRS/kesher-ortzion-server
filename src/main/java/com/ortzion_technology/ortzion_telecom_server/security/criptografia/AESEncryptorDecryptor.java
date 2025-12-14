package com.ortzion_technology.ortzion_telecom_server.security.criptografia;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AESEncryptorDecryptor {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding"; // Usando GCM e NoPadding
    private static final int KEY_SIZE_BITS = 256; // 128, 192 ou 256 bits para AES
    private static final int GCM_IV_LENGTH = 12; // 12 bytes é o recomendado para GCM
    private static final int GCM_TAG_LENGTH = 16; // 16 bytes (128 bits) é o recomendado para GCM

    // --- Geração e Gerenciamento da Chave Secreta ---

    /**
     * Gera uma nova chave AES aleatória.
     * Esta chave deve ser armazenada com segurança.
     * @return SecretKey AES
     * @throws NoSuchAlgorithmException se o algoritmo AES não estiver disponível
     */
    public static SecretKey generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(KEY_SIZE_BITS, new SecureRandom()); // SecureRandom para aleatoriedade criptograficamente forte
        return keyGen.generateKey();
    }

    /**
     * Converte uma chave secreta (SecretKey) em uma String Base64 para armazenamento.
     * @param secretKey A chave secreta a ser convertida.
     * @return String Base64 da chave secreta.
     */
    public static String keyToString(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * Converte uma String Base64 de volta para uma chave secreta (SecretKey).
     * @param encodedKey String Base64 da chave secreta.
     * @return SecretKey AES.
     */
    public static SecretKey stringToKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
    }

    // --- Criptografia ---

    /**
     * Criptografa uma mensagem usando AES/GCM.
     * Retorna um objeto contendo o IV e o texto cifrado, ambos em Base64.
     * O IV é necessário para a descriptografia e deve ser armazenado junto com o texto cifrado, mas não precisa ser secreto.
     * @param message A mensagem em texto claro a ser criptografada.
     * @param secretKey A chave secreta AES para criptografia.
     * @return Um objeto EncryptedData contendo o IV e o texto cifrado em Base64.
     * @throws Exception em caso de erro na criptografia.
     */
    public static EncryptedData encrypt(String message, SecretKey secretKey) throws Exception {
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv); // Gerar um IV novo e aleatório para cada criptografia

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv); // Tag length em bits
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

        byte[] encryptedBytes = cipher.doFinal(message.getBytes("UTF-8"));

        // Retornar IV e texto cifrado (e tag GCM, que está embutida no encryptedBytes)
        return new EncryptedData(Base64.getEncoder().encodeToString(iv),
                Base64.getEncoder().encodeToString(encryptedBytes));
    }

    // --- Descriptografia ---

    /**
     * Descriptografa uma mensagem usando AES/GCM.
     * @param encryptedData Objeto EncryptedData contendo o IV e o texto cifrado em Base64.
     * @param secretKey A chave secreta AES para descriptografia.
     * @return A mensagem descriptografada em texto claro.
     * @throws Exception em caso de erro na descriptografia (incluindo falha na autenticação GCM).
     */
    public static String decrypt(EncryptedData encryptedData, SecretKey secretKey) throws Exception {
        byte[] iv = Base64.getDecoder().decode(encryptedData.getIv());
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData.getCiphertext());

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, "UTF-8");
    }

    // Classe auxiliar para encapsular IV e ciphertext para armazenamento/transporte
    public static class EncryptedData {
        private String iv;
        private String ciphertext;

        public EncryptedData(String iv, String ciphertext) {
            this.iv = iv;
            this.ciphertext = ciphertext;
        }

        public String getIv() {
            return iv;
        }

        public String getCiphertext() {
            return ciphertext;
        }
    }

    public static void main(String[] args) throws Exception {
        // 1. Gerar a chave secreta (faça isso UMA VEZ e armazene com segurança!)
        SecretKey secretKey = generateAESKey();
        String encodedKey = keyToString(secretKey);
        System.out.println("Chave Secreta (Base64): " + encodedKey);

        // 2. Simular carregamento da chave (em produção, você carregaria de um local seguro)
        SecretKey loadedKey = stringToKey(encodedKey);

        // 3. Mensagem para criptografar
        String originalMessage = "Esta é a minha mensagem secreta para o banco de dados.";
        System.out.println("Mensagem Original: " + originalMessage);

        // 4. Criptografar a mensagem
        EncryptedData encryptedResult = encrypt(originalMessage, loadedKey);
        System.out.println("IV (Base64): " + encryptedResult.getIv());
        System.out.println("Mensagem Criptografada (Base64): " + encryptedResult.getCiphertext());

        // 5. Simular salvamento no banco:
        // Você salvaria encryptedResult.getIv() em uma coluna e encryptedResult.getCiphertext() em outra.
        // Por exemplo:
        // ALTER TABLE AGENDAMENTO_MENSAGEM ADD COLUMN iv VARCHAR(255);
        // ALTER TABLE AGENDAMENTO_MENSAGEM ADD COLUMN conteudo_mensagem_criptografado TEXT;
        // INSERT INTO AGENDAMENTO_MENSAGEM (..., iv, conteudo_mensagem_criptografado) VALUES (..., encryptedResult.getIv(), encryptedResult.getCiphertext());

        // 6. Descriptografar a mensagem (ao recuperar do banco)
        // Suponha que você recuperou o IV e o ciphertext do banco
        String recoveredIv = encryptedResult.getIv();
        String recoveredCiphertext = encryptedResult.getCiphertext();
        EncryptedData retrievedData = new EncryptedData(recoveredIv, recoveredCiphertext);

        String decryptedMessage = decrypt(retrievedData, loadedKey);
        System.out.println("Mensagem Descriptografada: " + decryptedMessage);

        // Teste com chave errada (deve falhar a descriptografia)
        // SecretKey wrongKey = generateAESKey(); // Uma nova chave aleatória
        // try {
        //     decrypt(retrievedData, wrongKey);
        // } catch (Exception e) {
        //     System.out.println("\nTentativa de descriptografar com chave errada (esperado falha): " + e.getMessage());
        // }
    }
}
