package com.historicosaude.agendamento.application.dto;

import java.time.LocalDateTime;

public record RegistroConsultaDto(
    String cpfPaciente,
    String nomePaciente,
    String emailPaciente,
    LocalDateTime dataConsulta,
    String medico,
    String especialidade,
    String motivo
) {}
