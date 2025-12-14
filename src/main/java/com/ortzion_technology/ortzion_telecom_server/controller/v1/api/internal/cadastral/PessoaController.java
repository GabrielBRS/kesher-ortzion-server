package com.ortzion_technology.ortzion_telecom_server.controller.v1.api.internal.cadastral;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CadastroPessoaRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Colaborador;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Pessoa;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.security.service.SecurityService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.cadastral.ColaboradorService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.cadastral.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pessoa")
public class PessoaController {

    private final PessoaService pessoaService;
    private final SecurityService securityService;

    @Autowired
    public PessoaController(PessoaService pessoaService, SecurityService securityService) {
        this.pessoaService = pessoaService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "/buscar", method = RequestMethod.GET)
    public ResponseEntity<?> buscarPessoaUsuarioLogado() {

        AcessoUsuario usuario = securityService.getUsuarioLogado();

        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        Pessoa pessoa = this.pessoaService.pegarPessoaUsuarioLogado(usuario);

        return ResponseEntity.ok(pessoa);

    }

    @RequestMapping(value = "/listar", method = RequestMethod.POST)
    public ResponseEntity<?> buscarPessoa(
            @RequestBody MulticontaRequestDTO multiconta) {

        AcessoUsuario usuario = securityService.getUsuarioLogado();

        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        Pessoa pessoa = this.pessoaService.buscarPessoaPorConta(multiconta, usuario);

        return ResponseEntity.ok(pessoa);

    }

    @RequestMapping(value = "/cadastrar", method = RequestMethod.POST)
    public ResponseEntity<?> cadastrarPessoa(
            @RequestBody CadastroPessoaRequest cadastroPessoaRequest) throws ServerException {

//        AcessoUsuario usuario = securityService.getUsuarioLogado();
//
//        if (usuario == null) {
//            return ResponseEntity.status(401).body("Usuário não autenticado");
//        }

        this.pessoaService.cadastrarPessoa(cadastroPessoaRequest);

        return ResponseEntity.ok().build();

    }

    @RequestMapping(value = "/atualizar", method = RequestMethod.POST)
    public ResponseEntity<?> atualizarPessoa(
            @RequestBody CadastroPessoaRequest cadastroPessoaRequest) throws ServerException {

//        AcessoUsuario usuario = securityService.getUsuarioLogado();
//
//        if (usuario == null) {
//            return ResponseEntity.status(401).body("Usuário não autenticado");
//        }

        this.pessoaService.cadastrarPessoa(cadastroPessoaRequest);

        return ResponseEntity.ok().build();

    }

}
