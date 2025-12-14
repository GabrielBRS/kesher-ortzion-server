package com.ortzion_technology.ortzion_telecom_server.service.internal.cadastral;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Contato;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Endereco;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.cadastral.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    @Autowired
    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    public Endereco pegarEnderecoUsuarioLogado(AcessoUsuario usuarioLogado){
        return enderecoRepository.pegarEnderecoPorPessoa(usuarioLogado.getDocumentoUsuario());
    }

    public Endereco pegarEnderecoEmpresaLogado(AcessoUsuario usuarioLogado){
        return enderecoRepository.findByEmpresa_Email(usuarioLogado.getDocumentoUsuario());
    }

    public Optional<Endereco> pegarContatoPorId(Long id) {
        return enderecoRepository.findById(id);
    }

    public Endereco salvarEndereco(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

}
