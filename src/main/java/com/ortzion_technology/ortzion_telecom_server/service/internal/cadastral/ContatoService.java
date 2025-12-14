package com.ortzion_technology.ortzion_telecom_server.service.internal.cadastral;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Contato;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.cadastral.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContatoService {

    private final ContatoRepository contatoRepository;

    @Autowired
    public ContatoService(ContatoRepository contatoRepository) {
        this.contatoRepository = contatoRepository;
    }

    public Optional<Contato> pegarContatoPorId(Long id) {
        return contatoRepository.findById(id);
    }

    public Contato salvarContato(Contato contato) {
        return contatoRepository.save(contato);
    }

}
