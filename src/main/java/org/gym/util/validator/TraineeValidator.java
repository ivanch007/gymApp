package org.gym.util.validator;

import org.gym.domain.model.Trainee;

public final class TraineeValidator {

    private TraineeValidator() {}

    public static void validateForCreate(Trainee trainee) {
        if (trainee == null)
            throw new IllegalArgumentException("Trainee cannot be null");

        if (trainee.getUserId() == null)
            throw new IllegalArgumentException("Trainee.userId is required");

        if (trainee.getAddress() == null || trainee.getAddress().isBlank())
            throw new IllegalArgumentException("Trainee address is required");

        if (trainee.getDateOfBirth() == null)
            throw new IllegalArgumentException("Trainee dateOfBirth is required");
    }

    public static void validateForUpdate(Trainee trainee) {
        if (trainee == null)
            throw new IllegalArgumentException("Trainee cannot be null");

        if (trainee.getId() == null)
            throw new IllegalArgumentException("Trainee ID is required for update");

        validateForCreate(trainee);
    }
}