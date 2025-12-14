package com.ortzion_technology.ortzion_telecom_server.security.config;

import com.ortzion_technology.ortzion_telecom_server.configuration.ambiente.ConstanteVariavelAmbiente;
import com.ortzion_technology.ortzion_telecom_server.security.TwoA.JwtFilter;
import com.ortzion_technology.ortzion_telecom_server.security.service.IpCredenciadoApiPagarmeSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private ConstanteVariavelAmbiente constantes;

    private final IpCredenciadoApiPagarmeSecurityService ipCredenciadoApiPagarmeSecurityService;
    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;

    public SecurityConfig(IpCredenciadoApiPagarmeSecurityService ipCredenciadoApiPagarmeSecurityService, UserDetailsService userDetailsService, JwtFilter jwtFilter) {
        this.ipCredenciadoApiPagarmeSecurityService = ipCredenciadoApiPagarmeSecurityService;
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1)
    @Profile("prod")
    public SecurityFilterChain pagarmeWebhookSecurityFilterChainProd(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/webhooks/pagarme/**")
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().access((authentication, context) -> {
                            for (String ip : ipCredenciadoApiPagarmeSecurityService.getPagarmeIpWhitelist()) {
                                if (new IpAddressMatcher(ip).matches(context.getRequest())) {
                                    return new AuthorizationDecision(true);
                                }
                            }
                            return new AuthorizationDecision(false);
                        })
                )
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    @Order(1)
    @Profile("!prod")
    public SecurityFilterChain pagarmeWebhookSecurityFilterChainDev(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/webhooks/pagarme/**")
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/api/v1/public/**",
                                "/",
                                "/api/v1/pre-cadastro/cadastrar/**",
                                "/api/v1/pre-cadastro/atualizar/**",
                                "/api/v1/pre-cadastro/email-confirmado/**",
                                "/api/v1/pre-cadastro/pegar/confirmacao-cadastro/**",
                                "/api/v1/pessoa/cadastrar/**",
                                "/api/v1/cadastrar/**",
                                "/api/webhook/inter/**"
                        )
                        .permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        if ("dev".equals(constantes.getAmbiente())) {
            configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "https://localhost:4200", "http://kesher.ortzion.com.br", "https://kesher.ortzion.com.br","http://ortzion.com.br", "https://ortzion.com.br"));
        } else {
            configuration.setAllowedOrigins(Arrays.asList("http://kesher.ortzion.com.br", "https://kesher.ortzion.com.br","http://ortzion.com.br", "https://ortzion.com.br"));
        }

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "x-auth-token"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Disposition"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}