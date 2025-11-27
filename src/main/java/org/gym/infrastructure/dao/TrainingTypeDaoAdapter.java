package org.gym.infrastructure.dao;

import org.gym.domain.model.TrainingType;
import org.gym.domain.port.out.TrainingTypeRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class TrainingTypeDaoAdapter implements TrainingTypeRepositoryPort {

    private Map<Long, TrainingType> storage;

    @Autowired
    public void setStorage(@Qualifier("trainingTypeStorage") Map<Long, TrainingType> storage) {
        this.storage = storage;
    }

    @Override
    public TrainingType save(TrainingType trainingType) {
        storage.put(trainingType.getId(), trainingType);
        return trainingType;
    }

    @Override
    public TrainingType findById(Long id) {
        return storage.get(id);
    }

    @Override
    public List<TrainingType> findAll() {
        return new ArrayList<>(storage.values());
    }
}
