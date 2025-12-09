package org.gym.application.service;

import org.gym.domain.model.Training;
import org.gym.domain.port.in.TrainingManagementPort;
import org.gym.domain.port.out.TraineeRepositoryPort;
import org.gym.domain.port.out.TrainerRepositoryPort;
import org.gym.domain.port.out.TrainingRepositoryPort;
import org.gym.domain.port.out.TrainingTypeRepositoryPort;
import org.gym.util.generator.IdGenerator;
import org.gym.util.validator.TrainingValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TrainingService implements TrainingManagementPort {

    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    private final TrainingRepositoryPort trainingRepositoryPort;
    private final TraineeRepositoryPort traineeRepositoryPort;
    private final TrainerRepositoryPort trainerRepositoryPort;
    private  final TrainingTypeRepositoryPort trainingTypeRepositoryPort;

    public TrainingService(TrainingRepositoryPort trainingRepositoryPort, TraineeRepositoryPort traineeRepositoryPort,
                           TrainerRepositoryPort trainerRepositoryPort,
                           TrainingTypeRepositoryPort trainingTypeRepositoryPort) {
        this.trainingRepositoryPort = trainingRepositoryPort;
        this.traineeRepositoryPort = traineeRepositoryPort;
        this.trainerRepositoryPort = trainerRepositoryPort;
        this.trainingTypeRepositoryPort = trainingTypeRepositoryPort;
    }

    @Override
    public Training createTraining(Training training) {

        logger.info("createTraining - start");
        TrainingValidator.validateForCreate(training);

        if (trainerRepositoryPort.findById(training.getTrainerUserId()) == null) {
            throw new IllegalStateException("Trainer not found");
        }

        if (traineeRepositoryPort.findById(training.getTraineeUserId()) == null) {
            throw new IllegalStateException("Trainee not found");
        }

        if (trainingTypeRepositoryPort.findById(training.getTrainingTypeId()) == null) {
            throw new IllegalStateException("TrainingType not found");
        }

        if (training.getId() == null) {
            training.setId(IdGenerator.generateId());
        }

        Training saved = trainingRepositoryPort.save(training);
        logger.info("createTraining - done id={}", saved.getId());
        return saved;



    }

    @Override
    public Training getTraining(Long id) {
        logger.debug("getTraining id={}", id);
        return trainingRepositoryPort.findById(id);
    }
}
