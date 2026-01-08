package org.gym.application.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.gym.domain.model.Trainee;
import org.gym.domain.model.User;
import org.gym.domain.port.in.TraineeManagementPort;
import org.gym.domain.port.out.TraineeRepositoryPort;
import org.gym.domain.port.out.UserRepositoryPort;
import org.gym.util.generator.PasswordGenerator;
import org.gym.util.generator.UserNameGenerator;
import org.gym.util.validator.AuthValidator;
import org.gym.util.validator.TraineeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TraineeService implements TraineeManagementPort {

    private final TraineeRepositoryPort traineeRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final AuthValidator authValidator;

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    public TraineeService(TraineeRepositoryPort traineeRepositoryPort,
                          UserRepositoryPort userRepositoryPort,
                          AuthValidator authValidator) {
        this.traineeRepositoryPort = traineeRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
        this.authValidator = authValidator;
    }

    @Override
    @Transactional
    public Trainee createTrainee(Trainee trainee) {
        logger.info("createTrainee - start");
        TraineeValidator.validateForCreate(trainee);

        User user = trainee.getUser();
        user.setUsername(UserNameGenerator.generateUserNameUnique(user, userRepositoryPort));
        user.setPassword(PasswordGenerator.generateRandomPassword(10));
        user.setActive(true);

        return traineeRepositoryPort.save(trainee);
    }

    @Override
    @Transactional
    public Trainee updateTrainee(String username, String password, Trainee trainee) {
        logger.info("updateTrainee - start for: {}", username);

        authValidator.validateCredentials(username, password);

        Trainee existingTrainee = traineeRepositoryPort.findByUsername(username);
        if (existingTrainee == null){
            throw new EntityNotFoundException("Trainee not found");
        }

        existingTrainee.setAddress(trainee.getAddress());
        existingTrainee.setDateOfBirth(trainee.getDateOfBirth());

        if (trainee.getUser() != null) {
            existingTrainee.getUser().setFirstName(trainee.getUser().getFirstName());
            existingTrainee.getUser().setLastName(trainee.getUser().getLastName());
        }

        return traineeRepositoryPort.save(existingTrainee);
    }

    @Override
    public Trainee getTrainee(String username) {
        return traineeRepositoryPort.findByUsername(username);
    }

    @Override
    @Transactional
    public void deleteTrainee(String username, String password) {
        logger.info("Attempting hard delete for trainee: {}", username);
        authValidator.validateCredentials(username, password);
        traineeRepositoryPort.delete(username);
        logger.info("Trainee {} and relevant trainings deleted.", username);
    }
}