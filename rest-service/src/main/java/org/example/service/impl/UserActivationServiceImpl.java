package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.CryptoTool;
import org.example.dao.AppUserDAO;
import org.example.service.UserActivationService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserActivationServiceImpl implements UserActivationService {
    private final AppUserDAO appUserDAO;
    private final CryptoTool cryptoTool;

    @Override
    public boolean activation(String cryptoUserId) {
        var userId = cryptoTool.idOf(cryptoUserId);
        var optional = appUserDAO.findById(userId);
        if(optional.isPresent()){
            var user = optional.get();
            user.setIsActive(true);
            appUserDAO.save(user);
            return true;
        }
        return false;
    }
}
