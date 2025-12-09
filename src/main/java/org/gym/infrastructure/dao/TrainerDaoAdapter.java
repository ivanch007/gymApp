package org.gym.infrastructure.dao;

import org.gym.domain.model.Trainer;
import org.gym.domain.port.out.TrainerRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TrainerDaoAdapter implements TrainerRepositoryPort {

    private static final Logger logger = LoggerFactory.getLogger(TrainerDaoAdapter.class);
    private final Map<Long, Trainer> storage;

    public TrainerDaoAdapter(@Qualifier("trainerStorage") Map<Long, Trainer> storage) {
        this.storage = storage;
    }

    @Override
    public Trainer save(Trainer trainer) {
        logger.debug("Saving Trainer id={}", trainer.getId());
        storage.put(trainer.getId(), trainer);
        return trainer;
    }

    @Override
    public Trainer findById(Long id) {
        logger.debug("Finding Trainer id={}", id);
        return storage.get(id);
    }

    @Override
    public List<Trainer> findAll() {
        return new ArrayList<>(storage.values());
    }

}
