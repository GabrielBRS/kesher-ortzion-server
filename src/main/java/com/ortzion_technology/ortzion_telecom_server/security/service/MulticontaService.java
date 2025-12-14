package com.ortzion_technology.ortzion_telecom_server.security.service;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Departamento;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Empresa;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Pessoa;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.PreCadastro;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaDTO;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaResponseDTO;
import com.ortzion_technology.ortzion_telecom_server.security.entity.Multiconta;
import com.ortzion_technology.ortzion_telecom_server.security.entity.MulticontaId;
import com.ortzion_technology.ortzion_telecom_server.security.enums.MulticontaEnum;
import com.ortzion_technology.ortzion_telecom_server.security.repository.MulticontaRepository;
import com.ortzion_technology.ortzion_telecom_server.security.repository.custom.MulticontaRepositoryCustomImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MulticontaService {

    private final MulticontaRepository multicontaRepository;
    private final MulticontaRepositoryCustomImpl multicontaRepositoryCustomImpl;

    public MulticontaService(MulticontaRepository multicontaRepository, MulticontaRepositoryCustomImpl multicontaRepositoryCustomImpl) {
        this.multicontaRepository = multicontaRepository;
        this.multicontaRepositoryCustomImpl = multicontaRepositoryCustomImpl;
    }

    public List<MulticontaResponseDTO> pegarTodasMulticontasPorAcessoUsuario(AcessoUsuario acessoUsuario) {
        return multicontaRepository.pegarTodasMulticontasPorAcessoUsuario(acessoUsuario.getIdUsuario());
    }

    public List<MulticontaResponseDTO> pegarTodasMulticontasPorMulticonta(MulticontaRequestDTO multicontaRequestDTO, AcessoUsuario usuario) {
        Long idUsuario = multicontaRequestDTO.getIdUsuario();
        return multicontaRepository.pegarTodasMulticontasPorMulticonta(idUsuario);
    }

    public Multiconta criarMulticontaColaborador(AcessoUsuario acessoUsuario, Pessoa novaPessoa, Departamento departamento, Empresa empresa, MulticontaDTO multicontaDTO) {

        MulticontaId multicontaId = new MulticontaId();
        Multiconta multiconta = new Multiconta();

        multicontaId.setIdUsuario(acessoUsuario.getIdUsuario());
        multicontaId.setTipoPessoa(2);
        multicontaId.setIdSubjectus(empresa.getIdEmpresa());
        multicontaId.setIdDepartamento(departamento.getIdDepartamento());
        multiconta.setMulticontaId(multicontaId);

        multiconta.setDepartamento(departamento);
        multiconta.setAcessoUsuario(acessoUsuario);
        multiconta.setStatusMulticonta(MulticontaEnum.ATIVO.getCodigoNumerico());

        multiconta.setEmail(multiconta.getEmail());
        multiconta.setUsername(multiconta.getUsername());
        multiconta.setTelefone(multicontaDTO.getTelefone());

        return this.multicontaRepository.save(multiconta);
    }

    public Multiconta criarMulticonta(AcessoUsuario acessoUsuario, Pessoa novaPessoa, Departamento departamento) {

        MulticontaId multicontaId = new MulticontaId();
        Multiconta multiconta = new Multiconta();

        multicontaId.setIdUsuario(acessoUsuario.getIdUsuario());
        multicontaId.setTipoPessoa(1);
        multicontaId.setIdSubjectus(novaPessoa.getIdPessoa());
        multicontaId.setIdDepartamento(departamento.getIdDepartamento());
        multiconta.setMulticontaId(multicontaId);
        multiconta.setIdSubjectus(novaPessoa.getIdPessoa());
        multiconta.setDepartamento(departamento);
        multiconta.setStatusMulticonta(MulticontaEnum.ATIVO.getCodigoNumerico());
        multiconta.setAcessoUsuario(acessoUsuario);

        return this.multicontaRepository.save(multiconta);
    }

    public Multiconta atualizar(Multiconta multiconta) {
        return this.multicontaRepository.save(multiconta);
    }

    public Multiconta buscarMulticontaColaboradorEAssociar(AcessoUsuario acessoUsuario, Integer tipoPessoa, Departamento departamento, Empresa empresa, MulticontaDTO multicontaDTO) {

        Optional<Multiconta> multicontaOptional = this.multicontaRepositoryCustomImpl.pegarMulticontaPerfilEmpresarialColaborador(acessoUsuario.getIdUsuario(), tipoPessoa, empresa.getIdEmpresa(), departamento.getIdDepartamento());

        if(multicontaOptional.isPresent()) {
            return multicontaOptional.get();

        }else{
            MulticontaId multicontaId = new MulticontaId();
            Multiconta multiconta = new Multiconta();

            multicontaId.setIdUsuario(acessoUsuario.getIdUsuario());
            multicontaId.setTipoPessoa(2);
            multicontaId.setIdSubjectus(empresa.getIdEmpresa());
            multicontaId.setIdDepartamento(departamento.getIdDepartamento());
            multiconta.setMulticontaId(multicontaId);

            multiconta.setDepartamento(departamento);
            multiconta.setAcessoUsuario(acessoUsuario);
            multiconta.setStatusMulticonta(MulticontaEnum.ATIVO.getCodigoNumerico());

            multiconta.setEmail(multiconta.getEmail());
            multiconta.setUsername(multiconta.getUsername());
            multiconta.setTelefone(multicontaDTO.getTelefone());

            return this.multicontaRepository.save(multiconta);
        }
    }

    public List<MulticontaResponseDTO> buscarPorEmpresaEDepartamento(Integer tipoPessoa, Long idEmpresa, Integer idDepartamento) {
        return this.multicontaRepository.pegarMulticontasPorIdEmpresaEIdDepartamento(tipoPessoa, idEmpresa, idDepartamento);
    }

    public void desassociarMulticonta(Long idUsuario, Integer tipoPessoa, Long idEmpresa, Integer idDepartamento) {

        // 1. Usar o novo método do repositório para buscar a ENTIDADE
        Optional<Multiconta> multicontaParaDeletar = multicontaRepository.pegarMulticontaPorChaveCompleta(
                idUsuario,
                tipoPessoa,
                idEmpresa,
                idDepartamento
        );

        if (multicontaParaDeletar.isPresent()) {
            multicontaRepository.delete(multicontaParaDeletar.get());
        } else {

        }
    }

    public Optional<Multiconta>  buscarMulticontaPorNomeUsuario(String nomeUsuario) {

        Optional<Multiconta> multiconta = multicontaRepository.buscarMulticontaPorNomeUsuario(
                nomeUsuario
        );

        return multiconta;
    }



}