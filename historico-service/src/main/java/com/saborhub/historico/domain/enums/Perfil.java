package com.historicosaude.historico.domain.enums;

public enum Perfil {
    ADMIN("admin"),
    MEDICO("medico"),
    ENFERMEIRO("enfermeiro"),
    PACIENTE("paciente");

    private final String role;

    Perfil(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
