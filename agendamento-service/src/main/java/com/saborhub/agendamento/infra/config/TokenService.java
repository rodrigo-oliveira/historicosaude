package com.historicosaude.agendamento.infra.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.historicosaude.agendamento.infra.persistence.UsuarioEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Service
public class TokenService {
    
    @Value("${api.security.token.secret}")
    private String secret;
    
    @Value("${api.security.token.expiration-time:86400000}")
    private long expirationTime;

    private static final Set<String> invalidTokens = new HashSet<>();

    public String generateToken(UsuarioEntity usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("historicosaude-agendamento")
                    .withSubject(usuario.getLogin())
                    .withClaim("id", usuario.getId())
                    .withClaim("perfil", usuario.getPerfil().toString())
                    .withExpiresAt(generateExpiresAt())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            if (invalidTokens.contains(token)) {
                return null;
            }
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("historicosaude-agendamento")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    public void invalidateToken(String token) {
        invalidTokens.add(token);
    }

    private Instant generateExpiresAt() {
        return Instant.now().plus(expirationTime, ChronoUnit.MILLIS);
    }
}
