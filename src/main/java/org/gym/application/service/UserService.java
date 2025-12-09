package org.gym.application.service;

import org.gym.domain.model.User;
import org.gym.domain.port.in.UserManagementPort;
import org.gym.domain.port.out.UserRepositoryPort;
import org.gym.util.generator.IdGenerator;
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
    public User createUser(User user) {

        logger.info("createUser -start: {} {}", user.getFirstName(), user.getLastName());
        UserValidator.validateUserForCreate(user);

        if(user.getId() == null){
            user.setId(IdGenerator.generateId());
        }
        String username = UserNameGenerator.generateUserNameUnique(user, userRepositoryPort);
        user.setUsername(username);
        user.setPassword(PasswordGenerator.generateRandomPassword(10));

        User saved = userRepositoryPort.save(user);
        logger.info("createUser - done: id={}, username={}", saved.getId(), saved.getUsername());
        return saved;
    }

    @Override
    public User updateUser(User user) {

        logger.info("updateUser - start id={}", user.getId());
        UserValidator.validateForUpdate(user);
        User saved = userRepositoryPort.save(user);
        logger.info("updateUser - done id={}", saved.getId());
        return saved;
    }

    @Override
    public User getUser(Long id) {

        logger.debug("getUser id={}", id);
        if (id == null) return null;
        return userRepositoryPort.findById(id);
    }
}
