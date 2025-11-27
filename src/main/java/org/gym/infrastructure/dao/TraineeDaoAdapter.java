package org.gym.infrastructure.dao;

import org.gym.domain.model.Trainee;
import org.gym.domain.port.out.TraineeRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TraineeDaoAdapter implements TraineeRepositoryPort {

    private Map<Long, Trainee> storage;

    @Autowired
    public void setStorage(@Qualifier("traineeStorage") Map<Long, Trainee> storage) {
        this.storage = storage;
    }

    @Override
    public Trainee save(Trainee trainee) {
        storage.put(trainee.getId(), trainee);
        return trainee;
    }

    @Override
    public Trainee findById(Long id) {
        return storage.get(id);
    }

    @Override
    public List<Trainee> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);

    }
}
