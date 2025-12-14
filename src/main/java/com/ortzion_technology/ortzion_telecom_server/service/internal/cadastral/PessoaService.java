package com.ortzion_technology.ortzion_telecom_server.service.internal.cadastral;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CadastroColaboradorRequest;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.CadastroPessoaRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.analytics.Dashboard;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.*;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.CanalMensageria;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.mercado_virtual.EstoqueMercadoriaVirtual;
import com.ortzion_technology.ortzion_telecom_server.entity.enums.cadastral.RolesEnum;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import com.ortzion_technology.ortzion_telecom_server.security.entity.*;
import com.ortzion_technology.ortzion_telecom_server.security.enums.AcessoUsuarioEnum;
import com.ortzion_technology.ortzion_telecom_server.security.enums.MulticontaEnum;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.cadastral.PessoaRepository;
import com.ortzion_technology.ortzion_telecom_server.security.repository.*;
import com.ortzion_technology.ortzion_telecom_server.service.internal.analytics.DashboardService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual.CanalMensageriaService;
import com.ortzion_technology.ortzion_telecom_server.service.internal.mercado_virtual.EstoqueMercadoriaVirtualService;
import com.ortzion_technology.ortzion_telecom_server.utils.DocumentoUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.rmi.ServerException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final PreCadastroService preCadastroService;
    private final AcessoUsuarioRepository acessoUsuarioRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MulticontaRepository multicontaRepository;
    private final CanalMensageriaService  canalMensageriaService;
    private final EstoqueMercadoriaVirtualService  estoqueMercadoriaVirtualService;
    private final DashboardService  dashboardService;
    private final AcessoGrupoRepository acessoGrupoRepository;
    private final DepartamentoRepository departamentoRepository;
    private final AcessoGrupoMulticontaRepository acessoGrupoMulticontaRepository;
    private final EnderecoService enderecoService;
    private final ContatoService contatoService;

    @Autowired
    public PessoaService(PessoaRepository pessoaRepository, PreCadastroService preCadastroService, AcessoUsuarioRepository acessoUsuarioRepository, BCryptPasswordEncoder bCryptPasswordEncoder, MulticontaRepository multicontaRepository, CanalMensageriaService canalMensageriaService, EstoqueMercadoriaVirtualService estoqueMercadoriaVirtualService, DashboardService dashboardService, AcessoGrupoRepository acessoGrupoRepository, DepartamentoRepository departamentoRepository, AcessoGrupoMulticontaRepository acessoGrupoMulticontaRepository, EnderecoService enderecoService, ContatoService contatoService) {
        this.pessoaRepository = pessoaRepository;
        this.preCadastroService = preCadastroService;
        this.acessoUsuarioRepository = acessoUsuarioRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.multicontaRepository = multicontaRepository;
        this.canalMensageriaService = canalMensageriaService;
        this.estoqueMercadoriaVirtualService = estoqueMercadoriaVirtualService;
        this.dashboardService = dashboardService;
        this.acessoGrupoRepository = acessoGrupoRepository;
        this.departamentoRepository = departamentoRepository;
        this.acessoGrupoMulticontaRepository = acessoGrupoMulticontaRepository;
        this.enderecoService = enderecoService;
        this.contatoService = contatoService;
    }

    public Pessoa pegarPessoaUsuarioLogado(AcessoUsuario usuario){
        return pessoaRepository.pegarPessoaPorDocumento(usuario.getDocumentoUsuario());
    }

    public Pessoa buscarPessoaPorConta(MulticontaRequestDTO multiconta, AcessoUsuario usuario){
        return pessoaRepository.pegarPessoaPorDocumento(usuario.getDocumentoUsuario());
    }

    public Pessoa pegarPessoaPorDocumento(String documento){
        return pessoaRepository.pegarPessoaPorDocumento(documento);
    }

    public Optional<Pessoa> buscarPessoaPorIdSistelcom(Long idPessoa){
        return this.pessoaRepository.findById(idPessoa);
    }

    @Transactional
    public void cadastrarPessoa(CadastroPessoaRequest cadastroPessoaRequest) throws ServerException {

        Optional<Pessoa> pessoaExistente = pessoaRepository.findByDocumento(cadastroPessoaRequest.getDocumento());
        if (pessoaExistente.isPresent()) {
            throw new ServerException("Esta pessoa já existe");
        }
        String documentoTratado = Optional.ofNullable(DocumentoUtils.desformatadorDocumento(cadastroPessoaRequest.getDocumento())).orElse(null);

        AcessoUsuario acessoUsuario = this.acessoUsuarioRepository.findByDocumentoUsuario(documentoTratado)
                .orElseThrow(() -> new IllegalStateException("AcessoUsuario não encontrado para o pré-cadastro com documento: " + documentoTratado));

        Pessoa novaPessoa = new Pessoa(cadastroPessoaRequest);
        PreCadastro preCadastro = this.preCadastroService.pegarPreCadastroPorDocumento(documentoTratado);

        novaPessoa.setDataCadastro(LocalDate.now());
        novaPessoa.setPreCadastro(preCadastro);

        acessoUsuario.setSenhaUsuario(bCryptPasswordEncoder.encode(cadastroPessoaRequest.getSenhaNova()));
        acessoUsuario.setStatusUsuario(AcessoUsuarioEnum.ATIVO.getCodigoNumerico());
        acessoUsuario = this.acessoUsuarioRepository.save(acessoUsuario);

        preCadastro.setDataAlteracaoPreCadastro(LocalDateTime.now());
        preCadastro = this.preCadastroService.atualizar(preCadastro);

        novaPessoa = pessoaRepository.save(novaPessoa);

        Contato contato = new Contato();
        contato.setCountryCode(cadastroPessoaRequest.getTelefoneCodigoPais());
        contato.setAreaCode(cadastroPessoaRequest.getTelefoneCodigoArea());
        contato.setNumber(cadastroPessoaRequest.getTelefone());
        novaPessoa.setContato(contato);

        Endereco endereco = new Endereco();
        endereco.setPais(cadastroPessoaRequest.getPais());
        endereco.setEstado(cadastroPessoaRequest.getEstado());
        endereco.setCidade(cadastroPessoaRequest.getCidade());
        endereco.setLogradouro(cadastroPessoaRequest.getLogradouro());
        endereco.setBairro(cadastroPessoaRequest.getBairro());
        endereco.setNumero(cadastroPessoaRequest.getNumero());
        endereco.setComplemento(cadastroPessoaRequest.getComplemento());
        endereco.setCep(cadastroPessoaRequest.getCep());
        novaPessoa.setEndereco(endereco);

        Optional<Departamento> departamentoOptional = this.departamentoRepository.findByIdDepartamento(1);
        Departamento departamento = new Departamento();
        if(departamentoOptional.isEmpty()) {
            departamento.setIdDepartamento(1);
            departamento.setCodigoDepartamento("DPP1");
            departamento.setNomeDepartamento("DEPARTAMENTO PESSOA FÍSICA");
            departamento = departamentoRepository.save(departamento);
        }else{
            departamento =  departamentoOptional.get();
        }

        Multiconta multiconta = new Multiconta();
        MulticontaId multicontaId = new MulticontaId();

        multicontaId.setIdUsuario(acessoUsuario.getIdUsuario());
        multicontaId.setTipoPessoa(1);
        multicontaId.setIdSubjectus(novaPessoa.getIdPessoa());
        multicontaId.setIdDepartamento(departamento.getIdDepartamento());

        multiconta.setMulticontaId(multicontaId);
        multiconta.setIdSubjectus(novaPessoa.getIdPessoa());
        multiconta.setDepartamento(departamento);
        multiconta.setStatusMulticonta(MulticontaEnum.ATIVO.getCodigoNumerico());
        multiconta.setAcessoUsuario(acessoUsuario);
        multiconta.setEmail(novaPessoa.getEmail());
        multiconta.setTelefone(contato.getAreaCode()+contato.getNumber());
        multiconta.setUsername(novaPessoa.getNome());

        multiconta = this.multicontaRepository.save(multiconta);

        AcessoGrupo novaRole = this.acessoGrupoRepository.getByIdGrupo(RolesEnum.ROLE_USER.getCodigoNumerico());

        AcessoGrupoMulticontaId relacaoId = new AcessoGrupoMulticontaId(
                novaRole.getIdGrupo(),
                multiconta.getMulticontaId().getIdUsuario(),
                multiconta.getMulticontaId().getTipoPessoa(),
                multiconta.getMulticontaId().getIdSubjectus(),
                multiconta.getMulticontaId().getIdDepartamento()
        );

        AcessoGrupoMulticonta acessoGrupoMulticonta = new AcessoGrupoMulticonta();
        acessoGrupoMulticonta.setAcessoGrupo(novaRole);
        acessoGrupoMulticonta.setMulticonta(multiconta);
        acessoGrupoMulticonta.setId(relacaoId);
        acessoGrupoMulticonta = this.acessoGrupoMulticontaRepository.save(acessoGrupoMulticonta);

        multiconta.getAcessoGrupoMulticonta().add(acessoGrupoMulticonta);

        multiconta = this.multicontaRepository.save(multiconta);

        List<CanalMensageria> canalMensagerias = this.canalMensageriaService.pegarTodosCanaisMensageria();
        for(CanalMensageria  canalMensageria : canalMensagerias) {
            EstoqueMercadoriaVirtual novoEstoqueMercadoriaVirtual = new EstoqueMercadoriaVirtual();
            novoEstoqueMercadoriaVirtual.setTipoPessoa(1);
            novoEstoqueMercadoriaVirtual.setIdSubjectus(novaPessoa.getIdPessoa());
            novoEstoqueMercadoriaVirtual.setIdDepartamento(departamento.getIdDepartamento());
            novoEstoqueMercadoriaVirtual.setCanalMensageria(canalMensageria);
            novoEstoqueMercadoriaVirtual.setQuantidadeEstoqueCanalMensageria(0L);
            this.estoqueMercadoriaVirtualService.criarEstoque(novoEstoqueMercadoriaVirtual);

            Dashboard dashboard = new Dashboard();
            dashboard.setTipoPessoa(1);
            dashboard.setIdSubjectus(novaPessoa.getIdPessoa());
            dashboard.setIdDepartamento(departamento.getIdDepartamento());
            dashboard.setIdCanalMensageria(canalMensageria.getIdCanalMensageria());
            dashboard.setDisponivel(novoEstoqueMercadoriaVirtual.getQuantidadeEstoqueCanalMensageria());
            dashboard = this.dashboardService.salvarDashboard(dashboard);
        }

        LGPD lgpd = new LGPD();
        lgpd.setAutorizacaoPosseDados(cadastroPessoaRequest.getAutorizacaoPosseDados());
        lgpd.setAutorizacaoPosseDadosTerceiros(cadastroPessoaRequest.getAutorizacaoPosseDadosTerceiros());
        novaPessoa.setLgpd(lgpd);

        pessoaRepository.save(novaPessoa);

    }

    public Pessoa cadastrarPessoa(CadastroColaboradorRequest colaboradorRequest) {

        Pessoa pessoa = this.pessoaRepository.pegarPessoaPorDocumento(colaboradorRequest.getPessoa().getDocumento());

        if(pessoa == null){

            Pessoa novaPessoa = new Pessoa(colaboradorRequest.getPessoa());
            novaPessoa.setDataCadastro(LocalDate.now());

            Contato contato = this.contatoService.salvarContato(new Contato(colaboradorRequest.getContatoPessoa()));
            novaPessoa.setContato(contato);

            Endereco endereco = this.enderecoService.salvarEndereco(new Endereco(colaboradorRequest.getEnderecoPessoa()));
            novaPessoa.setEndereco(endereco);

            LGPD lgpd = new LGPD();
            lgpd.setAutorizacaoPosseDados(true);
            lgpd.setAutorizacaoPosseDadosTerceiros(true);
            novaPessoa.setLgpd(lgpd);

            novaPessoa = pessoaRepository.save(novaPessoa);

            return novaPessoa;
        }else{
            return pessoa;
        }

    }

}
