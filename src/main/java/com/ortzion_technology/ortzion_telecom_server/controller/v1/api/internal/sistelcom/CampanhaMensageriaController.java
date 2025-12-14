package com.ortzion_technology.ortzion_telecom_server.controller.v1.api.internal.sistelcom;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CampanhaMensageriaRequest;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.sistelcom.CampanhaMensageria;
import com.ortzion_technology.ortzion_telecom_server.security.service.SecurityService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.sistelcom.processamento.campanhas.CampanhaMensageriaService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/campanha-mensageria")
public class CampanhaMensageriaController {

    private final CampanhaMensageriaService campanhaMensageriaService;
    private final SecurityService securityService;

    public CampanhaMensageriaController(CampanhaMensageriaService campanhaMensageriaService, SecurityService securityService) {
        this.campanhaMensageriaService = campanhaMensageriaService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "/pegar",
            method = RequestMethod.POST)
    public ResponseEntity<Page<CampanhaMensageria>> pegarCampanhasMensageriaMulticonta(
            @RequestBody() CampanhaMensageriaRequest campanhaMensageriaRequest){

        AcessoUsuario usuario = securityService.getUsuarioLogado();

        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        securityService.verificarAcessoPermissoesMulticonta(usuario, campanhaMensageriaRequest.getMulticontaRequestDTO());

        try {

           Page<CampanhaMensageria> campanhaMensageria = campanhaMensageriaService.pegarCampanhasPorMulticonta(campanhaMensageriaRequest, usuario);

            return ResponseEntity.ok(campanhaMensageria);

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }

    }

}
