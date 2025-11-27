package org.gym.domain.port.out;

import org.gym.domain.model.Training;

import java.util.List;

public interface TrainingRepositoryPort {
    Training save(Training training);
    Training findById(Long id);
    List<Training> findAll();
}
