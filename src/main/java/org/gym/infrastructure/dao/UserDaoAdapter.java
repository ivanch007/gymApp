package org.gym.infrastructure.dao;

import org.gym.domain.model.User;
import org.gym.domain.port.out.UserRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoAdapter implements UserRepositoryPort {

    private final Map<Long, User> storage;
    private final Logger logger = LoggerFactory.getLogger(UserDaoAdapter.class);

    public UserDaoAdapter(@Qualifier("userStorage") Map<Long, User> storage) {
        this.storage = storage;
    }

    @Override
    public User save(User user) {
        logger.debug("Saving user id={}", user.getId());
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public User findById(Long id) {
        logger.debug("Finding user id={}", id);
        return storage.get(id);
    }

    @Override
    public User findByUsername(String username) {
        logger.debug("Finding user by username={}", username);
        return storage.values().stream()
                .filter(u -> username.equals(u.getUsername()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findAll() {

        return new ArrayList<>(storage.values());
    }

}
