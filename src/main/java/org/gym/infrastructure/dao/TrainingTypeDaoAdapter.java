package org.gym.infrastructure.dao;

import org.gym.domain.model.TrainingType;
import org.gym.domain.port.out.TrainingTypeRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Repository
public class TrainingTypeDaoAdapter implements TrainingTypeRepositoryPort {

    private static final Logger logger = LoggerFactory.getLogger(TrainingTypeDaoAdapter.class);

    private final Map<Long, TrainingType> storage;

    public TrainingTypeDaoAdapter(@Qualifier("trainingTypeStorage") Map<Long, TrainingType> storage) {
        this.storage = storage;
    }

    @Override
    public TrainingType save(TrainingType trainingType) {
        logger.debug("Saving TrainingType id={}", trainingType.getId());
        storage.put(trainingType.getId(), trainingType);
        return trainingType;
    }

    @Override
    public TrainingType findById(Long id) {
        logger.debug("Finding TrainingType id={}", id);
        return storage.get(id);
    }

    @Override
    public List<TrainingType> findAll() {
        return new ArrayList<>(storage.values());
    }
}
