package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.dto.MailParams;
import org.example.CryptoTool;
import org.example.dao.AppUserDAO;
import org.example.entity.AppUser;
import org.example.entity.enums.UserState;
import org.example.service.AppUserService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

@Service
@RequiredArgsConstructor
@Log4j
public class AppUserServiceImpl implements AppUserService {
    private final AppUserDAO appUserDAO;
    private final CryptoTool cryptoTool;
    @Value("${spring.rabbitmq.queues.registration-mail}")
    private String registrationMailQueue;

    private final RabbitTemplate rabbitTemplate;


    @Override
    public String registerUser(AppUser appUser) {
        if (appUser.getIsActive()) {
            return "An account is already registered";
        } else if (appUser.getEmail() != null) {
            return "Activation letter has already been sent." + "Click on the lick in the letter to finish registration";
        }
        appUser.setState(UserState.WAIT_FOR_EMAIL_STATE);
        appUserDAO.save(appUser);
        return "Please, enter your email";
    }

    @Override
    public String setEmail(AppUser appUser, String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException e) {
            return "Please, enter correct email." + " To cancel command use /cancel";
        }
        var optional = appUserDAO.findByEmail(email);
        if (optional.isEmpty()) {
            appUser.setEmail(email);
            appUser.setState(UserState.BASIC_STATE);
            appUser = appUserDAO.save(appUser);

            var cryptoUserId = cryptoTool.hashOf(appUser.getId());
            sendRequestToMailService(cryptoUserId, email);

            return "Activation letter was sent." + "Check your email and click on the link in the email to confirm registration";
        } else {
            return "This email already exists. Input correct another email." + " To cancel command use /cancel";
        }
    }

    private void sendRequestToMailService(String cryptoUserId, String email) {
        var mailParams = MailParams.builder()
                .id(cryptoUserId)
                .emailTo(email)
                .build();
        rabbitTemplate.convertAndSend(registrationMailQueue, mailParams);
    }
}
