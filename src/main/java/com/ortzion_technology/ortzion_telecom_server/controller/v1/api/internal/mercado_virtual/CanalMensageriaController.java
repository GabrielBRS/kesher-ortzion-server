package com.ortzion_technology.ortzion_telecom_server.controller.v1.api.internal.mercado_virtual;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.CanalMensageria;
import com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual.CanalMensageriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/canal-mensageria")
public class CanalMensageriaController {

    private final CanalMensageriaService canalMensageriaService;

    @Autowired
    public CanalMensageriaController(CanalMensageriaService canalMensageriaService) {
        this.canalMensageriaService = canalMensageriaService;
    }

    @RequestMapping(value = "/pegar", method = RequestMethod.GET)
    public ResponseEntity<?> pegarProdutos() {

        List<CanalMensageria> canalMensageria = this.canalMensageriaService.pegarTodosCanaisMensageria();
        return ResponseEntity.ok(canalMensageria);

    }

}
