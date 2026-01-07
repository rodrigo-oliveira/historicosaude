package com.historicosaude.notificacoes.application.services;

import com.historicosaude.notificacoes.infra.messaging.ConsultaEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificacaoService {

    public void processarNotificacao(ConsultaEvent event) {
        switch (event.getEventType()) {
            case "CONSULTA_CRIADA":
                enviarNotificacaoCriacaoConsulta(event);
                break;
            case "CONSULTA_ATUALIZADA":
                enviarNotificacaoAtualizacaoConsulta(event);
                break;
            case "CONSULTA_CANCELADA":
                enviarNotificacaoCancelamentoConsulta(event);
                break;
            default:
                log.warn("Tipo de evento desconhecido: {}", event.getEventType());
        }
    }

    private void enviarNotificacaoCriacaoConsulta(ConsultaEvent event) {
        String assunto = "Consulta Agendada";
        String corpoEmail = String.format(
            "Olá,\n\n" +
            "Sua consulta foi agendada com sucesso.\n" +
            "Data: %s\n" +
            "Médico: %s\n" +
            "Motivo: %s\n\n" +
            "Atenciosamente,\n" +
            "Sistema de Agendamento",
            event.getDataConsulta(), event.getMedico(), event.getMotivo()
        );
        
        enviarEmail(event.getEmailPaciente(), assunto, corpoEmail);
        enviarSMS(event.getCpfPaciente(), "Sua consulta foi agendada para " + event.getDataConsulta());
    }

    private void enviarNotificacaoAtualizacaoConsulta(ConsultaEvent event) {
        String assunto = "Consulta Atualizada";
        String corpoEmail = String.format(
            "Olá,\n\n" +
            "Sua consulta foi atualizada.\n" +
            "Nova Data: %s\n" +
            "Médico: %s\n\n" +
            "Atenciosamente,\n" +
            "Sistema de Agendamento",
            event.getDataConsulta(), event.getMedico()
        );
        
        enviarEmail(event.getEmailPaciente(), assunto, corpoEmail);
        enviarSMS(event.getCpfPaciente(), "Sua consulta foi atualizada para " + event.getDataConsulta());
    }

    private void enviarNotificacaoCancelamentoConsulta(ConsultaEvent event) {
        String assunto = "Consulta Cancelada";
        String corpoEmail = String.format(
            "Olá,\n\n" +
            "Sua consulta foi cancelada.\n" +
            "Data da consulta: %s\n" +
            "Médico: %s\n\n" +
            "Atenciosamente,\n" +
            "Sistema de Agendamento",
            event.getDataConsulta(), event.getMedico()
        );
        
        enviarEmail(event.getEmailPaciente(), assunto, corpoEmail);
        enviarSMS(event.getCpfPaciente(), "Sua consulta de " + event.getDataConsulta() + " foi cancelada");
    }

    private void enviarEmail(String email, String assunto, String corpo) {
        // Simulação de envio de e-mail
        log.info("=== ENVIO DE E-MAIL ===");
        log.info("Para: {}", email);
        log.info("Assunto: {}", assunto);
        log.info("Corpo:\n{}", corpo);
        log.info("======================");
    }

    private void enviarSMS(String cpf, String mensagem) {
        // Simulação de envio de SMS
        log.info("=== ENVIO DE SMS ===");
        log.info("Para (CPF): {}", cpf);
        log.info("Mensagem: {}", mensagem);
        log.info("===================");
    }
}
