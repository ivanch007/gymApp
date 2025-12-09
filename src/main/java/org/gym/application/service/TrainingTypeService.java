package org.gym.application.service;

import org.gym.domain.model.TrainingType;
import org.gym.domain.port.in.TrainingTypeManagementPort;
import org.gym.domain.port.out.TrainingTypeRepositoryPort;
import org.gym.util.generator.IdGenerator;
import org.gym.util.validator.TrainingTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TrainingTypeService implements TrainingTypeManagementPort {

    private final TrainingTypeRepositoryPort trainingTypeRepositoryPort;
    private static final Logger logger = LoggerFactory.getLogger(TrainingTypeService.class);

    public TrainingTypeService(TrainingTypeRepositoryPort trainingTypeRepositoryPort) {
        this.trainingTypeRepositoryPort = Objects.requireNonNull(trainingTypeRepositoryPort);
    }


    @Override
    public TrainingType create(TrainingType trainingType) {

        logger.info("create TrainingType start name={}", trainingType.getTrainingTypeName());
        TrainingTypeValidator.validateForCreate(trainingType);

        if (trainingType.getId() == null) {
            trainingType.setId(IdGenerator.generateId());
        }
        TrainingType saved = trainingTypeRepositoryPort.save(trainingType);
        logger.info("create TrainingType done id={}", saved.getId());
        return saved;
    }

    @Override
    public TrainingType get(Long id) {
        logger.debug("get TrainingType id={}", id);
        if (id == null) return null;
        return trainingTypeRepositoryPort.findById(id);
    }

    @Override
    public TrainingType update(TrainingType trainingType) {
        logger.info("update TrainingType id={}", trainingType.getId());
        TrainingTypeValidator.validateForUpdate(trainingType);


        TrainingType saved = trainingTypeRepositoryPort.save(trainingType);
        logger.info("update TrainingType done id={}", saved.getId());
        return saved;
    }
}
