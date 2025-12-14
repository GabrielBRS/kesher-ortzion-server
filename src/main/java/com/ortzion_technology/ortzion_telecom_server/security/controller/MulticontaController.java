package com.ortzion_technology.ortzion_telecom_server.security.controller;

import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaDTO;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaResponseDTO;
import com.ortzion_technology.ortzion_telecom_server.security.repository.AcessoUsuarioRepository;
import com.ortzion_technology.ortzion_telecom_server.security.service.MulticontaService;
import com.ortzion_technology.ortzion_telecom_server.security.service.SecurityService;
import com.ortzion_technology.ortzion_telecom_server.security.vo.MulticontaRequestVO;
import com.ortzion_technology.ortzion_telecom_server.security.vo.MulticontaVO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/multiconta")
public class MulticontaController {

    private final MulticontaService multicontaService;

    private final AcessoUsuarioRepository acessoUsuarioRepository;

    private final SecurityService securityService;

    public MulticontaController(MulticontaService multicontaService, AcessoUsuarioRepository acessoUsuarioRepository, SecurityService securityService) {
        this.multicontaService = multicontaService;
        this.acessoUsuarioRepository = acessoUsuarioRepository;
        this.securityService = securityService;
    }

    @RequestMapping(
            value = "/pegar",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> pegarTodasMulticontasPorAcessoUsuario(
            @RequestBody() MulticontaRequestDTO multiconta
            ){

        AcessoUsuario usuario = securityService.getUsuarioLogado();

        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        securityService.verificarAcessoPermissoesMulticonta(usuario, multiconta);

        List<MulticontaResponseDTO> multicontaResponseDTOS = this.multicontaService.pegarTodasMulticontasPorAcessoUsuario(usuario);
        return ResponseEntity.ok(multicontaResponseDTOS);

    }

    @RequestMapping(
            value = "/listar",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> pegarTodasMulticontasPorMulticonta(
            @RequestBody() MulticontaRequestDTO multiconta
    ){

        AcessoUsuario usuario = securityService.getUsuarioLogado();

        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        securityService.verificarAcessoPermissoesMulticonta(usuario, multiconta);

        List<MulticontaResponseDTO> multicontaResponseDTOS = this.multicontaService.pegarTodasMulticontasPorMulticonta(multiconta, usuario);

        return ResponseEntity.ok(multicontaResponseDTOS);

    }

    @RequestMapping(
            value = "/atualizar",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> editarMinhasMulticontas(
            @RequestBody() MulticontaRequestVO conta
    ){

        AcessoUsuario usuario = securityService.getUsuarioLogado();

        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        securityService.verificarAcessoPermissoesMulticonta(usuario, conta.getMulticonta());

        List<MulticontaResponseDTO> multicontaResponseDTOS = this.multicontaService.pegarTodasMulticontasPorAcessoUsuario(usuario);
        return ResponseEntity.ok(multicontaResponseDTOS);

    }

}
