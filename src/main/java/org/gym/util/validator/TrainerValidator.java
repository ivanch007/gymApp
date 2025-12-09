package org.gym.util.validator;

import org.gym.domain.model.Trainer;

public final class TrainerValidator {

    private TrainerValidator() {}

    public static void validateForCreate(Trainer trainer) {
        if (trainer == null)
            throw new IllegalArgumentException("Trainer cannot be null");

        if (trainer.getUserId() == null)
            throw new IllegalArgumentException("Trainer.userId is required");

        if (trainer.getSpecialization() == null || trainer.getSpecialization().isBlank())
            throw new IllegalArgumentException("Trainer specialization is required");
    }

    public static void validateForUpdate(Trainer trainer) {
        if (trainer == null)
            throw new IllegalArgumentException("Trainer cannot be null");

        if (trainer.getId() == null)
            throw new IllegalArgumentException("Trainer ID is required for update");

        validateForCreate(trainer);
    }
}