package com.historicosaude.notificacoes.application.services;

import com.historicosaude.notificacoes.infra.messaging.ConsultaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacaoConsumer {

    private final NotificacaoService notificacaoService;

    @RabbitListener(queues = "consultas.notificacoes.queue")
    public void receberEventoConsulta(ConsultaEvent event) {
        log.info("Evento recebido: {} para consulta ID: {}", event.getEventType(), event.getId());
        notificacaoService.processarNotificacao(event);
    }
}
