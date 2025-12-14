package com.ortzion_technology.ortzion_telecom_server.security.TwoA;

import com.ortzion_technology.ortzion_telecom_server.security.entity.AcessoUsuario;
import com.ortzion_technology.ortzion_telecom_server.security.repository.AcessoUsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AcessoUsuarioRepository acessoUsuarioRepository;

    public UserDetailsServiceImpl(AcessoUsuarioRepository acessoUsuarioRepository) {
        this.acessoUsuarioRepository = acessoUsuarioRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String documento) throws UsernameNotFoundException {
        Optional<AcessoUsuario> usuario = acessoUsuarioRepository.findByDocumentoUsuario(documento);

        if (usuario.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado com documento: " + documento);
        }

        return usuario.get();
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

}
