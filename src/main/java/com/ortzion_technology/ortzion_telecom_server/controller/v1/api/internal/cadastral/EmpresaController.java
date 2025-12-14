package com.ortzion_technology.ortzion_telecom_server.controller.v1.api.internal.cadastral;

import com.google.protobuf.ServiceException;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CadastroEmpresaRequest;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.EstoqueMercadoriaVirtualRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Empresa;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.security.TwoA.UserDetailsServiceImpl;
import com.ortzion_technology.ortzion_telecom_server.security.service.SecurityService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.cadastral.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/empresa")
public class EmpresaController {

    private final EmpresaService empresaService;
    private final SecurityService securityService;

    @Autowired
    public EmpresaController(EmpresaService empresaService, SecurityService securityService) {
        this.empresaService = empresaService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "/pegar", method = RequestMethod.POST)
    public ResponseEntity<?> pegarEmpresaUsuarioLogado(
            @RequestBody MulticontaRequestDTO multiconta) throws IOException {

        AcessoUsuario usuario = securityService.getUsuarioLogado();

        multiconta.setIdDepartamento(1);

        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        securityService.verificarAcessoPermissoesMulticonta(usuario, multiconta);

        List<Empresa> empresa = this.empresaService.pegarEmpresaMulticonta(multiconta);

        return ResponseEntity.ok(empresa);
    }

    @RequestMapping(value = "/cadastrar", method = RequestMethod.POST)
    public ResponseEntity<?> cadastrarPessoa(
            @RequestBody CadastroEmpresaRequest cadastroEmpresa) throws IOException, ServiceException {

        AcessoUsuario usuario = securityService.getUsuarioLogado();

        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        securityService.verificarAcessoPermissoesMulticonta(usuario, cadastroEmpresa.getMulticonta());

        this.empresaService.cadastrarEmpresa(cadastroEmpresa);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/editar", method = RequestMethod.PUT)
    public ResponseEntity<?> editarPessoa(
            @RequestBody CadastroEmpresaRequest cadastroEmpresa) throws IOException {

        AcessoUsuario usuario = securityService.getUsuarioLogado();

        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        securityService.verificarAcessoPermissoesMulticonta(usuario, cadastroEmpresa.getMulticonta());

        this.empresaService.editarEmpresa(cadastroEmpresa);

        return ResponseEntity.ok().build();
    }

}
