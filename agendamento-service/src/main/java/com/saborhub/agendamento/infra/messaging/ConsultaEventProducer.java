package com.historicosaude.agendamento.infra.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsultaEventProducer {
    
    private final RabbitTemplate rabbitTemplate;
    private static final String EXCHANGE_NAME = "consultas.exchange";
    private static final String ROUTING_KEY = "consulta.evento";

    public void publishConsultaEvent(ConsultaEvent event) {
        try {
            log.info("========================================");
            log.info("Publicando evento de consulta: {}", event.getEventType());
            log.info("Consulta ID: {}", event.getId());
            log.info("CPF Paciente: {}", event.getCpfPaciente());
            log.info("Nome Paciente: {}", event.getNomePaciente());
            log.info("Email Paciente: {}", event.getEmailPaciente());
            log.info("Médico: {}", event.getMedico());
            log.info("Status: {}", event.getStatus());
            log.info("Exchange: {}, Routing Key: {}", EXCHANGE_NAME, ROUTING_KEY);
            log.info("========================================");
            
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, event);
            
            log.info("✓ Evento publicado com sucesso para consulta ID: {}", event.getId());
        } catch (Exception e) {
            log.error("✗ ERRO ao publicar evento de consulta", e);
            log.error("Detalhes do erro: {}", e.getMessage());
            throw e;
        }
    }
}
