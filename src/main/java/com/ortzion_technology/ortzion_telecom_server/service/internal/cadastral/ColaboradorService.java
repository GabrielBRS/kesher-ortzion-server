package com.ortzion_technology.ortzion_telecom_server.service.internal.cadastral;

import com.google.protobuf.ServiceException;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CadastroColaboradorRequest;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.PermissoesColaboradorRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.*;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.GestaoEstoqueMercadoriaVirtualEmpresarial;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.cadastral.ColaboradorRepository;
import com.ortzion_technology.ortzion_telecom_server.security.criptografia.TokenEncryptorDecryptorService;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaResponseDTO;
import com.ortzion_technology.ortzion_telecom_server.security.entity.*;
import com.ortzion_technology.ortzion_telecom_server.security.io.email.EnvioEmailService;
import com.ortzion_technology.ortzion_telecom_server.security.repository.*;
import com.ortzion_technology.ortzion_telecom_server.security.service.AcessoGrupoMulticontaService;
import com.ortzion_technology.ortzion_telecom_server.security.service.AcessoGrupoService;
import com.ortzion_technology.ortzion_telecom_server.security.service.AcessoUsuarioService;
import com.ortzion_technology.ortzion_telecom_server.security.service.MulticontaService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual.EstoqueMercadoriaVirtualService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual.GestaoEstoqueMercadoriaVirtualEmpresarialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;
    private final EnvioEmailService envioEmailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AcessoGrupoService acessoGrupoService;
    private final TokenEncryptorDecryptorService tokenEncryptorDecryptorService;
    private final MulticontaService multicontaService;
    private final AcessoGrupoMulticontaService acessoGrupoMulticontaService;
    private final AcessoUsuarioService acessoUsuarioService;
    private final DepartamentoService departamentoService;
    private final PessoaService pessoaService;
    private final EstoqueMercadoriaVirtualService estoqueMercadoriaVirtualService;
    private final EmpresaService empresaService;
    private final GestaoEstoqueMercadoriaVirtualEmpresarialService gestaoEstoqueMercadoriaVirtualEmpresarialService;

    @Autowired
    public ColaboradorService(ColaboradorRepository colaboradorRepository, EnvioEmailService envioEmailService, BCryptPasswordEncoder bCryptPasswordEncoder, AcessoGrupoRepository acessoGrupoRepository, AcessoGrupoService acessoGrupoService, TokenEncryptorDecryptorService tokenEncryptorDecryptorService, MulticontaService multicontaService, AcessoGrupoMulticontaService acessoGrupoMulticontaService, AcessoUsuarioService acessoUsuarioService, DepartamentoService departamentoService, PessoaService pessoaService, EstoqueMercadoriaVirtualService estoqueMercadoriaVirtualService, EmpresaService empresaService, GestaoEstoqueMercadoriaVirtualEmpresarialService gestaoEstoqueMercadoriaVirtualEmpresarialService) {
        this.colaboradorRepository = colaboradorRepository;
        this.envioEmailService = envioEmailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.acessoGrupoService = acessoGrupoService;
        this.tokenEncryptorDecryptorService = tokenEncryptorDecryptorService;
        this.multicontaService = multicontaService;
        this.acessoGrupoMulticontaService = acessoGrupoMulticontaService;
        this.acessoUsuarioService = acessoUsuarioService;
        this.departamentoService = departamentoService;
        this.pessoaService = pessoaService;
        this.estoqueMercadoriaVirtualService = estoqueMercadoriaVirtualService;
        this.empresaService = empresaService;
        this.gestaoEstoqueMercadoriaVirtualEmpresarialService = gestaoEstoqueMercadoriaVirtualEmpresarialService;
    }

    public Optional<Colaborador> pegarColaboradorUsuarioLogado(AcessoUsuario usuario){
        return colaboradorRepository.pegarColaboradorPorDocumento(usuario.getDocumentoUsuario());
    }

    public Optional<Colaborador> pegarColaborador(String documento){
        return colaboradorRepository.pegarColaboradorPorDocumento(documento);
    }

    /**
     * MÉTODO CORRIGIDO
     * Recebe o DTO, extrai o idEmpresa (idSubjectus) e filtra a busca.
     */
    public List<Colaborador> pegarTodosColaboradores(MulticontaRequestDTO multiconta) {

        // Extrai o ID da empresa logada (idSubjectus)
        Long idEmpresa = multiconta.getIdSubjectus();

        if (idEmpresa == null) {
            // Se não houver ID da empresa, não retorna nada por segurança.
            return Collections.emptyList();
        }

        // Chama o novo método do repositório que filtra por ID da empresa
        return colaboradorRepository.findAllByEmpresa_IdEmpresa(idEmpresa);
    }

    public void cadastrarColaborador(CadastroColaboradorRequest colaboradorRequest) throws ServiceException {

        String documentoColaborador = colaboradorRequest.getColaborador().getDocumentoColaborador();

        Optional<Colaborador> colaboradorExistente = colaboradorRepository.pegarColaboradorPorDocumento(documentoColaborador);
        if (colaboradorExistente.isPresent()) {
            throw new ServiceException("O colaborador já está cadastrado");
        }

        Empresa empresa = this.empresaService.pegarEmpresaPorId(colaboradorRequest.getEmpresa().getIdEmpresa());

        Pessoa novaPessoa = this.pessoaService.cadastrarPessoa(colaboradorRequest);

        Colaborador colaborador =  new Colaborador(colaboradorRequest.getColaborador());
        colaborador.setPessoa(novaPessoa);
        // Associa a empresa ao colaborador no momento do cadastro
        colaborador.setEmpresa(empresa);

        colaborador = colaboradorRepository.save(colaborador);

        AcessoUsuario acessoUsuario = this.acessoUsuarioService.criarAcessoUsuario(novaPessoa);

        Departamento departamento = this.departamentoService.atribuirAoColaboradorDepartamento(colaboradorRequest.getDepartamento());

        Multiconta multiconta = this.multicontaService.criarMulticontaColaborador(acessoUsuario, novaPessoa, departamento, empresa, colaboradorRequest.getMulticontaColaborador());

        List<AcessoGrupo> novasRoles = this.acessoGrupoService.atribuirAcessoGrupoDaEmpresaAoColaborador(colaboradorRequest);

        Set<AcessoGrupoMulticonta> acessoGrupoMulticonta = this.acessoGrupoMulticontaService.criarAcessoGrupoMulticonta(novasRoles, multiconta);

        multiconta.setAcessoGrupoMulticonta(acessoGrupoMulticonta);

        multiconta = this.multicontaService.atualizar(multiconta);

        List<GestaoEstoqueMercadoriaVirtualEmpresarial> gestaoEstoqueMercadoriaVirtualEmpresarialList = this.gestaoEstoqueMercadoriaVirtualEmpresarialService
                .atribuirGestaoEstoqueEmpresaAoColaborador(acessoUsuario, novaPessoa, empresa, departamento, colaborador);

        acessoUsuario = this.acessoUsuarioService.criarMulticonta(multiconta);
    }

    public void atualizarColaborador(CadastroColaboradorRequest colaboradorRequest) throws ServiceException {

        String documentoColaborador = colaboradorRequest.getColaborador().getDocumentoColaborador();

        Optional<Colaborador> colaboradorExistente = colaboradorRepository.pegarColaboradorPorDocumento(documentoColaborador);
        if (colaboradorExistente.isPresent()) {
            throw new ServiceException("O colaborador já está cadastrado");
        }
    }

    public void permissoesAcessosColaborador(PermissoesColaboradorRequest permissoesColaborador) throws ServiceException {

        String documentoColaborador = permissoesColaborador.getColaborador().getDocumentoColaborador();

        Optional<Colaborador> colaboradorExistente = colaboradorRepository.pegarColaboradorPorDocumento(documentoColaborador);
        if (!colaboradorExistente.isPresent()) {
            throw new ServiceException("O colaborador não está cadastrado");
        }

        Empresa empresa = this.empresaService.pegarEmpresaPorId(permissoesColaborador.getEmpresa().getIdEmpresa());

        Departamento departamento = this.departamentoService.pegarDepartamentoPorId(permissoesColaborador.getDepartamento().getIdDepartamento());

        Colaborador colaborador = colaboradorExistente.get();

        AcessoUsuario acessoUsuario = this.acessoUsuarioService.buscarAcessoUsuario(colaborador.getPessoa().getDocumento());

        Multiconta multiconta = this.multicontaService.buscarMulticontaColaboradorEAssociar(acessoUsuario, 2, departamento, empresa, permissoesColaborador.getMulticontaColaborador());

        List<AcessoGrupo> novasRoles = this.acessoGrupoService.buscarAcessoGrupoDaEmpresa(permissoesColaborador.getAcessoGrupo());

        Set<AcessoGrupoMulticonta> acessoGrupoMulticonta = this.acessoGrupoMulticontaService.criarAcessoGrupoMulticonta(novasRoles, multiconta);

        multiconta.setAcessoGrupoMulticonta(acessoGrupoMulticonta);

        multiconta = this.multicontaService.atualizar(multiconta);

        acessoUsuario = this.acessoUsuarioService.criarMulticonta(multiconta);
    }

    /**
     * MÉTODO CORRIGIDO
     * Extrai o documento do PessoaVO (vindo do DTO da multiconta)
     * e busca os colaboradores usando o método 'pegarColaborador'
     */
    public List<Colaborador> pegarColaboradoresPorDepartamento(PermissoesColaboradorRequest request) throws ServiceException {

        Integer tipoPessoa = 2;
        Long idEmpresa = request.getEmpresa().getIdEmpresa();
        Integer idDepartamento = request.getDepartamento().getIdDepartamento();

        if (idEmpresa == null || idDepartamento == null) {
            throw new ServiceException("ID da Empresa e ID do Departamento são obrigatórios.");
        }

        List<MulticontaResponseDTO> multicontas = multicontaService.buscarPorEmpresaEDepartamento(tipoPessoa, idEmpresa, idDepartamento);

        List<String> documentos = multicontas.stream()
                .filter(dto -> dto.getPessoaVO() != null && dto.getPessoaVO().getDocumento() != null)
                .map(dto -> dto.getPessoaVO().getDocumento())
                .distinct()
                .collect(Collectors.toList());

        // Implementação robusta que reutiliza seu método 'pegarColaborador'
        return documentos.stream()
                .map(this::pegarColaborador)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public void desassociarColaboradorDepartamento(PermissoesColaboradorRequest request) throws ServiceException {

        String documentoColaborador = request.getColaborador().getDocumentoColaborador();
        Integer tipoPessoa = 2;
        Long idEmpresa = request.getEmpresa().getIdEmpresa();
        Integer idDepartamento = request.getDepartamento().getIdDepartamento();

        if (documentoColaborador == null || idEmpresa == null || idDepartamento == null) {
            throw new ServiceException("Documento, ID da Empresa e ID do Departamento são obrigatórios.");
        }

        AcessoUsuario acessoUsuario = acessoUsuarioService.buscarAcessoUsuario(documentoColaborador);
        if (acessoUsuario == null) {
            throw new ServiceException("Usuário de Acesso não encontrado para o documento: " + documentoColaborador);
        }

        multicontaService.desassociarMulticonta(acessoUsuario.getIdUsuario(), tipoPessoa, idEmpresa, idDepartamento);
    }
}
