package org.gym.application.service;

import org.gym.domain.model.TrainingType;
import org.gym.domain.port.in.TrainingTypeManagementPort;
import org.gym.domain.port.out.TrainingTypeRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingTypeService implements TrainingTypeManagementPort {

    private TrainingTypeRepositoryPort trainingTypeRepositoryPort;

    public TrainingTypeService(TrainingTypeRepositoryPort trainingTypeRepositoryPort) {
        this.trainingTypeRepositoryPort = trainingTypeRepositoryPort;
    }

    @Autowired
    public void setRepo(TrainingTypeRepositoryPort trainingTypeRepositoryPort) {
        this.trainingTypeRepositoryPort = trainingTypeRepositoryPort;
    }

    @Override
    public TrainingType create(TrainingType trainingType) {
        if (trainingType.getId() == null) {
            trainingType.setId(System.currentTimeMillis());
        }
        return trainingTypeRepositoryPort.save(trainingType);
    }

    @Override
    public TrainingType get(Long id) {
        return trainingTypeRepositoryPort.findById(id);
    }

    @Override
    public TrainingType update(TrainingType trainingType) {
        return trainingTypeRepositoryPort.save(trainingType);
    }
}
