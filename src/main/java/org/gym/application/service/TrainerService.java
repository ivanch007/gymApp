package org.gym.application.service;

import org.gym.domain.model.Trainer;
import org.gym.domain.port.in.TrainerManagementPort;
import org.gym.domain.port.out.TrainerRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainerService implements TrainerManagementPort {

    private TrainerRepositoryPort trainerRepositoryPort;

    @Autowired
    public void setRepo(TrainerRepositoryPort trainerRepositoryPort) {
        this.trainerRepositoryPort = trainerRepositoryPort;
    }


    @Override
    public Trainer createTrainer(Trainer trainer) {
        if(trainer.getId() == null){
            trainer.setId(System.currentTimeMillis());
        }
        return trainerRepositoryPort.save(trainer);
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        return trainerRepositoryPort.save(trainer);
    }

    @Override
    public Trainer getTrainer(Long id) {
        return trainerRepositoryPort.findById(id);
    }
}
