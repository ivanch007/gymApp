package org.gym.util.validator;

import org.gym.domain.model.Trainee;

public final class TraineeValidator {

    private TraineeValidator() {}

    public static void validateForCreate(Trainee trainee) {
        if (trainee == null)
            throw new IllegalArgumentException("Trainee cannot be null");

        if (trainee.getUser() == null)
            throw new IllegalArgumentException("User information is required");
    }
}