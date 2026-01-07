package com.historicosaude.agendamento.application.dto;

import com.historicosaude.agendamento.domain.enums.StatusConsulta;
import java.time.LocalDateTime;

public record ConsultaDto(
    Long id,
    String cpfPaciente,
    String nomePaciente,
    String emailPaciente,
    LocalDateTime dataConsulta,
    String medico,
    String especialidade,
    String motivo,
    StatusConsulta status
) {}
