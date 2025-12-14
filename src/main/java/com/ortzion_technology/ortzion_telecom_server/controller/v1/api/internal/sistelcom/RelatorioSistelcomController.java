package com.ortzion_technology.ortzion_telecom_server.controller.v1.api.internal.sistelcom;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.RelatorioSistelcomRequest;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.security.service.SecurityService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.exception.RelatorioGenerationException;
import com.ortzion_technology.ortzion_telecom_server.service.internal.exception.RelatorioVazioException;
import com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.relatorio.RelatorioSistelcomService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/relatorio")
public class RelatorioSistelcomController {

    private final RelatorioSistelcomService relatorioSistelcomService;
    private final SecurityService securityService;

    public RelatorioSistelcomController(RelatorioSistelcomService relatorioSistelcomService, SecurityService securityService) {
        this.relatorioSistelcomService = relatorioSistelcomService;
        this.securityService = securityService;
    }

    @PostMapping(value = "/gerar", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> gerarRelatorio(@RequestBody RelatorioSistelcomRequest relatorioSistelcomRequest) {

        AcessoUsuario usuario = securityService.getUsuarioLogado();
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        }

        try {
            securityService.verificarAcessoPermissoesMulticonta(usuario, relatorioSistelcomRequest.getMulticontaRequestDTO());

            RelatorioSistelcomService.RelatorioGerado relatorio = this.relatorioSistelcomService.gerarRelatorio(relatorioSistelcomRequest);

            Resource resource = new ByteArrayResource(relatorio.getDados());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(relatorio.getMediaType()));
            headers.setContentLength(relatorio.getDados().length);
            headers.setContentDispositionFormData("attachment", relatorio.getNomeArquivo());

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);

        } catch (RelatorioVazioException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
        } catch (RelatorioGenerationException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }

}