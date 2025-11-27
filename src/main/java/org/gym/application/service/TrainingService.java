package org.gym.application.service;

import org.gym.domain.model.Training;
import org.gym.domain.port.in.TrainingManagementPort;
import org.gym.domain.port.out.TrainingRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingService implements TrainingManagementPort {

    private TrainingRepositoryPort trainingRepositoryPort;

    @Autowired
    public void setRepo(TrainingRepositoryPort trainingRepositoryPort) {
        this.trainingRepositoryPort = trainingRepositoryPort;
    }

    @Override
    public Training createTraining(Training training) {
        if (training.getId() == null) {
            training.setId(System.currentTimeMillis());
        }
        return trainingRepositoryPort.save(training);
    }

    @Override
    public Training getTraining(Long id) {
        return trainingRepositoryPort.findById(id);
    }
}
