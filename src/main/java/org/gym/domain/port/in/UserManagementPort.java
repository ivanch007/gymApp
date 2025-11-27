package org.gym.domain.port.in;

import org.gym.domain.model.User;

public interface UserManagementPort {
    User createUser(User user);
    User updateUser(User user);
    User getUser(Long id);
}
