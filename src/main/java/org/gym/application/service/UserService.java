package org.gym.application.service;

import org.gym.domain.model.User;
import org.gym.domain.port.in.UserManagementPort;
import org.gym.domain.port.out.UserRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserManagementPort {

    private UserRepositoryPort userRepositoryPort;
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final java.security.SecureRandom RANDOM = new java.security.SecureRandom();

    @Autowired
    public void setRepo(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    private String generateRandomPassword(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }


    @Override
    public User createUser(User user) {

        if(user.getId() == null){
            user.setId(System.currentTimeMillis());
        }
        String username = user.getFirstName() + "." + user.getLastName();
        user.setUsername(username);
        user.setPassword(generateRandomPassword(10));

        return userRepositoryPort.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepositoryPort.save(user);
    }

    @Override
    public User getUser(Long id) {
        return userRepositoryPort.findById(id);
    }
}
