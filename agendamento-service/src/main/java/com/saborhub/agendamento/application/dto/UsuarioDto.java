package com.historicosaude.agendamento.application.dto;

import com.historicosaude.agendamento.domain.enums.Perfil;
import java.time.ZonedDateTime;

public record UsuarioDto(
    Long id,
    String cpf,
    String nome,
    String email,
    String login,
    ZonedDateTime dataUltimaAlteracao,
    Perfil perfil
) {}
