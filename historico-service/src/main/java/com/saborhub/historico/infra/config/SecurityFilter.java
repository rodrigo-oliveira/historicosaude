package com.historicosaude.historico.infra.config;

import com.historicosaude.historico.domain.entities.Usuario;
import com.historicosaude.historico.infra.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired(required = false)
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = recoverToken(request);
        
        log.debug("Método: {}, Path: {}, Token presente: {}", request.getMethod(), request.getRequestURI(), token != null);

        if (token != null) {
            try {
                log.debug("Validando token: {}", token.substring(0, Math.min(20, token.length())) + "...");
                if (tokenService.validateToken(token)) {
                    var login = tokenService.getLoginFromToken(token);
                    var perfilStr = tokenService.getPerfilFromToken(token);
                    var userId = tokenService.getUserIdFromToken(token);
                    
                    log.info("Token válido - Login: {}, Perfil: {}, UserId: {}", login, perfilStr, userId);
                    
                    var perfil = com.historicosaude.historico.domain.enums.Perfil.valueOf(perfilStr);
                    
                    String cpf = "";
                    if (usuarioRepository != null) {
                        Optional<Usuario> usuario = usuarioRepository.findById(userId);
                        cpf = usuario.map(Usuario::getCpf).orElse("");
                        log.debug("Usuário encontrado: CPF: {}", cpf);
                    }
                    
                    var userDetails = new CustomUserDetails(login, "", perfil, userId, cpf);
                    var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.info("Autenticação definida para usuário: {}", login);
                } else {
                    log.warn("Token inválido ou expirado");
                }
            } catch (Exception e) {
                log.error("Erro ao processar token", e);
            }
        } else {
            log.debug("Nenhum token encontrado no header Authorization");
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        log.debug("Header Authorization: {}", authHeader);
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.debug("Token extraído do header");
            return token;
        }
        
        log.debug("Nenhum header Authorization com Bearer encontrado");
        return null;
    }
}
