package org.gym.util.validator;

import org.gym.domain.model.Training;

public final class TrainingValidator {

    private TrainingValidator() {}

    public static void validateForCreate(Training training) {
        if (training == null)
            throw new IllegalArgumentException("Training cannot be null");

        if (training.getTrainerUserId() == null)
            throw new IllegalArgumentException("Training.trainerUserId is required");

        if (training.getTraineeUserId() == null)
            throw new IllegalArgumentException("Training.traineeUserId is required");

        if (training.getTrainingTypeId() == null)
            throw new IllegalArgumentException("Training.trainingType is required");

        if (training.getTrainingName() == null || training.getTrainingName().isBlank())
            throw new IllegalArgumentException("Training name is required");

        if (training.getDate() == null)
            throw new IllegalArgumentException("Training date is required");

        if (training.getDuration() <= 0)
            throw new IllegalArgumentException("Training duration must be positive");
    }

    public static void validateForUpdate(Training training) {
        if (training == null)
            throw new IllegalArgumentException("Training cannot be null");

        if (training.getId() == null)
            throw new IllegalArgumentException("Training ID is required for update");

        validateForCreate(training);
    }
}
