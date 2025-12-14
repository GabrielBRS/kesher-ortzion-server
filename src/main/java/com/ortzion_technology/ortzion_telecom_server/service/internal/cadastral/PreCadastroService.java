package com.ortzion_technology.ortzion_telecom_server.service.internal.cadastral;

import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.AtualizacaoPreCadastroDTO;
import com.ortzion_technology.ortzion_telecom_server.controller.v1.dto.request.internal.PreCadastroRequest;
import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.PreCadastro;
import com.ortzion_technology.ortzion_telecom_server.security.entity.Multiconta;
import com.ortzion_technology.ortzion_telecom_server.security.entity.MulticontaId;
import com.ortzion_technology.ortzion_telecom_server.security.enums.AcessoUsuarioEnum;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.entity.enums.cadastral.CadastroEnum;
import com.ortzion_technology.ortzion_telecom_server.entity.enums.cadastral.PreCadastroEnum;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.cadastral.PreCadastroRepository;
import com.ortzion_technology.ortzion_telecom_server.security.repository.AcessoGrupoFuncionalidadeRepository;
import com.ortzion_technology.ortzion_telecom_server.security.repository.AcessoGrupoRepository;
import com.ortzion_technology.ortzion_telecom_server.security.repository.AcessoUsuarioRepository;
import com.ortzion_technology.ortzion_telecom_server.security.io.email.EnvioEmailService;
import com.ortzion_technology.ortzion_telecom_server.security.service.MulticontaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class PreCadastroService {

    private final PreCadastroRepository preCadastroRepository;

    private final AcessoUsuarioRepository acessoUsuarioRepository;

    private final AcessoGrupoRepository acessoGrupoRepository;

//    private final AcessoGrupoMulticontaRepository acessoGrupoUsuarioRepository;

    private final AcessoGrupoFuncionalidadeRepository acessoGrupoFuncionalidadeRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final EnvioEmailService envioEmailService;

    private final MulticontaService multicontaService;

    @Autowired
    public PreCadastroService(PreCadastroRepository preCadastroRepository, AcessoUsuarioRepository acessoUsuarioRepository, AcessoGrupoRepository acessoGrupoRepository, BCryptPasswordEncoder bCryptPasswordEncoder, EnvioEmailService envioEmailService, AcessoGrupoFuncionalidadeRepository acessoGrupoFuncionalidadeRepository, MulticontaService multicontaService) {
        this.preCadastroRepository = preCadastroRepository;
        this.acessoUsuarioRepository = acessoUsuarioRepository;
        this.acessoGrupoRepository = acessoGrupoRepository;
        this.acessoGrupoFuncionalidadeRepository = acessoGrupoFuncionalidadeRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.envioEmailService = envioEmailService;
        this.multicontaService = multicontaService;
    }

    public PreCadastro pegarPrecadastro(AcessoUsuario usuario) {

//        PreCadastro preCadastro = null;
//
//        if(tipoPessoa != null && idSubjectus != null && tipoPessoa != 0 && idSubjectus != 0) {
//            preCadastro = this.preCadastroRepository.getPreCadastroByTipoPreCadastroAndIdPreCadastroOrDocumento(tipoPessoa, idSubjectus, usuario.getDocumentoUsuario());
//        }else{
//            preCadastro = this.preCadastroRepository.getPreCadastroByDocumento(usuario.getDocumentoUsuario());
//        }

        return this.preCadastroRepository.getPreCadastroByDocumento(usuario.getDocumentoUsuario());

    }

    public PreCadastro pegarPrecadastro(String documento) {

        PreCadastro preCadastro = this.preCadastroRepository.pegarPreCadastroPorDocumento(documento);

        return preCadastro;

    }

    public PreCadastro cadastrar(PreCadastroRequest preCadastroRequest) {

        PreCadastro preCadastro = this.preCadastroRepository.pegarPreCadastroPorEmail(preCadastroRequest.getEmail());

        if (preCadastro == null) {
            PreCadastro novoPreCadastro = new PreCadastro(preCadastroRequest);

            novoPreCadastro.setStatusPreCadastro(PreCadastroEnum.PROCESSANDO.ordinal());
            novoPreCadastro.setDataPreCadastro(LocalDateTime.now());

            return preCadastroRepository.save(novoPreCadastro);
        }else {
            return preCadastro;
        }

    }

    public Optional<PreCadastro> atualizar(PreCadastroRequest preCadastroRequest, AtualizacaoPreCadastroDTO peticao) {
        return Optional.ofNullable(preCadastroRepository.pegarPreCadastroPorEmail(preCadastroRequest.getEmail()))
                .map(preCadastro -> {
                    preCadastro.setStatusPreCadastro(preCadastroRequest.getStatusPreCadastro());
                    preCadastro.setDataAlteracaoPreCadastro(LocalDateTime.now());
                    return preCadastroRepository.save(preCadastro);
                });
    }

    public PreCadastro atualizar(PreCadastro preCadastro) {
        return preCadastroRepository.save(preCadastro);
    }

//    @Async("taskExecutorSMS")
    public void pegarPreCadastroPorEmail(String email) throws IOException {

        PreCadastro preCadastro = this.preCadastroRepository.pegarPreCadastroPorEmail(email);

        String senhaProvisoria = gerarSenhaAleatoria();
        String senhaCriptografada = bCryptPasswordEncoder.encode(senhaProvisoria);

        Optional<AcessoUsuario> acessoUsuario = this.acessoUsuarioRepository.findByDocumentoUsuario(preCadastro.getDocumento());

        if(acessoUsuario.isEmpty()) {
            String telefoneCodigo = preCadastro.getTelefoneCodigoArea();
            String telefone = preCadastro.getTelefone();

            AcessoUsuario novoAcessoUsuario = new AcessoUsuario();
            novoAcessoUsuario.setDocumentoUsuario(preCadastro.getDocumento());
            novoAcessoUsuario.setSenhaUsuario(senhaCriptografada);
            novoAcessoUsuario.setStatusUsuario(AcessoUsuarioEnum.PRE_CADRASTO_CONFIRMADO.getCodigoNumerico());
            novoAcessoUsuario.setEmailUsuario(preCadastro.getEmail());
            novoAcessoUsuario.setTelefoneUsuario(telefoneCodigo+telefone);
            novoAcessoUsuario.setMfaEnabled(false);
            novoAcessoUsuario.setCodigo2FAEnabled(false);

            acessoUsuarioRepository.save(novoAcessoUsuario);

            envioEmailService.enviarEmailPrimeiroLoginSenhaProvisoria(preCadastro, senhaProvisoria);
        }else{
            acessoUsuario.get().setSenhaUsuario(senhaCriptografada);
            acessoUsuarioRepository.save(acessoUsuario.get());
            envioEmailService.enviarEmailPrimeiroLoginSenhaProvisoria(preCadastro, senhaProvisoria);
        }

    }

    public PreCadastro pegarPreCadastroCnpj(String documento) throws IOException {

        PreCadastro preCadastro = this.preCadastroRepository.pegarPreCadastroPorCnpj(documento);

        String senhaProvisoria = gerarSenhaAleatoria();
        String senhaCriptografada = bCryptPasswordEncoder.encode(senhaProvisoria);

        Optional<AcessoUsuario> acessoUsuario = this.acessoUsuarioRepository.findByDocumentoUsuario(preCadastro.getDocumento());

        if(!acessoUsuario.isPresent()) {
            AcessoUsuario novoAcessoUsuario = new AcessoUsuario();
            novoAcessoUsuario.setDocumentoUsuario(preCadastro.getDocumento());
            novoAcessoUsuario.setSenhaUsuario(senhaCriptografada);
            novoAcessoUsuario.setStatusUsuario(CadastroEnum.ATIVO.ordinal());

            acessoUsuarioRepository.save(novoAcessoUsuario);

            envioEmailService.enviarEmailPrimeiroLoginSenhaProvisoria(preCadastro, senhaProvisoria);

        }else{
            acessoUsuario.get().setSenhaUsuario(senhaCriptografada);
            acessoUsuarioRepository.save(acessoUsuario.get());
            envioEmailService.enviarEmailPrimeiroLoginSenhaProvisoria(preCadastro, senhaProvisoria);
        }

        return preCadastro;
    }

    public PreCadastro pegarPreCadastroPorDocumento(String documento) {
        return this.preCadastroRepository.getPreCadastroByDocumento(documento);
    }

    private String gerarSenhaAleatoria() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }


}
