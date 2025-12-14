package com.ortzion_technology.ortzion_telecom_server.security.service;

import com.google.protobuf.ServiceException;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Pessoa;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoGrupo;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.security.entity.Multiconta;
import com.ortzion_technology.ortzion_telecom_server.security.repository.AcessoGrupoRepository;
import com.ortzion_technology.ortzion_telecom_server.security.repository.AcessoUsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AcessoUsuarioService {

    private final AcessoUsuarioRepository acessoUsuarioRepository;

    public AcessoUsuarioService(AcessoUsuarioRepository acessoUsuarioRepository) {
        this.acessoUsuarioRepository = acessoUsuarioRepository;
    }

    public AcessoUsuario criarAcessoUsuario(Pessoa pessoa) {

        Optional<AcessoUsuario> acessoUsuarioOptional = this.acessoUsuarioRepository.findByDocumentoUsuario(pessoa.getDocumento());

        if(acessoUsuarioOptional.isEmpty()){
            AcessoUsuario acessoUsuario =  new AcessoUsuario();
            acessoUsuario.setStatusUsuario(1);
            acessoUsuario.setDocumentoUsuario(pessoa.getDocumento());
            acessoUsuario.setSenhaUsuario("123456");
            return acessoUsuarioRepository.save(acessoUsuario);
        }else{
            return acessoUsuarioOptional.get();
        }

    }

    public AcessoUsuario criarMulticonta(Multiconta multiconta) {

        Optional<AcessoUsuario> acessoUsuario =  this.acessoUsuarioRepository.findById(multiconta.getMulticontaId().getIdUsuario());

        if (acessoUsuario.isEmpty()) {
            return null;
        }

        AcessoUsuario usuario = acessoUsuario.get();
        usuario.setMulticontas(Set.of(multiconta));

        return acessoUsuarioRepository.save(usuario);
    }

    public AcessoUsuario buscarAcessoUsuario(String documento) throws ServiceException {

        Optional<AcessoUsuario> acessoUsuarioOptional = this.acessoUsuarioRepository.findByDocumentoUsuario(documento);

        if(acessoUsuarioOptional.isPresent()){
            return acessoUsuarioOptional.get();
        }else{
            throw new ServiceException("Este usuário não existe");
        }

    }

}
