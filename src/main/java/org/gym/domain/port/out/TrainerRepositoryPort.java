package org.gym.domain.port.out;

import org.gym.domain.model.Trainer;

import java.util.List;

public interface TrainerRepositoryPort {
    Trainer save(Trainer trainer);
    Trainer findById(Long id);
    List<Trainer> findAll();

}
