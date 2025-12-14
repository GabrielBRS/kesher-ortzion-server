package com.ortzion_technology.ortzion_telecom_server.security.service;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CadastroColaboradorRequest;
import com.ortzion_technology.ortzion_telecom_server.security.dto.AcessoGrupoDTO;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoGrupo;
import com.ortzion_technology.ortzion_telecom_server.security.repository.AcessoGrupoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AcessoGrupoService {

    private final AcessoGrupoRepository acessoGrupoRepository;

    public AcessoGrupoService(AcessoGrupoRepository acessoGrupoRepository) {
        this.acessoGrupoRepository = acessoGrupoRepository;
    }

    public List<AcessoGrupo> pegarTodosAcessoGrupoPorInstituicao(){
        return acessoGrupoRepository.findAll();
    }

    public List<AcessoGrupo> atribuirAcessoGrupoDaEmpresaAoColaborador(CadastroColaboradorRequest colaboradorRequest){

        List<Long> idsGrupo = colaboradorRequest.getAcessoGrupo()
                .stream()
                .filter(Objects::nonNull)
                .map(AcessoGrupoDTO::getIdGrupo)
                .toList();

        return this.acessoGrupoRepository.buscarAcessoGrupoPorId(idsGrupo);
    }

    public List<AcessoGrupo> buscarAcessoGrupoDaEmpresa(List<AcessoGrupoDTO> acessoGrupo){

        List<Long> idsGrupo = acessoGrupo
                .stream()
                .filter(Objects::nonNull)
                .map(AcessoGrupoDTO::getIdGrupo)
                .toList();

        return this.acessoGrupoRepository.buscarAcessoGrupoPorId(idsGrupo);
    }

    public Optional<AcessoGrupo> buscarPorId(Long idGrupo){
        return this.acessoGrupoRepository.findById(idGrupo);
    }

}
