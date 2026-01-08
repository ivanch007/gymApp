package org.gym.application.service;

import jakarta.transaction.Transactional;
import org.gym.domain.model.User;
import org.gym.domain.port.in.AuthManagementPort;
import org.gym.domain.port.out.UserRepositoryPort;
import org.gym.util.validator.AuthValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthManagementPort {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final UserRepositoryPort userRepositoryPort;
    private final AuthValidator authValidator;

    public AuthService(UserRepositoryPort userRepositoryPort, AuthValidator authValidator) {
        this.userRepositoryPort = userRepositoryPort;
        this.authValidator = authValidator;
    }

    @Override
    public boolean authenticate(String username, String password) {
        try {
            authValidator.validateCredentials(username, password);
            return true;
        } catch (SecurityException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public void changePassword(String username, String oldPassword,String newPassword) {

        logger.info("Changing password for user: {}", username);
        User user = authValidator.validateCredentials(username, oldPassword);

        user.setPassword(newPassword);
        userRepositoryPort.save(user);

        logger.info("Password updated successfully for user: {}", username);


    }

    @Override
    @Transactional
    public void toggleStatus(String username, String password) {

        logger.info("Request for change of status (Active/Inactive) for: {}", username);

        User user = authValidator.validateCredentials(username, password);

        boolean currentStatus = user.isActive();
        user.setActive(!currentStatus);

        userRepositoryPort.save(user);
        logger.info("User {} status changed from {} to {}", username, currentStatus, !currentStatus);

    }
}
