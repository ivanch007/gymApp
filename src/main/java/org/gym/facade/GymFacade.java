package org.gym.facade;

import org.gym.domain.model.*;
import org.gym.domain.port.in.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class GymFacade {

    private static final Logger logger = LoggerFactory.getLogger(GymFacade.class);

    private final AuthManagementPort authManagementPort;
    private final TraineeManagementPort traineeManagementPort;
    private final TrainerManagementPort trainerManagementPort;
    private final TrainingManagementPort trainingManagementPort;
    private final TrainingTypeManagementPort trainingTypeManagementPort;
    private final UserManagementPort userManagementPort;


    public GymFacade(AuthManagementPort authManagementPort,
                     TraineeManagementPort traineeManagementPort,
                     TrainerManagementPort trainerManagementPort,
                     TrainingManagementPort trainingManagementPort,
                     TrainingTypeManagementPort trainingTypeManagementPort,
                     UserManagementPort userManagementPort) {
        this.authManagementPort = authManagementPort;
        this.traineeManagementPort = traineeManagementPort;
        this.trainerManagementPort = trainerManagementPort;
        this.trainingManagementPort = trainingManagementPort;
        this.trainingTypeManagementPort = trainingTypeManagementPort;
        this.userManagementPort = userManagementPort;
    }

    // --- Authentication & Security ---
    public boolean authenticate(String username, String password) {
        return authManagementPort.authenticate(username, password);
    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        authManagementPort.changePassword(username, oldPassword, newPassword);
    }

    public void toggleUserStatus(String username, String password) {
        authManagementPort.toggleStatus(username, password);
    }

    // --- Trainee ---
    public Trainee createTrainee(Trainee trainee) {
        logger.info("Facade: Creating trainee");
        return traineeManagementPort.createTrainee(trainee);
    }

    public Trainee updateTrainee(String username, String password, Trainee trainee) {
        return traineeManagementPort.updateTrainee(username, password, trainee);
    }

    public Trainee getTrainee(String username) {
        return traineeManagementPort.getTrainee(username);
    }

    public void deleteTrainee(String username, String password) {
        logger.info("Facade: Deleting trainee: {}", username);
        traineeManagementPort.deleteTrainee(username, password);
    }

    // --- Trainer ---
    public Trainer createTrainer(Trainer trainer) {
        logger.info("Facade: Creating trainer");
        return trainerManagementPort.createTrainer(trainer);
    }

    public Trainer updateTrainer(String username, String password, Trainer trainer) {
        return trainerManagementPort.updateTrainer(username, password, trainer);
    }

    public Trainer getTrainer(String username) {
        return trainerManagementPort.getTrainer(username);
    }

    // --- Training (Reportes Dinámicos) ---
    public Training createTraining(Training training) {
        return trainingManagementPort.createTraining(training);
    }

    public List<Training> getTraineeTrainings(String username, LocalDate from, LocalDate to, String trainerName, String type) {
        return trainingManagementPort.getTraineeTrainings(username, from, to, trainerName, type);
    }

    public List<Training> getTrainerTrainings(String username, LocalDate from, LocalDate to, String traineeName) {
        return trainingManagementPort.getTrainerTrainings(username, from, to, traineeName);
    }

    // --- TrainingType (Catálogo) ---
    public TrainingType createTrainingType(TrainingType type) {
        return trainingTypeManagementPort.create(type);
    }

    public TrainingType getTrainingType(Long id) {
        return trainingTypeManagementPort.get(id);
    }

    // --- User Management ---
    public User getUserByUsername(String username) {
        return userManagementPort.getUserByUsername(username);
    }
}