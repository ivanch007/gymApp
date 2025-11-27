package org.gym.domain.port.in;

import org.gym.domain.model.TrainingType;

public interface TrainingTypeManagementPort {
    TrainingType create(TrainingType trainingType);
    TrainingType get(Long id);
    TrainingType update(TrainingType trainingType);
}
