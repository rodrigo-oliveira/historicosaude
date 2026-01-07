package com.historicosaude.agendamento.application.usecases;

import com.historicosaude.agendamento.domain.entities.Consulta;
import com.historicosaude.agendamento.domain.enums.StatusConsulta;
import com.historicosaude.agendamento.infra.repository.ConsultaRepository;
import com.historicosaude.agendamento.infra.messaging.ConsultaEvent;
import com.historicosaude.agendamento.infra.messaging.ConsultaEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CancelarConsultaUseCase {
    
    private final ConsultaRepository consultaRepository;
    private final ConsultaEventProducer eventProducer;

    public void executar(Long id) {
        log.info("Iniciando cancelamento da consulta com ID: {}", id);
        try {
            Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
            log.info("Consulta encontrada: {} - Status atual: {}", id, consulta.getStatus());

            consulta.setStatus(StatusConsulta.CANCELADA);
            consulta.setAtualizadoEm(LocalDateTime.now());
            consultaRepository.save(consulta);
            log.info("Consulta {} cancelada com sucesso", id);

            // Publicar evento
            ConsultaEvent event = new ConsultaEvent(
                "CONSULTA_CANCELADA",
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
            log.info("Evento de cancelamento publicado para consulta {}", id);
        } catch (Exception e) {
            log.error("Erro ao cancelar consulta com ID: {}", id, e);
            throw e;
        }
    }
}
