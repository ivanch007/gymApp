package org.gym.domain.port.in;

import org.gym.domain.model.Trainee;

public interface TraineeManagementPort {
    Trainee createTrainee(Trainee trainee);
    Trainee updateTrainee(Trainee trainee);
    Trainee getTrainee(Long id);
    void deleteTrainee(Long id);
}
