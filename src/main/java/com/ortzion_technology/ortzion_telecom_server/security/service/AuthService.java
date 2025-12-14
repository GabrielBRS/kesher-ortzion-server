package com.ortzion_technology.ortzion_telecom_server.security.service;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Pessoa;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.PreCadastro;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.cadastral.PessoaRepository;
import com.ortzion_technology.ortzion_telecom_server.security.dto.LoginResponseDTO;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoGrupoMulticonta;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.security.entity.Multiconta;
import com.ortzion_technology.ortzion_telecom_server.security.repository.AcessoGrupoRepository;
import com.ortzion_technology.ortzion_telecom_server.security.repository.AcessoUsuarioRepository;
import com.ortzion_technology.ortzion_telecom_server.security.TwoA.JwtUtil;
import com.ortzion_technology.ortzion_telecom_server.security.repository.MulticontaRepository;
import com.ortzion_technology.ortzion_telecom_server.security.repository.custom.MulticontaRepositoryCustomImpl;
import com.ortzion_technology.ortzion_telecom_server.security.vo.MulticontaVO;
import com.ortzion_technology.ortzion_telecom_server.service.internal.cadastral.PreCadastroService;
import com.ortzion_technology.ortzion_telecom_server.security.io.email.EnvioEmailService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.security.SecureRandom; // Importação de SecureRandom
import java.util.*;
import java.util.stream.Collectors;
import java.util.Map;

@Service
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final AcessoUsuarioRepository acessoUsuarioRepository;
    private final EnvioEmailService envioEmailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PreCadastroService preCadastroService;
    private final AcessoGrupoRepository acessoGrupoRepository;
    private final PessoaRepository pessoaRepository;
    private final MulticontaRepository multicontaRepository;
    private final MulticontaRepositoryCustomImpl multicontaRepositoryCustomImpl;
    private final GoogleAuthenticator gAuth;

    public AuthService(AuthenticationManager authenticationManager,
                       UserDetailsService userDetailsService,
                       JwtUtil jwtUtil,
                       AcessoUsuarioRepository acessoUsuarioRepository,
                       EnvioEmailService envioEmailService,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       PreCadastroService preCadastroService,
                       AcessoGrupoRepository acessoGrupoRepository,
                       PessoaRepository pessoaRepository,
                       MulticontaRepository multicontaRepository, MulticontaRepositoryCustomImpl multicontaRepositoryCustomImpl,
                       GoogleAuthenticator gAuth) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.acessoUsuarioRepository = acessoUsuarioRepository;
        this.envioEmailService = envioEmailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.preCadastroService = preCadastroService;
        this.acessoGrupoRepository = acessoGrupoRepository;
        this.pessoaRepository = pessoaRepository;
        this.multicontaRepository = multicontaRepository;
        this.multicontaRepositoryCustomImpl = multicontaRepositoryCustomImpl;
        this.gAuth = gAuth;
    }

    @Transactional
    public ResponseEntity<?> iniciarLogin(String documento, String password) {
        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(documento, password));

            AcessoUsuario usuario = acessoUsuarioRepository.findByDocumentoUsuario(documento)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado após autenticação."));

            if (usuario.isMfaEnabled()) {
                return ResponseEntity.ok(Map.of(
                        "statusUsuario", usuario.getStatusUsuario(),
                        "mfaType", "TOTP"
                ));

            } else if (usuario.isCodigo2FAEnabled()) {

                String codigo2FA = gerarCodigo2FA();
                usuario.setCodigo2FA(bCryptPasswordEncoder.encode(codigo2FA));
                usuario.setCodigo2FAExpiracao(LocalDateTime.now().plusMinutes(5));
                acessoUsuarioRepository.save(usuario);

                envioEmailService.enviarTokenAcesso(usuario, codigo2FA);

                return ResponseEntity.ok(Map.of(
                        "statusUsuario", usuario.getStatusUsuario(),
                        "mfaType", "EMAIL"
                ));

            } else {
                if(!Objects.equals(usuario.getStatusUsuario(),1)){
                    return ResponseEntity.ok(Map.of(
                            "statusUsuario", usuario.getStatusUsuario(),
                            "tipoPessoa", 1,
                            "idSubjectus", usuario.getIdUsuario(),
                            "documento", usuario.getDocumentoUsuario(),
                            "mfaType", "NONE"
                    ));
                }else{
                    return ResponseEntity.ok(Map.of(
                            "statusUsuario", usuario.getStatusUsuario(),
                            "mfaType", "NONE"
                    ));
                }
            }

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Documento ou senha inválidos.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro interno no servidor: " + e.getMessage());
        }
    }


    public ResponseEntity<?> verificarCodigo2FA(String documento, String codigo2FA) {
        AcessoUsuario usuario = acessoUsuarioRepository.findByDocumentoUsuario(documento)
                .orElseThrow(() -> new BadCredentialsException("Usuário não encontrado."));

        boolean isCodeValid = false;

        if (!usuario.isCodigo2FAEnabled() && !usuario.isMfaEnabled()) {
            isCodeValid = true;
        }

        else if (usuario.isMfaEnabled()) {
            try {
                int verificationCode = Integer.parseInt(codigo2FA);
                isCodeValid = gAuth.authorize(usuario.getMfaSecret(), verificationCode);
            } catch (NumberFormatException e) {
                isCodeValid = false;
            }
        }

        else if (usuario.isCodigo2FAEnabled()) {
            isCodeValid = usuario.getCodigo2FA() != null &&
                    usuario.getCodigo2FAExpiracao() != null &&
                    usuario.getCodigo2FAExpiracao().isAfter(LocalDateTime.now()) &&
                    bCryptPasswordEncoder.matches(codigo2FA, usuario.getCodigo2FA());

            if (isCodeValid) {
                usuario.setCodigo2FA(null);
                usuario.setCodigo2FAExpiracao(null);
                acessoUsuarioRepository.save(usuario);
            }
        }

        if (!isCodeValid) {
            return ResponseEntity.status(401).body("Código de verificação inválido ou expirado.");
        }

        List<MulticontaVO> contasDisponiveis = this.multicontaRepositoryCustomImpl.pegarTodasMulticontasPorIdUsuario(usuario.getIdUsuario());

        UserDetails userDetails = new User(usuario.getUsername(), "", usuario.getAuthorities());
        String jwtToken = jwtUtil.generateToken(userDetails);

        LoginResponseDTO response = new LoginResponseDTO(jwtToken, contasDisponiveis);

        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<?> selectContext(MulticontaRequestDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String documento = authentication.getName();

        AcessoUsuario usuario = acessoUsuarioRepository.findByDocumentoUsuario(documento)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário do token não encontrado."));

        if (!usuario.getIdUsuario().equals(request.getIdUsuario())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("A solicitação não corresponde ao usuário autenticado.");
        }

        Collection<GrantedAuthority> authorities;

        if (request.getTipoPessoa() == 0 && request.getIdSubjectus().equals(usuario.getIdUsuario())) {
            authorities = carregarAuthoritiesDeAdmin();
        } else {
            Multiconta multicontaSelecionada = usuario.getMulticontas().stream()
                    .filter(mc -> mc.getMulticontaId() != null &&
                            Objects.equals(mc.getMulticontaId().getTipoPessoa(), request.getTipoPessoa()) &&
                            Objects.equals(mc.getMulticontaId().getIdSubjectus(), request.getIdSubjectus()) &&
                            Objects.equals(mc.getMulticontaId().getIdDepartamento(), request.getIdDepartamento()))
                    .findFirst()
                    .orElseThrow(() -> new BadCredentialsException("Multiconta inválida ou não pertence ao usuário."));

            authorities = Optional.ofNullable(multicontaSelecionada.getAcessoGrupoMulticonta())
                    .orElse(Collections.emptySet())
                    .stream()
                    .filter(Objects::nonNull)
                    .map(AcessoGrupoMulticonta::getAcessoGrupo)
                    .filter(Objects::nonNull)
                    .flatMap(acessoGrupo -> Optional.ofNullable(acessoGrupo.getAcessoGrupoFuncionalidades())
                            .orElse(Collections.emptySet())
                            .stream())
                    .filter(Objects::nonNull)
                    .map(func -> new SimpleGrantedAuthority(func.getAcessoFuncionalidade().getRole().getNome()))
                    .collect(Collectors.toSet());
        }

        if (authorities.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("A conta selecionada não possui permissões de acesso.");
        }

        UserDetails userDetailsComContexto = new User(usuario.getUsername(), "", authorities);
        String tokenDeAcesso = jwtUtil.generateToken(userDetailsComContexto);

        return ResponseEntity.ok(Map.of("token", tokenDeAcesso));
    }

    public void solicitarRedefinicaoSenha(AcessoUsuario acessoUsuario) throws IOException {

        Optional<Pessoa> pessoaOptional = pessoaRepository.findByDocumento(acessoUsuario.getDocumentoUsuario());
        if (pessoaOptional.isEmpty()) {
            throw new IllegalStateException("Não foi possível encontrar os dados cadastrais...");
        }
        Pessoa pessoa = pessoaOptional.get();

        String token = UUID.randomUUID().toString();

        acessoUsuario.setTokenRedefinicao(token);
        acessoUsuario.setTokenRedefinicaoExpiraEm(LocalDateTime.now().plusHours(1));
        acessoUsuarioRepository.save(acessoUsuario);

        envioEmailService.enviarEmailRedefinirSenha(pessoa, acessoUsuario, token);
    }

    @Transactional
    public AcessoUsuario validarTokenRedefinicao(String token) {
        AcessoUsuario usuario = acessoUsuarioRepository.findByTokenRedefinicao(token)
                .orElseThrow(() -> new BadCredentialsException("Token de redefinição inválido."));

        if (usuario.getTokenRedefinicaoExpiraEm().isBefore(LocalDateTime.now())) {
            throw new BadCredentialsException("Token de redefinição expirado.");
        }

        return usuario;
    }

    @Transactional
    public void redefinirSenhaComToken(String token, String novaSenha) {

        AcessoUsuario usuario = validarTokenRedefinicao(token);

        usuario.setSenhaUsuario(bCryptPasswordEncoder.encode(novaSenha));

        usuario.setTokenRedefinicao(null);
        usuario.setTokenRedefinicaoExpiraEm(null);

        acessoUsuarioRepository.save(usuario);
    }


    private Collection<GrantedAuthority> carregarAuthoritiesDeAdmin() {
        return acessoGrupoRepository.findAll().stream()
                .flatMap(grupo -> grupo.getAcessoGrupoFuncionalidades().stream())
                .map(func -> new SimpleGrantedAuthority(func.getAcessoFuncionalidade().getRole().getNome()))
                .collect(Collectors.toSet());
    }

    private String gerarCodigo2FA() {
        String chars = "0123456789";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(secureRandom.nextInt(chars.length())));
        }
        return sb.toString();
    }

}