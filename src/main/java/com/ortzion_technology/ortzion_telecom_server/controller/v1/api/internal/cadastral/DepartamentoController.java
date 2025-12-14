package com.ortzion_technology.ortzion_telecom_server.controller.v1.api.internal.cadastral;

import com.google.protobuf.ServiceException; // Importe a exceção correta
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.FiltroDepartamentoRequest;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.security.service.SecurityService;
import com.ortzion_technology.ortzion_telecom_server.security.vo.DepartamentoVO;
import com.ortzion_technology.ortzion_telecom_server.service.internal.cadastral.DepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/departamento")
public class DepartamentoController {

    private final DepartamentoService departamentoService;
    private final SecurityService securityService;

    @Autowired
    public DepartamentoController(DepartamentoService departamentoService, SecurityService securityService) {
        this.departamentoService = departamentoService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "/pegar-por-empresa", method = RequestMethod.POST)
    public ResponseEntity<?> pegarDepartamentosPorEmpresa(
            @RequestBody FiltroDepartamentoRequest request) throws IOException, ServiceException {

        AcessoUsuario usuario = securityService.getUsuarioLogado();
        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }
        securityService.verificarAcessoPermissoesMulticonta(usuario, request.getMulticonta());

        List<DepartamentoVO> departamentoVOResponse = this.departamentoService.pegarDepartamentosPorEmpresa(request);

        return ResponseEntity.ok(departamentoVOResponse);
    }

    @RequestMapping(value = "/pegar-mestres", method = RequestMethod.POST)
    public ResponseEntity<?> pegarDepartamentosMestres(
            @RequestBody FiltroDepartamentoRequest request) throws IOException, ServiceException {

        AcessoUsuario usuario = securityService.getUsuarioLogado();
        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }
        securityService.verificarAcessoPermissoesMulticonta(usuario, request.getMulticonta());

        List<DepartamentoVO> departamentoVOResponse = this.departamentoService.pegarDepartamentosMestres(request);

        return ResponseEntity.ok(departamentoVOResponse);
    }

    @RequestMapping(value = "/associar", method = RequestMethod.POST)
    public ResponseEntity<?> associarDepartamento(
            @RequestBody FiltroDepartamentoRequest request) throws IOException, ServiceException {

        AcessoUsuario usuario = securityService.getUsuarioLogado();
        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }
        securityService.verificarAcessoPermissoesMulticonta(usuario, request.getMulticonta());

        List<DepartamentoVO> departamentoVOResponse = this.departamentoService.associarDepartamento(request);

        return ResponseEntity.ok(departamentoVOResponse);
    }

    @RequestMapping(value = "/desassociar", method = RequestMethod.POST)
    public ResponseEntity<?> desassociarDepartamento(
            @RequestBody FiltroDepartamentoRequest request) throws IOException, ServiceException {

        AcessoUsuario usuario = securityService.getUsuarioLogado();
        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }
        securityService.verificarAcessoPermissoesMulticonta(usuario, request.getMulticonta());

        List<DepartamentoVO> departamentoVOResponse = this.departamentoService.desassociarDepartamento(request);

        return ResponseEntity.ok(departamentoVOResponse);
    }

}