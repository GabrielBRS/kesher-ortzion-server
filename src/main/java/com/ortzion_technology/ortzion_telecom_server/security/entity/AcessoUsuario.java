package com.ortzion_technology.ortzion_telecom_server.security.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.ortzion_technology.ortzion_telecom_server.entity.domain.cadastral.Pessoa;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "acesso_usuario", schema = "security")
@SequenceGenerator(
        name = "seq_id_acesso_usuario_generator",
        sequenceName = "security.seq_id_acesso_usuario",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class AcessoUsuario implements UserDetails, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seq_id_acesso_usuario_generator")
    @Column(name = "id_usuario", unique = true, nullable = false)
    private Long idUsuario;

    @Column(name = "documento_usuario", unique = true, nullable = false)
    private String documentoUsuario;

    @Column(name = "email_usuario", unique = true, nullable = false)
    private String emailUsuario;

    @Column(name = "telefone_usuario", unique = true, nullable = false)
    private String telefoneUsuario;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(
//            name = "documento_usuario",
//            referencedColumnName = "documento",
//            insertable = false,
//            updatable = false
//    )
//    @JsonIgnore
//    private Pessoa pessoa;

    @Column(name = "senha_usuario")
    @JsonIgnore
    private String senhaUsuario;

    @Column(name="status_usuario")
    private Integer statusUsuario;

    @Column(name = "codigo_2fa_enabled", columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE")
    private boolean codigo2FAEnabled = false;

    @Column(name = "codigo_2fa")
    private String codigo2FA;

    @Column(name = "codigo_2fa_expiracao")
    private LocalDateTime codigo2FAExpiracao;

    @Column(name = "mfa_enabled", columnDefinition = "BOOLEAN NOT NULL DEFAULT FALSE")
    private boolean mfaEnabled = false;

    @Column(name = "mfa_secret")
    @JsonIgnore
    private String mfaSecret;

    @Column(name = "token_redefinicao")
    private String tokenRedefinicao;

    @Column(name = "token_redefinicao_expira_em")
    private LocalDateTime tokenRedefinicaoExpiraEm;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "acesso_usuario_recovery_codes",
            schema = "security",
            joinColumns = @JoinColumn(
                    name = "id_usuario",
                    referencedColumnName = "id_usuario"
            )
    )
    @Column(name = "recovery_code_hash")
    private Set<String> recoveryCodes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "acessoUsuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Multiconta> multicontas = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority("ROLE_PRE_SELECTION"));
    }

    @Override
    public String getPassword() {
        return this.senhaUsuario;
    }

    @Override
    public String getUsername() {
        return this.documentoUsuario;
    }

    @Override
    public boolean isEnabled() {
        return this.statusUsuario != null && this.statusUsuario != 0;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
