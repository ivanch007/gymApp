package org.gym.infrastructure.dao;

import org.gym.domain.model.User;
import org.gym.domain.port.out.UserRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoAdapter implements UserRepositoryPort {

    private Map<Long, User> storage;

    @Autowired
    public void setStorage(@Qualifier("userStorage") Map<Long, User> storage) {
        this.storage = storage;
    }

    @Override
    public User save(User user) {
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public User findById(Long id) {
        return storage.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

}
