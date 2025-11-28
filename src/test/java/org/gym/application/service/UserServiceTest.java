// language: java
package org.gym.application.service;

import org.gym.domain.model.User;
import org.gym.domain.port.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepositoryPort userRepositoryPort;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepositoryPort = mock(UserRepositoryPort.class);
        userService = new UserService(userRepositoryPort);
    }

    @Test
    void create_ShouldGenerateId_WhenIdIsNull() {
        User user = new User();
        user.setFirstName("ivan");
        user.setLastName("perez");

        when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.createUser(user);

        assertNotNull(result.getId());
        assertEquals("ivan.perez", result.getUsername());
        verify(userRepositoryPort).save(result);
    }

    @Test
    void get_ShouldReturnUserFromRepository() {
        User expected = new User();
        expected.setId(42L);
        expected.setUsername("alice");

        when(userRepositoryPort.findById(42L)).thenReturn(expected);

        User actual = userService.getUser(42L);

        assertSame(expected, actual);
        verify(userRepositoryPort).findById(42L);
    }

    @Test
    void update_ShouldCallSaveAndReturnUpdated() {
        User user = new User();
        user.setId(5L);
        user.setUsername("old");

        when(userRepositoryPort.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updated = userService.updateUser(user);

        verify(userRepositoryPort).save(user);
        assertEquals("old", updated.getUsername());
    }
}
