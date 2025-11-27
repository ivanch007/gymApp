package org.gym.facade;

import org.gym.domain.model.*;
import org.gym.domain.port.in.*;
import org.springframework.stereotype.Component;

@Component
public class GymFacade {

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
        return userManagementPort.createUser(user);
    }
    public User updateUser(User user) {
        return userManagementPort.updateUser(user);
    }
    public User getUser(Long id) {
        return userManagementPort.getUser(id);
    }

    //Trainer
    public Trainer createTrainer(Trainer trainer) {
        return trainerManagementPort.createTrainer(trainer);
    }
    public Trainer updateTrainer(Trainer trainer) {
        return trainerManagementPort.updateTrainer(trainer);
    }
    public Trainer getTrainer(Long id) {
        return trainerManagementPort.getTrainer(id);
    }

    //Trainee
    public Trainee createTrainee(Trainee trainee) {
        return traineeManagementPort.createTrainee(trainee);
    }
    public Trainee updateTrainee(Trainee trainee) {
        return traineeManagementPort.updateTrainee(trainee);
    }
    public Trainee getTrainee(Long id) {
        return traineeManagementPort.getTrainee(id);
    }
    public void deleteTrainee(Long id) {
        traineeManagementPort.deleteTrainee(id);
    }

    //Training
    public Training createTraining(Training training) {
        return trainingManagementPort.createTraining(training);
    }
    public Training getTraining(Long id) {
        return trainingManagementPort.getTraining(id);
    }

    //TrainingType
    public TrainingType createTrainingType(TrainingType trainingType) {
        return trainingTypeManagementPort.create(trainingType);
    }
    public TrainingType getTrainingType(Long id) {
        return trainingTypeManagementPort.get(id);
    }
    public TrainingType updateTrainingType(TrainingType trainingType) {
        return trainingTypeManagementPort.update(trainingType);
    }



}
