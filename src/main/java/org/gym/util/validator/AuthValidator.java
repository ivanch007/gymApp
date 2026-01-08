package org.gym.util.validator;

import org.gym.domain.model.User;
import org.gym.domain.port.out.UserRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public  class AuthValidator {

    private final UserRepositoryPort userRepositoryPort;
    private static final Logger logger = LoggerFactory.getLogger(AuthValidator.class);

    public AuthValidator(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    public User validateCredentials(String username, String password){
        logger.debug("Validating user credentials: {}", username);

        User user = userRepositoryPort.findByUsername(username);

        if (user == null || !user.getPassword().equals(password)) {
            logger.error("Authentication failed for user: {}", username);

            throw new SecurityException("Access denied: Invalid credentials.");
        }

        logger.debug("Successful validation for: {}", username);

        return user;
    }
    }


