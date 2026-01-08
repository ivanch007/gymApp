package org.gym.util.validator;

import org.gym.domain.model.Training;

public final class TrainingValidator {

    private TrainingValidator() {}

    public static void validateForCreate(Training training) {
        if (training == null)
            throw new IllegalArgumentException("Training object cannot be null");

        if (training.getTrainer() == null)
            throw new IllegalArgumentException("Trainer is required");

        if (training.getTrainee() == null)
            throw new IllegalArgumentException("Trainee is required");

        if (training.getTrainingType() == null)
            throw new IllegalArgumentException("Training type is required");

        if (training.getTrainingName() == null || training.getTrainingName().isBlank())
            throw new IllegalArgumentException("Training name is required");

        if (training.getDate() == null)
            throw new IllegalArgumentException("Training date is required");

        if (training.getDuration() == null || training.getDuration() <= 0)
            throw new IllegalArgumentException("Training duration must be a positive number");
    }
}