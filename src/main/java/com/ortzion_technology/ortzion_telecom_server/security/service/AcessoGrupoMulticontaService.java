package com.ortzion_technology.ortzion_telecom_server.security.service;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Departamento;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Pessoa;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaResponseDTO;
import com.ortzion_technology.ortzion_telecom_server.security.entity.*;
import com.ortzion_technology.ortzion_telecom_server.security.enums.MulticontaEnum;
import com.ortzion_technology.ortzion_telecom_server.security.repository.AcessoGrupoMulticontaRepository;
import com.ortzion_technology.ortzion_telecom_server.security.repository.MulticontaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AcessoGrupoMulticontaService {

    private final AcessoGrupoMulticontaRepository acessoGrupoMulticontaRepository;

    public AcessoGrupoMulticontaService(AcessoGrupoMulticontaRepository acessoGrupoMulticontaRepository) {
        this.acessoGrupoMulticontaRepository = acessoGrupoMulticontaRepository;
    }

//    public List<MulticontaResponseDTO> pegarTodasAcessoGrupoMulticontaPorAcessoUsuario(AcessoUsuario acessoUsuario) {
//        return acessoGrupoMulticontaRepository.pegarTodasMulticontasPorAcessoUsuario(acessoUsuario.getIdUsuario());
//    }

    public Set<AcessoGrupoMulticonta> criarAcessoGrupoMulticonta(
            List<AcessoGrupo> novasRoles, Multiconta multiconta) {

        Set<AcessoGrupoMulticonta> acessoGrupoMulticontaSet = novasRoles.stream()
                .map(acessoGrupo -> {

                    MulticontaId multicontaId = multiconta.getMulticontaId();

                    AcessoGrupoMulticontaId relacaoId = new AcessoGrupoMulticontaId(
                            acessoGrupo.getIdGrupo(),
                            multicontaId.getIdUsuario(),
                            multicontaId.getTipoPessoa(),
                            multicontaId.getIdSubjectus(),
                            multicontaId.getIdDepartamento()
                    );

                    AcessoGrupoMulticonta acessoGrupoMulticonta = new AcessoGrupoMulticonta();
                    acessoGrupoMulticonta.setAcessoGrupo(acessoGrupo);
                    acessoGrupoMulticonta.setMulticonta(multiconta);
                    acessoGrupoMulticonta.setId(relacaoId);

                    return acessoGrupoMulticonta;
                })
                .collect(Collectors.toSet());

        List<AcessoGrupoMulticonta> savedList = this.acessoGrupoMulticontaRepository.saveAll(acessoGrupoMulticontaSet);

        return new HashSet<>(savedList);
    }

}
