package org.gym.domain.port.out;

import org.gym.domain.model.TrainingType;

import java.util.List;

public interface TrainingTypeRepositoryPort {
    TrainingType save(TrainingType trainingType);
    TrainingType findById(Long id);
    List<TrainingType> findAll();

}
