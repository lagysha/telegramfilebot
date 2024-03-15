package org.example.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.example.service.ConsumerService;
import org.example.service.MainService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@Log4j
@AllArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {

    private final MainService mainService;

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.text-message-update}")
    public void consumerTextMessageUpdate(Update update) {
        log.debug("Node: Text Message is received");
        mainService.processTextMessage(update);
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.doc-message-update}")
    public void consumerDocMessageUpdate(Update update) {
        log.debug("Node: Doc Message is received");
        mainService.processDocMessage(update);
    }

    @Override
    @RabbitListener(queues = "${spring.rabbitmq.queues.photo-message-update}")
    public void consumerPhotoMessageUpdate(Update update) {
        log.debug("Node: Photo Message is received");
        mainService.processPhotoMessage(update);
    }
}
