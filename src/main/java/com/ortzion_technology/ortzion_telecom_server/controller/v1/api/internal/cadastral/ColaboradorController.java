package com.ortzion_technology.ortzion_telecom_server.controller.v1.api.internal.cadastral;

import com.google.protobuf.ServiceException;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CadastroColaboradorRequest;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.PermissoesColaboradorRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Colaborador;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.security.service.SecurityService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.cadastral.ColaboradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/colaborador")
public class ColaboradorController {

    private final ColaboradorService colaboradorService;
    private final SecurityService securityService;

    @Autowired
    public ColaboradorController(ColaboradorService colaboradorService, SecurityService securityService) {
        this.colaboradorService = colaboradorService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "/atualizar", method = RequestMethod.POST)
    public ResponseEntity<?> pegarTodasPessoa(
            @RequestBody CadastroColaboradorRequest colaboradorRequest
    ) throws ServiceException {

        AcessoUsuario usuario = securityService.getUsuarioLogado();

        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        securityService.verificarAcessoPermissoesMulticonta(usuario, colaboradorRequest.getMulticonta());

        this.colaboradorService.atualizarColaborador(colaboradorRequest);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/cadastrar", method = RequestMethod.POST)
    public ResponseEntity<?> cadastrarPessoa(
            @RequestBody CadastroColaboradorRequest colaboradorRequest) throws ServiceException {

        AcessoUsuario usuario = securityService.getUsuarioLogado();

        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        securityService.verificarAcessoPermissoesMulticonta(usuario, colaboradorRequest.getMulticonta());

        this.colaboradorService.cadastrarColaborador(colaboradorRequest);

        return ResponseEntity.ok().build();

    }

    /**
     * Endpoint para pegar a LISTA MESTRA de colaboradores
     * (Chamado pelo 'carregarColaboradoresMestres' no Angular)
     * CORREÇÃO: Recebe 'multiconta' e passa para o service.
     */
    @RequestMapping(value = "/buscar", method = RequestMethod.POST)
    public ResponseEntity<?> pegarTodosColaboradores(
            @RequestBody MulticontaRequestDTO multiconta) {

        AcessoUsuario usuario = securityService.getUsuarioLogado();
        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }
        securityService.verificarAcessoPermissoesMulticonta(usuario, multiconta);

        // CORREÇÃO: Passa o objeto 'multiconta' para o service
        List<Colaborador> colaboradorList = this.colaboradorService.pegarTodosColaboradores(multiconta);
        return ResponseEntity.ok(colaboradorList);
    }

    /**
     * Endpoint para ASSOCIAR um colaborador a um departamento
     * (Chamado pelo 'handleAssociarColaborador' no Angular)
     */
    @RequestMapping(value = "/permissoes-acessos", method = RequestMethod.POST)
    public ResponseEntity<?> permissoesAcessosColaborador(
            @RequestBody PermissoesColaboradorRequest permissoesColaborador) throws ServiceException {

        AcessoUsuario usuario = securityService.getUsuarioLogado();
        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }
        securityService.verificarAcessoPermissoesMulticonta(usuario, permissoesColaborador.getMulticonta());

        this.colaboradorService.permissoesAcessosColaborador(permissoesColaborador);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint para pegar os colaboradores JÁ ASSOCIADOS a um departamento
     * (Chamado pelo 'toggleDepartamento' no Angular)
     */
    @RequestMapping(value = "/pegar-por-departamento", method = RequestMethod.POST)
    public ResponseEntity<?> pegarColaboradoresPorDepartamento(
            @RequestBody PermissoesColaboradorRequest request) throws ServiceException {

        AcessoUsuario usuario = securityService.getUsuarioLogado();
        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }
        securityService.verificarAcessoPermissoesMulticonta(usuario, request.getMulticonta());

        List<Colaborador> colaboradorList = this.colaboradorService.pegarColaboradoresPorDepartamento(request);
        return ResponseEntity.ok(colaboradorList);
    }

    /**
     * Endpoint para DESASSOCIAR um colaborador de um departamento
     * (Chamado pelo 'desassociarColaborador' no Angular)
     */
    @RequestMapping(value = "/desassociar", method = RequestMethod.POST)
    public ResponseEntity<?> desassociarColaboradorDepartamento(
            @RequestBody PermissoesColaboradorRequest request) throws ServiceException {

        AcessoUsuario usuario = securityService.getUsuarioLogado();
        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }
        securityService.verificarAcessoPermissoesMulticonta(usuario, request.getMulticonta());

        this.colaboradorService.desassociarColaboradorDepartamento(request);
        return ResponseEntity.ok().build();
    }
}
