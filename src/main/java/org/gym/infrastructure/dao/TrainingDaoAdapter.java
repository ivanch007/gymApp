package org.gym.infrastructure.dao;

import org.gym.domain.model.Training;
import org.gym.domain.port.out.TrainingRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TrainingDaoAdapter implements TrainingRepositoryPort {

    private Map<Long, Training> storage;

    @Autowired
    public void setStorage(@Qualifier("trainingStorage") Map<Long, Training> storage) {
        this.storage = storage;
    }


    @Override
    public Training save(Training training) {
        storage.put(training.getId(), training);
        return training;
    }

    @Override
    public Training findById(Long id) {
        return storage.get(id);
    }

    @Override
    public List<Training> findAll() {
        return new ArrayList<>(storage.values());
    }

}
