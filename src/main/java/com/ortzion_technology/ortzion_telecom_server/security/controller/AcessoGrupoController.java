package com.ortzion_technology.ortzion_telecom_server.security.controller;

import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoGrupo;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.security.TwoA.UserDetailsServiceImpl;
import com.ortzion_technology.ortzion_telecom_server.security.service.AcessoGrupoService;
import com.ortzion_technology.ortzion_telecom_server.security.service.SecurityService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/grupos-seguranca")
public class AcessoGrupoController {

    private final AcessoGrupoService acessoGrupoService;
    private final SecurityService securityService;

    public AcessoGrupoController(AcessoGrupoService acessoGrupoService, SecurityService securityService) {
        this.acessoGrupoService = acessoGrupoService;
        this.securityService = securityService;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN','USUARIO','ADMINISTRADOR')")
    @RequestMapping(value = "/pegar", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> pegarTodosAcessoGrupoPorInstituicao(){

        AcessoUsuario usuario = securityService.getUsuarioLogado();

        if (usuario != null) {
            System.out.printf("Usuário logado: Documento: %s | ID: %d%n",
                    usuario.getDocumentoUsuario(),
                    usuario.getIdUsuario());
        } else {
            System.out.println("Nenhum usuário autenticado.");
        }

        List<AcessoGrupo> acessoGrupos = this.acessoGrupoService.pegarTodosAcessoGrupoPorInstituicao();
        return ResponseEntity.ok(acessoGrupos);
    }

}
