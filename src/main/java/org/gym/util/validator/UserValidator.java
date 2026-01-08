package org.gym.util.validator;

import org.gym.domain.model.User;

public final class UserValidator {

    private UserValidator() {}

    public static void validateUserForCreate(User user) {
        if (user == null)
            throw new IllegalArgumentException("User object cannot be null");

        if (user.getFirstName() == null || user.getFirstName().isBlank())
            throw new IllegalArgumentException("First name is required and cannot be blank");

        if (user.getLastName() == null || user.getLastName().isBlank())
            throw new IllegalArgumentException("Last name is required and cannot be blank");
    }

    public static void validateForUpdate(User user) {
        if (user == null)
            throw new IllegalArgumentException("User object cannot be null");

        if (user.getUsername() == null || user.getUsername().isBlank())
            throw new IllegalArgumentException("Username is required for update operations");

        validateUserForCreate(user);
    }
}
