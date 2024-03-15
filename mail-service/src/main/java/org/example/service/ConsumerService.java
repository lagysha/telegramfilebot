package org.example.service;

import org.dto.MailParams;

public interface ConsumerService {

    void consumeRegistrationMail(MailParams mailParams);
}
