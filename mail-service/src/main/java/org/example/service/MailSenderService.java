package org.example.service;


import org.dto.MailParams;

public interface MailSenderService {
    void send(MailParams mailParams);
}
