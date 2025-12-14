package com.ortzion_technology.ortzion_telecom_server.controller.v1.api.internal.mercado_virtual;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.PacoteCanalMensageria;
import com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual.PacoteCanalMensageriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pacote-canal-mensageria")
public class PacoteCanalMensageriaController {

    private final PacoteCanalMensageriaService pacoteCanalMensageriaService;

    public PacoteCanalMensageriaController(PacoteCanalMensageriaService pacoteCanalMensageriaService) {
        this.pacoteCanalMensageriaService = pacoteCanalMensageriaService;
    }

    @RequestMapping(value = "/pegar/{tipoPacote}", method = RequestMethod.GET)
    public ResponseEntity<List<PacoteCanalMensageria>> pegarProdutos(
            @PathVariable("tipoPacote") Integer tipoPacote
    ) {
        List<PacoteCanalMensageria> pacoteCanalMensageria = this.pacoteCanalMensageriaService.pegarTodosPacoteCanaisMensageriaPorTipo(tipoPacote);
        return ResponseEntity.ok(pacoteCanalMensageria);
    }

}
