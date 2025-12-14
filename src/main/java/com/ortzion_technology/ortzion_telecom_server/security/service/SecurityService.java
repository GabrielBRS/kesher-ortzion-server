package com.ortzion_technology.ortzion_telecom_server.security.service;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Pessoa;
import com.ortzion_technology.ortzion_telecom_server.repository.padrao.cadastral.PessoaRepository;
import com.ortzion_technology.ortzion_telecom_server.security.dto.MulticontaRequestDTO;
import com.ortzion_technology.ortzion_telecom_server.security.dto.UsuarioLogadoDTO;
import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.security.exception.AcessoNaoAutorizadoException;
import com.ortzion_technology.ortzion_telecom_server.security.repository.AcessoUsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {

    private final AcessoUsuarioRepository acessoUsuarioRepository;
    private final PessoaRepository pessoaRepository;

    public SecurityService(AcessoUsuarioRepository acessoUsuarioRepository, PessoaRepository pessoaRepository) {
        this.acessoUsuarioRepository = acessoUsuarioRepository;
        this.pessoaRepository = pessoaRepository;
    }

    public AcessoUsuario getUsuarioLogado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return acessoUsuarioRepository.findByDocumentoUsuario(username).orElse(null);
    }

    public void verificarAcessoPermissoesMulticonta(AcessoUsuario usuarioAutenticado, MulticontaRequestDTO multicontaRequisitada) {

        if (multicontaRequisitada.getIdSubjectus().equals(usuarioAutenticado.getIdUsuario())) {
            return;
        }

        boolean pertenceAoUsuario = usuarioAutenticado.getMulticontas().stream()
                .anyMatch(mc ->
                        mc.getTipoPessoa().equals(multicontaRequisitada.getTipoPessoa()) &&
                                mc.getIdSubjectus().equals(multicontaRequisitada.getIdSubjectus())
                );

        if (!pertenceAoUsuario) {
            throw new AcessoNaoAutorizadoException("Acesso negado. A conta solicitada não pertence a este usuário.");
        }

    }

    public UsuarioLogadoDTO montarUsuarioLogadoDTO(AcessoUsuario usuario, Pessoa pessoa) {

        if (usuario == null) {
            return null;
        }

        Optional<Pessoa> pessoaOpt = pessoaRepository.findByDocumento(usuario.getDocumentoUsuario());

        UsuarioLogadoDTO.UsuarioLogadoDTOBuilder builder = UsuarioLogadoDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .documento(usuario.getDocumentoUsuario())
                .loginUnico(usuario.getDocumentoUsuario());

        if (pessoa != null) {
            builder.nome(pessoa.getNome())
                    .sobrenome(pessoa.getSobrenome())
                    .email(pessoa.getEmail())
                    .dataCadastro(pessoa.getDataCadastro() != null ? pessoa.getDataCadastro().toString() : null)
                    .dataNascimento(pessoa.getDataNascimento() != null ? pessoa.getDataNascimento().toString() : null);

            if (pessoa.getEndereco() != null) {
                builder.cep(pessoa.getEndereco().getCep())
                        .pais(pessoa.getEndereco().getPais())
                        .estado(pessoa.getEndereco().getEstado())
                        .cidade(pessoa.getEndereco().getCidade())
                        .logradouro(pessoa.getEndereco().getLogradouro())
                        .bairro(pessoa.getEndereco().getBairro())
                        .numero(pessoa.getEndereco().getNumero())
                        .complemento(pessoa.getEndereco().getComplemento());
            }
            if (pessoa.getContato() != null) {
                builder.countryCode(pessoa.getContato().getCountryCode())
                        .areaCode(pessoa.getContato().getAreaCode())
                        .telefone(pessoa.getContato().getNumber());
            }
        }

        return builder.build();

    }

}
