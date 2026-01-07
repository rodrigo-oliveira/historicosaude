package com.historicosaude.historico.application.dto;

import com.historicosaude.historico.domain.enums.StatusConsulta;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record ConsultaDto(
    Long id,
    String cpfPaciente,
    String nomePaciente,
    String emailPaciente,
    String dataConsulta,
    String medico,
    String especialidade,
    String motivo,
    String status,
    String criadoEm,
    String atualizadoEm
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static ConsultaDto fromEntity(com.historicosaude.historico.domain.entities.Consulta consulta) {
        return new ConsultaDto(
            consulta.getId(),
            consulta.getCpfPaciente(),
            consulta.getNomePaciente(),
            consulta.getEmailPaciente(),
            consulta.getDataConsulta() != null ? consulta.getDataConsulta().format(FORMATTER) : null,
            consulta.getMedico(),
            consulta.getEspecialidade(),
            consulta.getMotivo(),
            consulta.getStatus() != null ? consulta.getStatus().toString() : null,
            consulta.getCriadoEm() != null ? consulta.getCriadoEm().format(FORMATTER) : null,
            consulta.getAtualizadoEm() != null ? consulta.getAtualizadoEm().format(FORMATTER) : null
        );
    }
}
