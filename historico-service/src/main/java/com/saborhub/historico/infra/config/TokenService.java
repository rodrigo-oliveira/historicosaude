package com.historicosaude.historico.infra.config;

import com.historicosaude.historico.domain.enums.Perfil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Value("${api.security.token.secret:my-secret-key}")
    private String jwtSecret;

    public String getLoginFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public String getPerfilFromToken(String token) {
        return (String) getClaims(token).get("perfil");
    }

    public Long getUserIdFromToken(String token) {
        return ((Number) getClaims(token).get("id")).longValue();
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseSignedClaims(token.replace("Bearer ", ""))
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    .parseSignedClaims(token.replace("Bearer ", ""));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
