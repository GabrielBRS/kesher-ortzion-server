package com.ortzion_technology.ortzion_telecom_server.controller.v1.api.internal.mercado_virtual;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.EstoqueMercadoriaVirtualRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.EstoqueMercadoriaVirtual;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.entity.dto.EstoqueMercadoriaVirtualDTO;
import com.ortzion_technology.ortzion_telecom_server.security.TwoA.UserDetailsServiceImpl;
import com.ortzion_technology.ortzion_telecom_server.security.service.SecurityService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual.EstoqueMercadoriaVirtualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping(value = "/api/v1/estoque-mercadoria-virtual")
public class EstoqueMercadoriaVirtualController {

    private final EstoqueMercadoriaVirtualService estoqueMercadoriaVirtualService;
    private final SecurityService securityService;

    @Autowired
    public EstoqueMercadoriaVirtualController(EstoqueMercadoriaVirtualService estoqueMercadoriaVirtualService, SecurityService securityService) {
        this.estoqueMercadoriaVirtualService = estoqueMercadoriaVirtualService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "/pegar-estoque/usuario-logado", method = RequestMethod.GET)
    public ResponseEntity<?> pegarProdutoUsuarioLogado(
            @RequestBody EstoqueMercadoriaVirtualRequest estoqueMercadoriaVirtualRequest
    ) {

        AcessoUsuario usuario = securityService.getUsuarioLogado();

        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        securityService.verificarAcessoPermissoesMulticonta(usuario, estoqueMercadoriaVirtualRequest.getMulticonta());

        EstoqueMercadoriaVirtualDTO estoqueMercadoriaVirtualDTO = new EstoqueMercadoriaVirtualDTO(estoqueMercadoriaVirtualRequest);
        List<EstoqueMercadoriaVirtual> canalMensageria = this.estoqueMercadoriaVirtualService.pegarTodosEstoquesMulticonta(estoqueMercadoriaVirtualDTO, usuario);
        return ResponseEntity.ok(canalMensageria);
    }

    @RequestMapping(
            value = "/pegar-estoque",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<EstoqueMercadoriaVirtual>> pegarEstoque(
            @RequestBody EstoqueMercadoriaVirtualRequest estoqueMercadoriaVirtualRequest
    ) {

        AcessoUsuario usuario = securityService.getUsuarioLogado();

        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        securityService.verificarAcessoPermissoesMulticonta(usuario, estoqueMercadoriaVirtualRequest.getMulticonta());

        EstoqueMercadoriaVirtualDTO estoqueMercadoriaVirtualDTO = new EstoqueMercadoriaVirtualDTO(estoqueMercadoriaVirtualRequest);
        List<EstoqueMercadoriaVirtual> estoqueMercadoriaVirtual = estoqueMercadoriaVirtualService.pegarTodosEstoquesMulticonta(estoqueMercadoriaVirtualDTO, usuario);
        return ResponseEntity.ok(estoqueMercadoriaVirtual);
    }

}
