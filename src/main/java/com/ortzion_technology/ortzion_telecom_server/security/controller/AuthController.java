package com.ortzion_technology.ortzion_telecom_server.security.controller;

import com.ortzion_technology.ortzion_telecom_server.configuration.ambiente.ConstanteVariavelAmbiente;
import com.ortzion_technology.ortzion_telecom_server.security.dto.LoginRequest;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import com.ortzion_technology.ortzion_telecom_server.security.dto.Verificacao2FARequest;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.security.repository.AcessoUsuarioRepository;
import com.ortzion_technology.ortzion_telecom_server.security.service.AuthService;
import com.ortzion_technology.ortzion_telecom_server.security.io.email.EnvioEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private ConstanteVariavelAmbiente constante;

    private final AuthService authService;
    private final AcessoUsuarioRepository acessoUsuarioRepository;
    private final EnvioEmailService envioEmailService;

    public AuthController(AuthService authService, AcessoUsuarioRepository acessoUsuarioRepository, EnvioEmailService envioEmailService) {
        this.authService = authService;
        this.acessoUsuarioRepository = acessoUsuarioRepository;
        this.envioEmailService = envioEmailService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return authService.iniciarLogin(loginRequest.getDocumento(), loginRequest.getPassword());
    }

    @PostMapping("/verify-2fa")
    public ResponseEntity<?> verificarCodigo2FA(@RequestBody Verificacao2FARequest request) {
        return authService.verificarCodigo2FA(request.getDocumento(), request.getCodigo2FA());
    }

    @PostMapping("/carregar-usuario")
    public ResponseEntity<?> carregarUsuario(@RequestBody LoginRequest loginRequest) {

        Optional<AcessoUsuario> usuario = acessoUsuarioRepository.findByDocumentoUsuario(loginRequest.getDocumento());

        if (usuario.isEmpty()) {
            return ResponseEntity.status(404).body("Usuário não encontrado.");
        }

        return ResponseEntity.ok(usuario.get());
    }

    @PostMapping("/select-context")
    @PreAuthorize("hasRole('PRE_SELECTION')")
    public ResponseEntity<?> selectContext(@RequestBody MulticontaRequestDTO request) {
        return authService.selectContext(request);
    }

    @PostMapping("/solicitar-redefinicao-senha")
    public ResponseEntity<?> solicitarRedefinicaoSenha(
            @RequestBody Map<String, String> payload
    ) throws IOException {

        String documento = payload.get("documento");
        Optional<AcessoUsuario> acessoUsuario = this.acessoUsuarioRepository.findByDocumentoUsuario(documento);

        if (acessoUsuario.isPresent()) {
            authService.solicitarRedefinicaoSenha(acessoUsuario.get());
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/validar-token-redefinicao")
    public ResponseEntity<?> validarToken(@RequestBody Map<String, String> payload) {
        try {
            String token = payload.get("token");
            authService.validarTokenRedefinicao(token);
            return ResponseEntity.ok().build();
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/redefinir-senha-com-token")
    public ResponseEntity<?> redefinirSenha(@RequestBody Map<String, String> payload) {
        try {
            String token = payload.get("token");
            String novaSenha = payload.get("novaSenha");

            authService.redefinirSenhaComToken(token, novaSenha);
            return ResponseEntity.ok(Map.of("message", "Senha redefinida com sucesso."));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar solicitação.");
        }
    }

}
