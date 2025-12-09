package org.gym.util.generator;

import org.gym.domain.model.User;
import org.gym.domain.port.out.UserRepositoryPort;

public final class UserNameGenerator {

    private UserNameGenerator() {

    }

    public static String generateUserNameUnique(User user, UserRepositoryPort userRepositoryPort) {
        String baseUserName = (user.getFirstName() == null ? "" : user.getFirstName().trim())
                + "." + (user.getLastName() == null ? "" : user.getLastName().trim());
        baseUserName = baseUserName.replaceAll("\\s+", "");

        baseUserName = baseUserName.toLowerCase();
        if(baseUserName.equals(".")) {
            baseUserName = "user";
        }

        String userName = baseUserName;
        int counter = 1;
        while(userRepositoryPort.findByUsername(userName) != null){
            userName = baseUserName + counter;
            counter++;
        }
        return userName;
    }
}
