package org.gym.domain.port.in;

import org.gym.domain.model.Trainee;

public interface TraineeManagementPort {
    Trainee createTrainee(Trainee trainee);
    Trainee updateTrainee(String  username, String password, Trainee trainee);
    Trainee getTrainee(String username);
    void deleteTrainee(String username, String password);
}
