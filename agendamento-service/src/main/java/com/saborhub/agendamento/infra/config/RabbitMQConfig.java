package com.historicosaude.agendamento.infra.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    public static final String CONSULTAS_QUEUE = "consultas.notificacoes.queue";
    public static final String CONSULTAS_EXCHANGE = "consultas.exchange";
    public static final String CONSULTAS_ROUTING_KEY = "consulta.evento";

    @Bean
    public Queue consultasQueue() {
        return new Queue(CONSULTAS_QUEUE, true);
    }

    @Bean
    public TopicExchange consultasExchange() {
        return new TopicExchange(CONSULTAS_EXCHANGE, true, false);
    }

    @Bean
    public Binding consultasBinding(Queue consultasQueue, TopicExchange consultasExchange) {
        return BindingBuilder.bind(consultasQueue)
                .to(consultasExchange)
                .with(CONSULTAS_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
}
