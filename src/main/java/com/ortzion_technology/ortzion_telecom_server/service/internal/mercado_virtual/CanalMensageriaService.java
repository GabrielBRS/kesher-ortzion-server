package com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.EstoqueMercadoriaVirtualRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.CanalMensageria;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.mercado_virtual.CanalMensageriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CanalMensageriaService {

    private final CanalMensageriaRepository canalMensageriaRepository;

    @Autowired
    public CanalMensageriaService(CanalMensageriaRepository canalMensageriaRepository) {
        this.canalMensageriaRepository = canalMensageriaRepository;
    }

    public List<CanalMensageria> pegarTodosCanaisMensageria(){
        return canalMensageriaRepository.pegarTodosCanaisMensageria();
    }

    public List<CanalMensageria> pegarCanalMEnsageriaPorIdOuNome(Integer idCanalMensageria, String nomeCanal){
        return this.canalMensageriaRepository.pegarCanalMEnsageriaPorIdOuNome(idCanalMensageria, nomeCanal);
    }

    public CanalMensageria salvarCanalMensageria(CanalMensageria canalMensageria){
        return canalMensageriaRepository.save(canalMensageria);
    }

    public Optional<CanalMensageria> pegarCanalMensageriaPorId(Integer idCanalMensageria){
        return canalMensageriaRepository.findById(idCanalMensageria);
    }

}
