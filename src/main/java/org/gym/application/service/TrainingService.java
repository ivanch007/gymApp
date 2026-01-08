package org.gym.application.service;

import org.gym.domain.model.Training;
import org.gym.domain.port.in.TrainingManagementPort;
import org.gym.domain.port.out.TrainingRepositoryPort;
import org.gym.util.validator.TrainingValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class TrainingService implements TrainingManagementPort {

    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);
    private final TrainingRepositoryPort trainingRepositoryPort;

    public TrainingService(TrainingRepositoryPort trainingRepositoryPort) {
        this.trainingRepositoryPort = Objects.requireNonNull(trainingRepositoryPort);
    }

    @Override
    @Transactional
    public Training createTraining(Training training) {
        logger.info("Service: Starting training creation: {}", training.getTrainingName());

        TrainingValidator.validateForCreate(training);


        Training saved = trainingRepositoryPort.save(training);

        logger.info("Service: Training created successfully with ID: {}", saved.getId());
        return saved;
    }

    @Override
    public List<Training> getTraineeTrainings(String username, LocalDate fromDate, LocalDate toDate,
                                              String trainerName, String trainingType) {
        logger.debug("Service: Fetching trainings for trainee: {} with filters", username);
        return trainingRepositoryPort.findTraineeTrainingsByCriteria(username, fromDate, toDate, trainerName, trainingType);
    }


    @Override
    public List<Training> getTrainerTrainings(String username, LocalDate fromDate, LocalDate toDate,
                                              String traineeName) {
        logger.debug("Service: Fetching trainings for trainer: {} with filters", username);
        return trainingRepositoryPort.findTrainerTrainingsByCriteria(username, fromDate, toDate, traineeName);
    }
}
