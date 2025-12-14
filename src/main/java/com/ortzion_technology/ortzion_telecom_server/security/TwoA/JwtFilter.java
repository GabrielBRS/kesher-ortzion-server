package com.ortzion_technology.ortzion_telecom_server.security.TwoA;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        System.out.println("[JwtFilter TRACE] Recebida requisicao: " + request.getMethod() + " " + request.getRequestURI());

        try {
            final String path = request.getRequestURI();

            if (path.startsWith("/api/v1/auth/login") ||
                    path.startsWith("/api/v1/auth/verify-2fa") ||
                    path.startsWith("/api/v1/auth/solicitar-redefinicao-senha") ||
                    path.startsWith("/api/v1/public/") ||
                    path.equals("/") ||
                    path.startsWith("/api/v1/pre-cadastro/cadastrar") ||
                    path.startsWith("/api/v1/pre-cadastro/atualizar") ||
                    path.startsWith("/api/v1/pre-cadastro/email-confirmado") ||
                    path.startsWith("/api/v1/pre-cadastro/pegar/confirmacao-cadastro") ||
                    path.startsWith("/api/v1/pessoa/cadastrar") ||
                    path.startsWith("/api/webhooks/pagarme/")) {

                System.out.println("[JwtFilter TRACE] Rota publica, pulando filtro: " + path);
                chain.doFilter(request, response);
                return;
            }

            String authorizationHeader = request.getHeader("Authorization");

            String token = null;
            String username = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
                System.out.println("[JwtFilter TRACE] Token extraido: " + token.substring(0, 10) + "...");

                username = jwtUtil.extractUsername(token);
                System.out.println("[JwtFilter TRACE] Username extraido: " + username);

            } else {
                System.out.println("[JwtFilter TRACE] Nenhum header 'Authorization' Bearer encontrado.");
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                System.out.println("[JwtFilter TRACE] Carregando UserDetails para: " + username);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(token, userDetails)) {
                    System.out.println("[JwtFilter TRACE] Token VALIDADO. Setando contexto de seguranca.");
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    System.out.println("[JwtFilter TRACE] Token considerado INVALIDO.");
                }
            }

            System.out.println("[JwtFilter TRACE] Passando para o proximo filtro (chain.doFilter)");
            chain.doFilter(request, response);

        } catch (Throwable t) {
            System.err.println("### ERRO CRITICO NO JWTFILTER: " + t.getClass().getName() + " - " + t.getMessage());
            t.printStackTrace();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.setContentType("application/json");
            response.getWriter().write("{\"erro\": \"Erro no filtro de token: " + t.getMessage() + "\"}");
        }
    }
}