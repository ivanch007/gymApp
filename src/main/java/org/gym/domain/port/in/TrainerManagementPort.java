package org.gym.domain.port.in;

import org.gym.domain.model.Trainer;

public interface TrainerManagementPort {
    Trainer createTrainer(Trainer trainer);
    Trainer updateTrainer(Trainer trainer);
    Trainer getTrainer(Long id);

}
