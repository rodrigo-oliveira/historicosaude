package com.historicosaude.historico.infra.config;

import com.historicosaude.historico.domain.enums.Perfil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    
    private String login;
    private String senha;
    private Perfil perfil;
    private Long userId;
    private String cpf;

    public CustomUserDetails(String login, String senha, Perfil perfil, Long userId, String cpf) {
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
        this.userId = userId;
        this.cpf = cpf;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + perfil.name()));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
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

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public Long getUserId() {
        return userId;
    }

    public String getCpf() {
        return cpf;
    }
}
