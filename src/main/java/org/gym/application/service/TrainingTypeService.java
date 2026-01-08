package org.gym.application.service;

import org.gym.domain.model.TrainingType;
import org.gym.domain.port.in.TrainingTypeManagementPort;
import org.gym.domain.port.out.TrainingTypeRepositoryPort;
import org.gym.util.validator.TrainingTypeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.Objects;

@Service
public class TrainingTypeService implements TrainingTypeManagementPort {

    private final TrainingTypeRepositoryPort trainingTypeRepositoryPort;
    private static final Logger logger = LoggerFactory.getLogger(TrainingTypeService.class);

    public TrainingTypeService(TrainingTypeRepositoryPort trainingTypeRepositoryPort) {
        this.trainingTypeRepositoryPort = Objects.requireNonNull(trainingTypeRepositoryPort);
    }

    @Override
    @Transactional
    public TrainingType create(TrainingType trainingType) {
        logger.info("Service: Starting TrainingType creation for name: {}", trainingType.getTrainingTypeName());

        TrainingTypeValidator.validateForCreate(trainingType);

        TrainingType saved = trainingTypeRepositoryPort.save(trainingType);

        logger.info("Service: TrainingType created successfully with ID: {}", saved.getId());
        return saved;
    }

    @Override
    public TrainingType get(Long id) {
        logger.debug("Service: Fetching TrainingType with ID: {}", id);
        if (id == null) return null;
        return trainingTypeRepositoryPort.findById(id);
    }

    @Override
    @Transactional
    public TrainingType update(TrainingType trainingType) {
        logger.info("Service: Updating TrainingType with ID: {}", trainingType.getId());

        TrainingTypeValidator.validateForUpdate(trainingType);

        if (trainingTypeRepositoryPort.findById(trainingType.getId()) == null) {
            logger.error("Service: Update failed. TrainingType not found with ID: {}", trainingType.getId());
            throw new IllegalArgumentException("TrainingType not found with ID: " + trainingType.getId());
        }

        TrainingType saved = trainingTypeRepositoryPort.save(trainingType);
        logger.info("Service: TrainingType updated successfully for ID: {}", saved.getId());
        return saved;
    }
}