package org.gym.infrastructure.dao;

import org.gym.domain.model.Trainer;
import org.gym.domain.port.out.TrainerRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TrainerDaoAdapter implements TrainerRepositoryPort {

    private Map<Long, Trainer> storage;

    @Autowired
    public void setStorage(@Qualifier("trainerStorage") Map<Long, Trainer> storage) {
        this.storage = storage;
    }

    @Override
    public Trainer save(Trainer trainer) {
        storage.put(trainer.getId(), trainer);
        return trainer;
    }

    @Override
    public Trainer findById(Long id) {
        return storage.get(id);
    }

    @Override
    public List<Trainer> findAll() {
        return new ArrayList<>(storage.values());
    }

}
