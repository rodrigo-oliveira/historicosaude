package com.historicosaude.agendamento.application.usecases;

import com.historicosaude.agendamento.application.dto.AtualizarConsultaDto;
import com.historicosaude.agendamento.domain.entities.Consulta;
import com.historicosaude.agendamento.infra.repository.ConsultaRepository;
import com.historicosaude.agendamento.infra.messaging.ConsultaEvent;
import com.historicosaude.agendamento.infra.messaging.ConsultaEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AtualizarConsultaUseCase {
    
    private final ConsultaRepository consultaRepository;
    private final ConsultaEventProducer eventProducer;

    public void executar(Long id, AtualizarConsultaDto dto) {
        Consulta consulta = consultaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));

        if (dto.dataConsulta() != null) {
            consulta.setDataConsulta(dto.dataConsulta());
        }
        if (dto.medico() != null) {
            consulta.setMedico(dto.medico());
        }
        if (dto.especialidade() != null) {
            consulta.setEspecialidade(dto.especialidade());
        }
        if (dto.motivo() != null) {
            consulta.setMotivo(dto.motivo());
        }
        if (dto.status() != null) {
            consulta.setStatus(dto.status());
        }

        // Atualizar timestamp de modificação
        consulta.setAtualizadoEm(LocalDateTime.now());

        consultaRepository.save(consulta);

        // Publicar evento
        ConsultaEvent event = new ConsultaEvent(
            "CONSULTA_ATUALIZADA",
            consulta.getId(),
            consulta.getCpfPaciente(),
            consulta.getNomePaciente(),
            consulta.getEmailPaciente(),
            consulta.getDataConsulta(),
            consulta.getMedico(),
            consulta.getEspecialidade(),
            consulta.getMotivo(),
            consulta.getStatus().toString(),
            consulta.getCriadoEm(),
            consulta.getAtualizadoEm()
        );
        eventProducer.publishConsultaEvent(event);
    }
}
