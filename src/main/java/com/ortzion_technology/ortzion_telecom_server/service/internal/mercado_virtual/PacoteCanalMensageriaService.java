package com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.PacoteCanalMensageria;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.mercado_virtual.PacoteCanalMensageriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacoteCanalMensageriaService {

    private final PacoteCanalMensageriaRepository pacoteCanalMensageriaRepository;

    public PacoteCanalMensageriaService(PacoteCanalMensageriaRepository pacoteCanalMensageriaRepository) {
        this.pacoteCanalMensageriaRepository = pacoteCanalMensageriaRepository;
    }

    public List<PacoteCanalMensageria> pegarTodosPacoteCanaisMensageria(Integer tipoPacote) {
        return pacoteCanalMensageriaRepository.pegarTodosPacoteCanalMensageria();
    }

    public List<PacoteCanalMensageria> pegarTodosPacoteCanaisMensageriaPorTipo(Integer tipoPacote) {
        return pacoteCanalMensageriaRepository.pegarTodosPacoteCanaisMensageriaPorTipo(tipoPacote);
    }

    public List<PacoteCanalMensageria> pegarPacoteCanalMensageriaPorIdOuNome(Integer idPacoteCanalMensageria, String nomeCanal){
        return this.pacoteCanalMensageriaRepository.pegarPacoteCanalMensageriaPorIdOuNome(idPacoteCanalMensageria, nomeCanal);
    }

    public PacoteCanalMensageria salvarPacoteCanalMensageria(PacoteCanalMensageria pacoteCanalMensageria){
        return pacoteCanalMensageriaRepository.save(pacoteCanalMensageria);
    }

    public Optional<PacoteCanalMensageria> pegarPacoteCanalMensageriaPorId(Integer idPacoteCanalMensageria){
        return pacoteCanalMensageriaRepository.findById(idPacoteCanalMensageria);
    }

}
