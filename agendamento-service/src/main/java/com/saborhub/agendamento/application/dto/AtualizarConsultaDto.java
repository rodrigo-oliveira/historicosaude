package com.historicosaude.agendamento.application.dto;

import com.historicosaude.agendamento.domain.enums.StatusConsulta;
import java.time.LocalDateTime;

public record AtualizarConsultaDto(
    LocalDateTime dataConsulta,
    String medico,
    String especialidade,
    String motivo,
    StatusConsulta status
) {}
