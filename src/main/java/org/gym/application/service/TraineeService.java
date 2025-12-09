package org.gym.application.service;

import org.gym.domain.model.Trainee;
import org.gym.domain.model.User;
import org.gym.domain.port.in.TraineeManagementPort;
import org.gym.domain.port.out.TraineeRepositoryPort;
import org.gym.domain.port.out.UserRepositoryPort;
import org.gym.util.generator.IdGenerator;
import org.gym.util.validator.TraineeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TraineeService implements TraineeManagementPort {

    private final TraineeRepositoryPort traineeRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    public TraineeService(TraineeRepositoryPort traineeRepositoryPort,
                          UserRepositoryPort userRepositoryPort) {
        this.traineeRepositoryPort = traineeRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
    }


    @Override
    public Trainee createTrainee(Trainee trainee) {

        logger.info("createTrainee - start");

        TraineeValidator.validateForCreate(trainee);

        User user = userRepositoryPort.findById(trainee.getUserId());
        if (user == null)
            throw new IllegalStateException("Associated user not found with id=" + trainee.getUserId());

        if (trainee.getId() == null)
            trainee.setId(IdGenerator.generateId());

        Trainee saved = traineeRepositoryPort.save(trainee);
        logger.info("createTrainee - done id={}", saved.getId());
        return saved;
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        logger.info("updateTrainee - start id={}", trainee.getId());

        TraineeValidator.validateForUpdate(trainee);

        Trainee saved = traineeRepositoryPort.save(trainee);
        logger.info("updateTrainee - done id={}", saved.getId());
        return saved;
    }

    @Override
    public Trainee getTrainee(Long id) {
        logger.debug("getTrainee id={}", id);
        return traineeRepositoryPort.findById(id);
    }

    @Override
    public void deleteTrainee(Long id) {
        logger.info("deleteTrainee - id={}", id);
        traineeRepositoryPort.delete(id);
    }
}
