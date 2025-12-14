package com.ortzion_technology.ortzion_telecom_server.controller.v1.api.internal.bigdata.pessoa;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.AtualizarClientePessoaBigDataRequest;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CadastrarClientePessoaBigDataRequest;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.FiltroClientePessoaBigDataRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.bigdata.pessoa.ClientePessoaBigData;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.security.service.SecurityService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.bigdata.pessoa.ClientePessoaBigDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cliente-pessoa/bigdata")
public class ClientePessoaBigDataController {

    private final ClientePessoaBigDataService clientePessoaBigDataService;
    private final SecurityService securityService;

    @Autowired
    public ClientePessoaBigDataController(ClientePessoaBigDataService clientePessoaBigDataService, SecurityService securityService) {
        this.clientePessoaBigDataService = clientePessoaBigDataService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "/buscar", method = RequestMethod.GET)
    public ResponseEntity<?> pegarClientePessoaUsuarioLogado(
            @RequestBody FiltroClientePessoaBigDataRequest clientePessoa
    ) {

        AcessoUsuario usuario = securityService.getUsuarioLogado();

        if (usuario == null) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }

        List<ClientePessoaBigData> clientePessoaBigDataList = this.clientePessoaBigDataService.pegarClientePessoaBigData(clientePessoa);

        return ResponseEntity.ok(clientePessoaBigDataList);

    }

    @RequestMapping(value = "/cadastrar", method = RequestMethod.POST)
    public ResponseEntity<?> cadastrarClientePessoa(
            @RequestBody CadastrarClientePessoaBigDataRequest cadastrarClientePessoa
    ) {

        this.clientePessoaBigDataService.cadastrarPessoa(cadastrarClientePessoa);

        return ResponseEntity.ok().build();

    }

    @RequestMapping(value = "/atualizar", method = RequestMethod.POST)
    public ResponseEntity<?> atualizarClientePessoa(
            @RequestBody AtualizarClientePessoaBigDataRequest atualizarClientePessoa
    ) {

        this.clientePessoaBigDataService.atualizarClientePessoaBigData(atualizarClientePessoa);

        return ResponseEntity.ok().build();

    }

}
