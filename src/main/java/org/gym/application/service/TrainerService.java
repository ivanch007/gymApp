package org.gym.application.service;

import org.gym.domain.model.Trainer;
import org.gym.domain.model.User;
import org.gym.domain.port.in.TrainerManagementPort;
import org.gym.domain.port.out.TrainerRepositoryPort;
import org.gym.domain.port.out.UserRepositoryPort;
import org.gym.util.generator.PasswordGenerator;
import org.gym.util.generator.UserNameGenerator;
import org.gym.util.validator.AuthValidator;
import org.gym.util.validator.TrainerValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.Objects;

@Service
public class TrainerService implements TrainerManagementPort {

    private final TrainerRepositoryPort trainerRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final AuthValidator authValidator;
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    public TrainerService(TrainerRepositoryPort trainerRepositoryPort,
                          UserRepositoryPort userRepositoryPort,
                          AuthValidator authValidator) {
        this.trainerRepositoryPort = Objects.requireNonNull(trainerRepositoryPort);
        this.userRepositoryPort = userRepositoryPort;
        this.authValidator = authValidator;
    }

    @Override
    @Transactional
    public Trainer createTrainer(Trainer trainer) {
        logger.info("Service: Starting Trainer profile creation");
        TrainerValidator.validateForCreate(trainer);

        User user = trainer.getUser();
        user.setUsername(UserNameGenerator.generateUserNameUnique(user, userRepositoryPort));
        user.setPassword(PasswordGenerator.generateRandomPassword(10));
        user.setActive(true);

        Trainer saved = trainerRepositoryPort.save(trainer);
        logger.info("Service: Trainer profile created for username: {}", saved.getUser().getUsername());
        return saved;
    }

    @Override
    public Trainer getTrainer(String username) {
        return trainerRepositoryPort.findByUsername(username);
    }

    @Override
    @Transactional
    public Trainer updateTrainer(String username, String password, Trainer trainer) {
        logger.info("Service: Attempting to update trainer profile for: {}", username);

        authValidator.validateCredentials(username, password);

        Trainer existing = trainerRepositoryPort.findByUsername(username);
        if (existing == null) {
            throw new IllegalArgumentException("Trainer not found with username: " + username);
        }

        existing.setSpecialization(trainer.getSpecialization());
        if (trainer.getUser() != null) {
            existing.getUser().setFirstName(trainer.getUser().getFirstName());
            existing.getUser().setLastName(trainer.getUser().getLastName());
        }

        return trainerRepositoryPort.save(existing);
    }
}