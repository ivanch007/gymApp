package org.gym.util.validator;

import org.gym.domain.model.User;

public final class UserValidator {

    private UserValidator() {}

    public static void validateUserForCreate(User user){

        if (user == null)
            throw new IllegalArgumentException("User cannot be null");

        if (user.getFirstName() == null || user.getFirstName().isBlank())
            throw new IllegalArgumentException("First name is required");

        if (user.getLastName() == null || user.getLastName().isBlank())
            throw new IllegalArgumentException("Last name is required");
    }

    public static void validateForUpdate(User user) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");

        if (user.getId() == null)
            throw new IllegalArgumentException("User ID is required for update");

        validateUserForCreate(user);
    }
}
