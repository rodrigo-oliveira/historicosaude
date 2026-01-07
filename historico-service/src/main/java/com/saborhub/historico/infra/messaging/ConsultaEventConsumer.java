package com.historicosaude.historico.infra.messaging;

import com.historicosaude.historico.domain.entities.Consulta;
import com.historicosaude.historico.domain.enums.StatusConsulta;
import com.historicosaude.historico.infra.repository.ConsultaRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsultaEventConsumer {
    
    private final ConsultaRepository consultaRepository;
    private final EntityManager entityManager;
    private static final String CONSULTAS_QUEUE = "consultas.notificacoes.queue";
    
    @Transactional
    @RabbitListener(queues = CONSULTAS_QUEUE)
    public void consumirEventoConsulta(ConsultaEvent evento) {
        try {
            log.info("========================================");
            log.info("Consumindo evento de consulta: {}", evento.getEventType());
            log.info("Event Type: {}", evento.getEventType());
            log.info("Consulta ID: {}", evento.getId());
            log.info("CPF Paciente: {}", evento.getCpfPaciente());
            log.info("Nome Paciente: {}", evento.getNomePaciente());
            log.info("Email Paciente: {}", evento.getEmailPaciente());
            log.info("Data Consulta: {}", evento.getDataConsulta());
            log.info("Médico: {}", evento.getMedico());
            log.info("Especialidade: {}", evento.getEspecialidade());
            log.info("Status: {}", evento.getStatus());
            log.info("========================================");
            
            Consulta consulta = criarNovaConsulta(evento);
            
            // Usar persist() em vez de save() para forçar um INSERT
            entityManager.persist(consulta);
            entityManager.flush();
            
            log.info("✓ Consulta persistida e flushed com sucesso. ID: {}, CPF: {}", consulta.getId(), consulta.getCpfPaciente());
            
        } catch (Exception e) {
            log.error("✗ ERRO ao consumir evento de consulta: ", e);
            log.error("Erro details: {}", e.getMessage());
            throw e;
        }
    }
    
    private Consulta criarNovaConsulta(ConsultaEvent evento) {
        Consulta consulta = new Consulta();
        
        // NÃO settar ID aqui - deixar o banco gerar via IDENTITY
        // Se settar ID e a consulta já existe, Hibernate tenta fazer merge e gera StaleObjectStateException
        
        consulta.setCpfPaciente(evento.getCpfPaciente() != null ? evento.getCpfPaciente() : "");
        consulta.setNomePaciente(evento.getNomePaciente() != null ? evento.getNomePaciente() : "");
        consulta.setEmailPaciente(evento.getEmailPaciente() != null ? evento.getEmailPaciente() : "");
        consulta.setDataConsulta(evento.getDataConsulta() != null ? evento.getDataConsulta() : LocalDateTime.now());
        consulta.setMedico(evento.getMedico() != null ? evento.getMedico() : "");
        consulta.setEspecialidade(evento.getEspecialidade());
        consulta.setMotivo(evento.getMotivo());
        
        if (evento.getStatus() != null) {
            try {
                consulta.setStatus(StatusConsulta.valueOf(evento.getStatus()));
            } catch (IllegalArgumentException e) {
                log.warn("Status inválido: {}, usando AGENDADA como padrão", evento.getStatus());
                consulta.setStatus(StatusConsulta.AGENDADA);
            }
        } else {
            consulta.setStatus(StatusConsulta.AGENDADA);
        }
        
        LocalDateTime agora = LocalDateTime.now();
        consulta.setCriadoEm(evento.getCriadoEm() != null ? evento.getCriadoEm() : agora);
        consulta.setAtualizadoEm(evento.getAtualizadoEm() != null ? evento.getAtualizadoEm() : agora);
        
        return consulta;
    }
}
