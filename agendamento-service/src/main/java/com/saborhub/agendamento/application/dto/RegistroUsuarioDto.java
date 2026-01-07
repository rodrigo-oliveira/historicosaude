package com.historicosaude.agendamento.application.dto;

public record RegistroUsuarioDto(
    String cpf,
    String nome,
    String email,
    String login,
    String senha,
    String perfil
) {}
