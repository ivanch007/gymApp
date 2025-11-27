package org.gym.domain.port.in;

import org.gym.domain.model.Training;

public interface TrainingManagementPort {
    Training createTraining(Training training);
    Training getTraining(Long id);
}
