package org.gym.application.service;

import org.gym.domain.model.Trainer;
import org.gym.domain.model.User;
import org.gym.domain.port.in.TrainerManagementPort;
import org.gym.domain.port.out.TrainerRepositoryPort;
import org.gym.domain.port.out.UserRepositoryPort;
import org.gym.util.generator.IdGenerator;
import org.gym.util.validator.TrainerValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TrainerService implements TrainerManagementPort {

    private final TrainerRepositoryPort trainerRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    public TrainerService(TrainerRepositoryPort trainerRepositoryPort, UserRepositoryPort userRepositoryPort) {
        this.trainerRepositoryPort = Objects.requireNonNull(trainerRepositoryPort);
        this.userRepositoryPort = Objects.requireNonNull(userRepositoryPort);
    }


    @Override
    public Trainer createTrainer(Trainer trainer) {

        logger.info("createTrainer start for userId={}", trainer.getUserId());
        TrainerValidator.validateForCreate(trainer);

        User user = userRepositoryPort.findById(trainer.getUserId());
        if(user == null) {
            throw new IllegalArgumentException("Associated user not found with id=" + trainer.getUserId());
        }
        if(trainer.getId() == null){
            trainer.setId(IdGenerator.generateId());
        }
        Trainer saved = trainerRepositoryPort.save(trainer);
        logger.info("createTrainer - done id={}", saved.getId());
        return saved;
    }

    @Override
    public Trainer updateTrainer(Trainer trainer) {
        logger.info("updateTrainer - start id={}", trainer.getId());

        TrainerValidator.validateForUpdate(trainer);

        Trainer saved = trainerRepositoryPort.save(trainer);
        logger.info("updateTrainer - done id={}", saved.getId());
        return saved;
    }

    @Override
    public Trainer getTrainer(Long id) {
        logger.debug("getTrainer id={}", id);
        return trainerRepositoryPort.findById(id);
    }
}
