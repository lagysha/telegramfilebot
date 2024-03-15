package org.example.service.impl;

import lombok.AllArgsConstructor;
import org.example.controller.UpdateProcessor;
import org.example.service.AnswerConsumer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


@AllArgsConstructor
@Service
public class AnswerConsumerImpl implements AnswerConsumer {
    private final UpdateProcessor updateProcessor;

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.answer-message}")
    public void cosumer(SendMessage sendMessage) {
        updateProcessor.setView(sendMessage);
    }
}
