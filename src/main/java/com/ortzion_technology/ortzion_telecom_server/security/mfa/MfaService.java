package com.ortzion_technology.ortzion_telecom_server.security.mfa;

import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.security.entity.Multiconta;
import com.ortzion_technology.ortzion_telecom_server.security.repository.AcessoUsuarioRepository;
import com.ortzion_technology.ortzion_telecom_server.security.repository.MulticontaRepository;
import com.ortzion_technology.ortzion_telecom_server.security.service.MulticontaService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MfaService {

    private final AcessoUsuarioRepository userRepository;
    private final GoogleAuthenticator gAuth;
    private final QrCodeService qrCodeService;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom = new SecureRandom();
    private final MulticontaService multicontaService;

    // Use constructor injection
    public MfaService(AcessoUsuarioRepository userRepository,
                      GoogleAuthenticator gAuth,
                      QrCodeService qrCodeService,
                      PasswordEncoder passwordEncoder, MulticontaService multicontaService) {
        this.userRepository = userRepository;
        this.gAuth = gAuth;
        this.qrCodeService = qrCodeService;
        this.passwordEncoder = passwordEncoder;
        this.multicontaService = multicontaService;
    }


    public MfaSetupDto setupMfa(String username) {

        AcessoUsuario user = userRepository.findByDocumentoUsuarioWithMulticontasGraph(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Multiconta multiconta = this.multicontaService.buscarMulticontaPorNomeUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        GoogleAuthenticatorKey key = gAuth.createCredentials();
        String secretKey = key.getKey();

        user.setMfaSecret(secretKey);
        userRepository.save(user);

        String otpAuthUrl = GoogleAuthenticatorQRGenerator.getOtpAuthURL(
                "Kesher Ortzion",
                multiconta.getEmail(),
                key
        );

        String qrCodeBase64 = qrCodeService.generateQrCodeBase64(otpAuthUrl, 250, 250);

        return new MfaSetupDto(secretKey, qrCodeBase64);
    }

    public MfaRecoveryCodesDto confirmMfa(String username, String code) {
        AcessoUsuario user = userRepository.findByDocumentoUsuarioWithMulticontasGraph(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        int verificationCode;
        try {
            verificationCode = Integer.parseInt(code);
        } catch (NumberFormatException e) {
            return new MfaRecoveryCodesDto(false, null);
        }

        if (gAuth.authorize(user.getMfaSecret(), verificationCode)) {

            user.setMfaEnabled(true);

            List<String> plainTextRecoveryCodes = generateRecoveryCodes(10);
            Set<String> hashedRecoveryCodes = hashRecoveryCodes(plainTextRecoveryCodes);

            user.setRecoveryCodes(hashedRecoveryCodes);
            userRepository.save(user);

            return new MfaRecoveryCodesDto(true, plainTextRecoveryCodes);
        } else {

            return new MfaRecoveryCodesDto(false, null);
        }
    }

    private List<String> generateRecoveryCodes(int count) {
        List<String> codes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String part1 = String.format("%05d", secureRandom.nextInt(100000));
            String part2 = String.format("%05d", secureRandom.nextInt(100000));
            codes.add(part1 + "-" + part2);
        }
        return codes;
    }

    private Set<String> hashRecoveryCodes(List<String> plainTextCodes) {
        return plainTextCodes.stream()
                .map(passwordEncoder::encode)
                .collect(Collectors.toSet());
    }

}