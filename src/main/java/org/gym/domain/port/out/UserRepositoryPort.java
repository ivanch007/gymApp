package org.gym.domain.port.out;

import org.gym.domain.model.User;

import java.util.List;

public interface UserRepositoryPort {
    User save(User user);
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll();

}
