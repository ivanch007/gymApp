package org.gym.infrastructure.dao;

import org.gym.domain.model.Training;
import org.gym.domain.port.out.TrainingRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TrainingDaoAdapter implements TrainingRepositoryPort {

    private static final Logger logger = LoggerFactory.getLogger(TrainingDaoAdapter.class);

    private final Map<Long, Training> storage;

    @Autowired
    public TrainingDaoAdapter(@Qualifier("trainingStorage") Map<Long, Training> storage) {
        this.storage = storage;
    }


    @Override
    public Training save(Training training) {
        logger.debug("Saving Training id={}", training.getId());
        storage.put(training.getId(), training);
        return training;
    }

    @Override
    public Training findById(Long id) {
        logger.debug("Finding Training id={}", id);
        return storage.get(id);
    }

    @Override
    public List<Training> findAll() {
        return new ArrayList<>(storage.values());
    }

}
