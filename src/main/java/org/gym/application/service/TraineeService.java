package org.gym.application.service;

import org.gym.domain.model.Trainee;
import org.gym.domain.port.in.TraineeManagementPort;
import org.gym.domain.port.out.TraineeRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraineeService implements TraineeManagementPort {

    private TraineeRepositoryPort traineeRepositoryPort;

    @Autowired
    public void setRepo(TraineeRepositoryPort traineeRepositoryPort) {
        this.traineeRepositoryPort = traineeRepositoryPort;
    }


    @Override
    public Trainee createTrainee(Trainee trainee) {

        if (trainee.getId() == null) {
            trainee.setId(System.currentTimeMillis());
        }
        return traineeRepositoryPort.save(trainee);
    }

    @Override
    public Trainee updateTrainee(Trainee trainee) {
        return traineeRepositoryPort.save(trainee);
    }

    @Override
    public Trainee getTrainee(Long id) {
        return traineeRepositoryPort.findById(id);
    }

    @Override
    public void deleteTrainee(Long id) {
        traineeRepositoryPort.delete(id);
    }
}
