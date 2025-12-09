package org.gym.util.validator;

import org.gym.domain.model.TrainingType;

public final class TrainingTypeValidator {

    private TrainingTypeValidator(){}

    public static void validateForCreate(TrainingType trainingType){

        if (trainingType == null) {
            throw new IllegalArgumentException("TrainingType cannot be null");
        }
        if (trainingType.getTrainingTypeName() == null || trainingType.getTrainingTypeName().isBlank()) {
            throw new IllegalArgumentException("TrainingType name is required");
        }
    }

    public static void validateForUpdate(TrainingType trainingType) {
        if (trainingType == null)
            throw new IllegalArgumentException("TrainingType cannot be null");

        if (trainingType.getId() == null)
            throw new IllegalArgumentException("TrainingType ID is required for update");

        validateForCreate(trainingType);
    }
}
