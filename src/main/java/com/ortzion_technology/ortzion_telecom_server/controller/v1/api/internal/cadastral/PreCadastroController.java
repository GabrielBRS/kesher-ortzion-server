package com.ortzion_technology.ortzion_telecom_server.controller.v1.api.internal.cadastral;

import com.ortzion_technology.ortzion_telecom_server.configuration.ambiente.ConstanteVariavelAmbiente;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.AtualizacaoPreCadastroDTO;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.PreCadastroRequest;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.response.internal.PreCadastroResponse;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.PreCadastro;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.security.service.SecurityService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.cadastral.PreCadastroService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.cloud.CloudFileStorageService;
import com.ortzion_technology.ortzion_telecom_server.security.io.email.EnvioEmailService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/pre-cadastro")
public class PreCadastroController {

    @Autowired
    private ConstanteVariavelAmbiente constante;

    private final PreCadastroService preCadastroService;

    private final CloudFileStorageService cloudFileStorageService;

    private final EnvioEmailService envioEmailService;

    private final SecurityService securityService;

    public PreCadastroController(PreCadastroService preCadastroService, CloudFileStorageService cloudFileStorageService, EnvioEmailService envioEmailService, SecurityService securityService) {
        this.preCadastroService = preCadastroService;
        this.cloudFileStorageService = cloudFileStorageService;
        this.envioEmailService = envioEmailService;
        this.securityService = securityService;
    }

    @RequestMapping(
            value = "/pegar",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> pegarPrecadastro() {

        AcessoUsuario usuario = securityService.getUsuarioLogado();

        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

//        securityService.verificarAcessoPermissoesMulticonta(usuario, new MulticontaRequestDTO(idSubjectus, tipoPessoa, idSubjectus, 0));

        PreCadastro preCadastro = preCadastroService.pegarPrecadastro(usuario);

        PreCadastroResponse preCadastroResponse = new PreCadastroResponse(preCadastro);

        return ResponseEntity.ok().body(preCadastroResponse);
    }

    @RequestMapping(
            value = "/pegar/confirmacao-cadastro/{documento}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> pegarPrecadastroConfirmacao(
            @PathVariable("documento") String documento
    ) {

        PreCadastro preCadastro = preCadastroService.pegarPrecadastro(documento);

        PreCadastroResponse preCadastroResponse = new PreCadastroResponse(preCadastro);

        return ResponseEntity.ok().body(preCadastroResponse);
    }

    @RequestMapping(
            value = "/cadastrar",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> cadastrar(
            @RequestPart("preCadastro") PreCadastroRequest preCadastroRequest,
            @RequestPart("documento") MultipartFile file,
            @RequestPart(value = "documentoRepresentanteLegal", required = false) MultipartFile fileRepresentanteLegal,
            @RequestPart(value = "documentoProcuracaoRepresentanteLegal", required = false) MultipartFile fileProcuracaoRepresentanteLegal,
            @RequestPart(value = "documentoContratoSocialRepresentanteLegal", required = false) MultipartFile fileContratoSocialRepresentanteLegal) throws IOException {

        if(preCadastroRequest == null) {
            return ResponseEntity.badRequest().build();
        }

        PreCadastro preCadastro = preCadastroService.cadastrar(preCadastroRequest);

        cloudFileStorageService.storeFilePreCadastro(
                file,
                fileRepresentanteLegal,
                fileProcuracaoRepresentanteLegal,
                fileContratoSocialRepresentanteLegal,
                preCadastro
        );

        envioEmailService.enviarEmailConfirmacaoCadastro(preCadastro);

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(preCadastro);
    }

    @RequestMapping(
            value = "/email-confirmado",
            method = RequestMethod.GET
    )
    public void emailConfirmado(
            @RequestParam(name = "email") String email,
            HttpServletResponse response) throws IOException {

        if (email == null) {
            response.sendRedirect(constante.getHttpRedirectSite() + "/erro?msg=email-invalido");
            return;
        }

        preCadastroService.pegarPreCadastroPorEmail(email);

        response.sendRedirect(constante.getHttpRedirectSite() + "?primeiroAcesso=true");
    }

    @RequestMapping(
            value = "/atualizar",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> atualizarPreCadastro(
            @RequestPart("preCadastro") PreCadastroRequest preCadastroRequest,
            @RequestPart(value = "peticao", required = false) AtualizacaoPreCadastroDTO peticao,
            @RequestPart(value = "destinatarioFile", required = false) MultipartFile destinatarioFile) throws IOException {

        Optional<PreCadastro> preCadastro = preCadastroService.atualizar(preCadastroRequest, peticao);

        if (preCadastro.isPresent()) {
            envioEmailService.enviarEmailAtualizacaoCadastro(preCadastro.get(), peticao, destinatarioFile);
            return ResponseEntity.ok(preCadastro.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("PreCadastro não encontrado.");
        }

    }

}
