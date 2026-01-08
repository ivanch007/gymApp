package org.gym.application.service;

import jakarta.transaction.Transactional;
import org.gym.domain.model.User;
import org.gym.domain.port.in.UserManagementPort;
import org.gym.domain.port.out.UserRepositoryPort;
import org.gym.util.generator.PasswordGenerator;
import org.gym.util.generator.UserNameGenerator;
import org.gym.util.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class UserService implements UserManagementPort {

    private final UserRepositoryPort userRepositoryPort;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    public UserService(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = Objects.requireNonNull(userRepositoryPort);
    }

    @Override
    @Transactional
    public User createUser(User user) {

        logger.info("createUser -start: {} {}", user.getFirstName(), user.getLastName());
        UserValidator.validateUserForCreate(user);

        String username = UserNameGenerator.generateUserNameUnique(user, userRepositoryPort);
        user.setUsername(username);
        user.setPassword(PasswordGenerator.generateRandomPassword(10));
        user.setActive(true);

        User saved = userRepositoryPort.save(user);
        logger.info("createUser - done: id={}, username={}", saved.getId(), saved.getUsername());
        return saved;
    }

    @Override
    @Transactional
    public User updateUser(User user) {

        logger.info("Updating user profile for username: {}", user.getUsername());

        User existing = userRepositoryPort.findByUsername(user.getUsername());
        if (existing == null) {
            logger.error("Service: Update failed. User {} not found", user.getUsername());
            throw new IllegalArgumentException("User not found: " + user.getUsername());
        }

        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());


        User saved = userRepositoryPort.save(existing);
        logger.info("Service: User profile updated successfully for: {}", saved.getUsername());
        return saved;
    }

    @Override
    public User getUserByUsername(String username) {
        logger.debug("Fetching user by username: {}", username);
        return userRepositoryPort.findByUsername(username);
    }

}
