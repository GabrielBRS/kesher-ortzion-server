package com.ortzion_technology.ortzion_telecom_server.service.internal.cadastral;

import com.google.protobuf.ServiceException;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CadastroEmpresaRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Departamento;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Empresa;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Endereco;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.PreCadastro;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.cadastral.EmpresaRepository;
import com.ortzion_technology.ortzion_telecom_server.security.criptografia.TokenEncryptorDecryptorService;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import com.ortzion_technology.ortzion_telecom_server.security.entity.*;
import com.ortzion_technology.ortzion_telecom_server.security.repository.AcessoGrupoMulticontaRepository;
import com.ortzion_technology.ortzion_telecom_server.security.repository.AcessoUsuarioRepository;
import com.ortzion_technology.ortzion_telecom_server.security.repository.MulticontaRepository;
import com.ortzion_technology.ortzion_telecom_server.security.service.AcessoGrupoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.rmi.ServerError;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final PreCadastroService preCadastroService;
    private final MulticontaRepository multicontaRepository;
    private final AcessoGrupoMulticontaRepository acessoGrupoMulticontaRepository;
    private final DepartamentoService departamentoService;
    private final AcessoGrupoService acessoGrupoService;
    private final AcessoUsuarioRepository acessoUsuarioRepository;

    @Autowired
    public EmpresaService(EmpresaRepository empresaRepository,
                          PreCadastroService preCadastroService,
                          MulticontaRepository multicontaRepository,
                          AcessoGrupoMulticontaRepository acessoGrupoMulticontaRepository,
                          TokenEncryptorDecryptorService tokenEncryptorDecryptorService, DepartamentoService departamentoService, AcessoGrupoService acessoGrupoService, AcessoUsuarioRepository acessoUsuarioRepository) {
        this.empresaRepository = empresaRepository;
        this.preCadastroService = preCadastroService;
        this.multicontaRepository = multicontaRepository;
        this.acessoGrupoMulticontaRepository = acessoGrupoMulticontaRepository;
        this.departamentoService = departamentoService;
        this.acessoGrupoService = acessoGrupoService;
        this.acessoUsuarioRepository = acessoUsuarioRepository;
    }

    @Transactional
    public void cadastrarEmpresa(CadastroEmpresaRequest cadastroEmpresaRequest) throws IOException, ServiceException {

        if (cadastroEmpresaRequest.getDadosEmpresa().getCnpj() == null) {
            throw new ServiceException("CNPJ NULO");
        }

        Empresa empresa = empresaRepository.pegarEmpresaCnpj(cadastroEmpresaRequest.getDadosEmpresa().getCnpj());

        if (empresa == null) {
            Empresa novaEmpresa = new Empresa(cadastroEmpresaRequest.getDadosEmpresa());
            novaEmpresa.setDataCadastro(LocalDate.now());

            if (cadastroEmpresaRequest.getDadosEmpresa().getEndereco() != null) {
                Endereco novoEndereco = new Endereco(cadastroEmpresaRequest.getDadosEmpresa().getEndereco());
                novaEmpresa.setEndereco(novoEndereco);
            } else {
                novaEmpresa.setEndereco(new Endereco());
            }

            novaEmpresa = empresaRepository.save(novaEmpresa);

            Departamento novoDepartamento = this.departamentoService.pegarDepartamentoPorId(2);
            if (novoDepartamento == null) throw new ServiceException("Departamento padrão não encontrado");

            Long idUsuario = cadastroEmpresaRequest.getMulticonta().getIdUsuario();
            AcessoUsuario usuario = acessoUsuarioRepository.findById(idUsuario)
                    .orElseThrow(() -> new ServiceException("Usuário não encontrado para vincular a empresa"));

            MulticontaRequestDTO multicontaRequestDTO = cadastroEmpresaRequest.getMulticonta();
            MulticontaId multicontaId = new MulticontaId();
            multicontaId.setIdUsuario(idUsuario);
            multicontaId.setTipoPessoa(2);
            multicontaId.setIdSubjectus(novaEmpresa.getIdEmpresa());
            multicontaId.setIdDepartamento(novoDepartamento.getIdDepartamento());

            Multiconta multiconta = new Multiconta();
            multiconta.setMulticontaId(multicontaId);
            multiconta.setDepartamento(novoDepartamento);
            multiconta.setAcessoUsuario(usuario);
            this.multicontaRepository.save(multiconta);

            AcessoGrupo grupo = this.acessoGrupoService.buscarPorId(1L)
                    .orElseThrow(() -> new ServiceException("Grupo de acesso não encontrado"));

            AcessoGrupoMulticontaId acessoGrupoMulticontaId = new AcessoGrupoMulticontaId();
            acessoGrupoMulticontaId.setIdSubjectus(novaEmpresa.getIdEmpresa());
            acessoGrupoMulticontaId.setTipoPessoa(2);
            acessoGrupoMulticontaId.setIdDepartamento(novoDepartamento.getIdDepartamento());
            acessoGrupoMulticontaId.setIdGrupo(grupo.getIdGrupo());

            AcessoGrupoMulticonta acessoGrupoMulticonta = new AcessoGrupoMulticonta();
            acessoGrupoMulticonta.setId(acessoGrupoMulticontaId);
            acessoGrupoMulticonta.setAcessoGrupo(grupo);
            // acessoGrupoMulticonta.setDepartamento(novoDepartamento);

            this.acessoGrupoMulticontaRepository.save(acessoGrupoMulticonta);
        } else {
            throw new ServiceException("Empresa com este CNPJ já está cadastrada.");
        }
    }

    public Empresa pegarEmpresaPorId(Long idEmpresa) throws ServiceException {
        Optional<Empresa> empresa = empresaRepository.findById(idEmpresa);
        if (empresa.isPresent()) {
            return empresa.get();
        } else {
            throw new ServiceException("Essa empresa não existe");
        }
    }

    public Optional<Empresa> pegarEmpresaPorIdSistelcom(Long idEmpresa) {
        return empresaRepository.findById(idEmpresa);
    }

    public List<Empresa> pegarEmpresaMulticonta(MulticontaRequestDTO multicontaRequestDTO) {
        return empresaRepository.pegarEmpresaMulticonta(multicontaRequestDTO.getIdSubjectus());
    }

    @Transactional
    public void editarEmpresa(CadastroEmpresaRequest request) throws IOException {
        if (request.getDadosEmpresa() == null || request.getDadosEmpresa().getCnpj() == null) {
            throw new IllegalArgumentException("Dados da empresa ou CNPJ não fornecidos para edição.");
        }

        String cnpjAlvo = request.getDadosEmpresa().getCnpj();

        Empresa empresaExistente = empresaRepository.pegarEmpresaCnpj(cnpjAlvo);

        if (empresaExistente == null) {
            throw new EntityNotFoundException("Não foi encontrada nenhuma empresa com o CNPJ: " + cnpjAlvo);
        }

        CadastroEmpresaRequest.CadastroEmpresa dadosNovos = request.getDadosEmpresa();

        empresaExistente.setRazaoSocial(dadosNovos.getRazaoSocial());
        empresaExistente.setNomeFantasia(dadosNovos.getNomeFantasia());
        empresaExistente.setTelefone(dadosNovos.getTelefone());
        empresaExistente.setEmail(dadosNovos.getEmail());
        empresaExistente.setInscricaoEstadual(dadosNovos.getInscricaoEstadual());
        empresaExistente.setInscricaoMunicipal(dadosNovos.getInscricaoMunicipal());
        empresaExistente.setNaturezaJuridica(dadosNovos.getNaturezaJuridica());
        empresaExistente.setPorteEmpresa(dadosNovos.getPorteEmpresa());
        empresaExistente.setTipoEmpresa(dadosNovos.getTipoEmpresa());

        if (dadosNovos.getDataFundacao() != null) {
            empresaExistente.setDataFundacao(dadosNovos.getDataFundacao());
        }

        if (dadosNovos.getEndereco() != null) {
            CadastroEmpresaRequest.CadastroEmpresa.EnderecoEmpresa enderecoDTO = dadosNovos.getEndereco();
            Endereco enderecoEntidade = empresaExistente.getEndereco();

            if (enderecoEntidade == null) {
                enderecoEntidade = new Endereco();
            }

            enderecoEntidade.setLogradouro(enderecoDTO.getLogradouro());
            enderecoEntidade.setNumero(enderecoDTO.getNumero());
            enderecoEntidade.setComplemento(enderecoDTO.getComplemento());
            enderecoEntidade.setBairro(enderecoDTO.getBairro());
            enderecoEntidade.setCidade(enderecoDTO.getCidade());
            enderecoEntidade.setEstado(enderecoDTO.getEstado());
            enderecoEntidade.setPais(enderecoDTO.getPais());
            enderecoEntidade.setCep(enderecoDTO.getCep());

            empresaExistente.setEndereco(enderecoEntidade);
        }

        empresaRepository.save(empresaExistente);
    }

}