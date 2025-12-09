package org.gym.facade;

import org.gym.domain.model.*;
import org.gym.domain.port.in.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GymFacade {

    private static final Logger logger = LoggerFactory.getLogger(GymFacade.class);

    private final UserManagementPort userManagementPort;
    private final TrainerManagementPort trainerManagementPort;
    private final TraineeManagementPort traineeManagementPort;
    private final TrainingManagementPort trainingManagementPort;
    private final TrainingTypeManagementPort trainingTypeManagementPort;

    public GymFacade(UserManagementPort userManagementPort, TrainerManagementPort trainerManagementPort, TraineeManagementPort traineeManagementPort, TrainingManagementPort trainingManagementPort, TrainingTypeManagementPort trainingTypeManagementPort) {
        this.userManagementPort = userManagementPort;
        this.trainerManagementPort = trainerManagementPort;
        this.traineeManagementPort = traineeManagementPort;
        this.trainingManagementPort = trainingManagementPort;
        this.trainingTypeManagementPort = trainingTypeManagementPort;
    }

    //User
    public User createUser(User user) {
        logger.info("Facade: Creating user");
        return userManagementPort.createUser(user);
    }
    public User updateUser(User user) {
        logger.info("Facade: Updating user id={}", user.getId());
        return userManagementPort.updateUser(user);
    }
    public User getUser(Long id) {
        logger.info("Facade: Getting user id={}", id);
        return userManagementPort.getUser(id);
    }

    //Trainer
    public Trainer createTrainer(Trainer trainer) {
        logger.info("Facade: Creating trainer");
        return trainerManagementPort.createTrainer(trainer);
    }
    public Trainer updateTrainer(Trainer trainer) {
        logger.info("Facade: Updating trainer id={}", trainer.getId());
        return trainerManagementPort.updateTrainer(trainer);
    }
    public Trainer getTrainer(Long id) {
        logger.info("Facade: Getting trainer id={}", id);
        return trainerManagementPort.getTrainer(id);
    }

    //Trainee
    public Trainee createTrainee(Trainee trainee) {
        logger.info("Facade: Creating trainee");
        return traineeManagementPort.createTrainee(trainee);
    }
    public Trainee updateTrainee(Trainee trainee) {
        logger.info("Facade: Updating trainee id={}", trainee.getId());
        return traineeManagementPort.updateTrainee(trainee);
    }
    public Trainee getTrainee(Long id) {
        logger.info("Facade: Getting trainee id={}", id);
        return traineeManagementPort.getTrainee(id);
    }
    public void deleteTrainee(Long id) {
        logger.info("Facade: Deleting trainee id={}", id);
        traineeManagementPort.deleteTrainee(id);
    }

    //Training
    public Training createTraining(Training training) {
        logger.info("Facade: Creating training");
        return trainingManagementPort.createTraining(training);
    }
    public Training getTraining(Long id) {
        logger.info("Facade: Getting training id={}", id);
        return trainingManagementPort.getTraining(id);
    }

    //TrainingType
    public TrainingType createTrainingType(TrainingType trainingType) {
        logger.info("Facade: Creating training type");
        return trainingTypeManagementPort.create(trainingType);
    }
    public TrainingType getTrainingType(Long id) {
        logger.info("Facade: Getting training type id={}", id);
        return trainingTypeManagementPort.get(id);
    }
    public TrainingType updateTrainingType(TrainingType trainingType) {
        logger.info("Facade: Updating training type id={}", trainingType.getId());
        return trainingTypeManagementPort.update(trainingType);
    }
}
