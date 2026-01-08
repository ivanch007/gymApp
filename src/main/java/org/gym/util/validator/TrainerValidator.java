package org.gym.util.validator;

import org.gym.domain.model.Trainer;

public final class TrainerValidator {

    private TrainerValidator() {}

    public static void validateForCreate(Trainer trainer) {
        if (trainer == null)
            throw new IllegalArgumentException("Trainer cannot be null");

        if (trainer.getUser() == null)
            throw new IllegalArgumentException("Trainer must be associated with a user");

        if (trainer.getSpecialization() == null)
            throw new IllegalArgumentException("Trainer specialization is required");
    }
}