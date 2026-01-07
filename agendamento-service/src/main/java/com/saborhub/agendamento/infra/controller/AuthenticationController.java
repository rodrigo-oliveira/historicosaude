package com.historicosaude.agendamento.infra.controller;

import com.historicosaude.agendamento.application.dto.AutenticacaoDto;
import com.historicosaude.agendamento.application.dto.LoginResponseDto;
import com.historicosaude.agendamento.application.dto.LogoutResponseDto;
import com.historicosaude.agendamento.application.dto.RegistroUsuarioDto;
import com.historicosaude.agendamento.domain.enums.Perfil;
import com.historicosaude.agendamento.infra.config.TokenService;
import com.historicosaude.agendamento.infra.persistence.UsuarioEntity;
import com.historicosaude.agendamento.infra.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/autenticacao")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/entrar")
    public ResponseEntity<LoginResponseDto> entrar(@RequestBody AutenticacaoDto data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.senha());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((UsuarioEntity) auth.getPrincipal());
            return ResponseEntity.ok(new LoginResponseDto(token));
        } catch (Exception e) {
            System.out.println("Erro na autenticação: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/registrar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> registrar(@RequestBody RegistroUsuarioDto data) {
        try {
            // Validar se usuário já existe
            if (usuarioRepository.findByLogin(data.login()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login já existe");
            }
            if (usuarioRepository.findByEmail(data.email()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email já existe");
            }
            if (usuarioRepository.findByCpf(data.cpf()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CPF já existe");
            }

            // Criar novo usuário
            UsuarioEntity usuario = new UsuarioEntity();
            usuario.setCpf(data.cpf());
            usuario.setNome(data.nome());
            usuario.setEmail(data.email());
            usuario.setLogin(data.login());
            usuario.setSenha(passwordEncoder.encode(data.senha())); // Hash da senha
            usuario.setPerfil(Perfil.valueOf(data.perfil()));
            usuario.setDataUltimaAlteracao(java.time.ZonedDateTime.now());

            usuarioRepository.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário registrado com sucesso");
        } catch (Exception e) {
            System.out.println("Erro ao registrar: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao registrar usuário");
        }
    }
    
    @PostMapping("/sair")
    public ResponseEntity<LogoutResponseDto> sair(HttpServletRequest request) {
        String token = recoverToken(request);
        
        if (token != null && !token.isEmpty()) {
            tokenService.invalidateToken(token);
            return ResponseEntity.ok(new LogoutResponseDto("Logout realizado com sucesso"));
        }
        
        return ResponseEntity.badRequest().body(new LogoutResponseDto("Token não encontrado"));
    }
    
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}

