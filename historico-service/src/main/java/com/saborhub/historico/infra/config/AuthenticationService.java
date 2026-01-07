package com.historicosaude.historico.infra.config;

import com.historicosaude.historico.domain.enums.Perfil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationService {

    public boolean isAuthenticated() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean authenticated = authentication != null 
            && authentication.isAuthenticated() 
            && authentication.getPrincipal() instanceof CustomUserDetails;
        log.debug("isAuthenticated: {} | Authentication: {} | Principal type: {}", 
                  authenticated, 
                  authentication,
                  authentication != null ? authentication.getPrincipal().getClass().getSimpleName() : "null");
        return authenticated;
    }

    public Perfil getUserPerfil() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            Perfil perfil = ((CustomUserDetails) authentication.getPrincipal()).getPerfil();
            log.debug("getUserPerfil: {}", perfil);
            return perfil;
        }
        log.warn("Não conseguiu extrair perfil da autenticação");
        return null;
    }

    public String getUserLogin() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            String login = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
            log.debug("getUserLogin: {}", login);
            return login;
        }
        return null;
    }

    public String getUserCpf() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            String cpf = ((CustomUserDetails) authentication.getPrincipal()).getCpf();
            log.debug("getUserCpf: {}", cpf);
            return cpf;
        }
        return null;
    }

    public void validarAcesso(String... rolesPermitidos) {
        log.info("Validando acesso para roles: {}", String.join(", ", rolesPermitidos));
        
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("Authentication object: {} | Principal: {}", 
                  authentication != null ? authentication.getClass().getSimpleName() : "null",
                  authentication != null ? authentication.getPrincipal().getClass().getSimpleName() : "null");
        
        if (!isAuthenticated()) {
            log.warn("Acesso negado: Usuário não autenticado ou nenhum token JWT fornecido");
            throw new RuntimeException("Acesso negado: Forneça um token JWT válido no header Authorization (Bearer {token})");
        }

        var perfil = getUserPerfil();
        if (perfil == null) {
            log.warn("Acesso negado: Perfil do usuário não encontrado");
            throw new RuntimeException("Acesso negado: Perfil do usuário não encontrado no token JWT");
        }

        log.debug("Perfil do usuário: {}", perfil);

        for (String role : rolesPermitidos) {
            if (perfil.name().equals(role)) {
                log.info("Acesso permitido para perfil: {}", perfil);
                return;
            }
        }

        log.warn("Usuário com perfil {} não tem permissão. Roles permitidos: {}", 
                 perfil, String.join(", ", rolesPermitidos));
        throw new RuntimeException("Acesso negado: Usuário com perfil " + perfil + " não tem permissão para acessar este recurso. Roles permitidos: " + String.join(", ", rolesPermitidos));
    }
}
