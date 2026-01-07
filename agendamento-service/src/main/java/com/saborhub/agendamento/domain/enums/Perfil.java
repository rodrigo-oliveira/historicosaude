package com.historicosaude.agendamento.domain.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Perfil implements GrantedAuthority {
    PACIENTE("PACIENTE"),
    ENFERMEIRO("ENFERMEIRO"),
    MEDICO("MEDICO"),
    ADMIN("ADMIN");

    private final String role;

    Perfil(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + this.role;
    }
}
