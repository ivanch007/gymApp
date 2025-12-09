package org.gym.infrastructure.dao;

import org.gym.domain.model.Trainee;
import org.gym.domain.port.out.TraineeRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TraineeDaoAdapter implements TraineeRepositoryPort {

    private static final Logger logger = LoggerFactory.getLogger(TraineeDaoAdapter.class);
    private final Map<Long, Trainee> storage;

    public TraineeDaoAdapter(@Qualifier("traineeStorage") Map<Long, Trainee> storage) {
        this.storage = storage;
    }

    @Override
    public Trainee save(Trainee trainee) {
        logger.debug("Saving Trainee id={}", trainee.getId());
        storage.put(trainee.getId(), trainee);
        return trainee;
    }

    @Override
    public Trainee findById(Long id) {
        logger.debug("Finding Trainee id={}", id);
        return storage.get(id);
    }

    @Override
    public List<Trainee> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(Long id) {
        logger.debug("Deleting Trainee id={}", id);
        storage.remove(id);

    }
}
