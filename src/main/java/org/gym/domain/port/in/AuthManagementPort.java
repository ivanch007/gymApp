package org.gym.domain.port.in;

public interface AuthManagementPort {
    boolean authenticate(String username, String password);
    void changePassword(String username, String oldPassword, String newPassword);
    void toggleStatus(String username, String password);
}
