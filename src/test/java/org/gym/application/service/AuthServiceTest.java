package org.gym.application.service;

import org.gym.domain.model.User;
import org.gym.domain.port.out.UserRepositoryPort;
import org.gym.util.validator.AuthValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private AuthValidator authValidator;

    @InjectMocks
    private AuthService authService;

    private final String username = "user1";
    private final String password = "pass";

    @BeforeEach
    void setUp() {
        // mocks are initialized by MockitoExtension and injected into authService
    }

    @Test
    void authenticate_ShouldReturnTrue_WhenCredentialsValid() {
        // Arrange: validator returns a User (credentials valid)
        User u = new User();
        u.setUsername(username);
        when(authValidator.validateCredentials(username, password)).thenReturn(u);

        // Act
        boolean result = authService.authenticate(username, password);

        // Assert
        assertTrue(result);
        verify(authValidator).validateCredentials(username, password);
        verifyNoInteractions(userRepositoryPort);
    }

    @Test
    void authenticate_ShouldReturnFalse_WhenCredentialsInvalid() {
        // Arrange: validator throws SecurityException for invalid creds
        when(authValidator.validateCredentials(username, "bad"))
                .thenThrow(new SecurityException("bad creds"));

        // Act
        boolean result = authService.authenticate(username, "bad");

        // Assert
        assertFalse(result);
        verify(authValidator).validateCredentials(username, "bad");
        verifyNoInteractions(userRepositoryPort);
    }

    @Test
    void changePassword_ShouldUpdatePasswordAndSave_WhenCredentialsValid() {
        // Arrange: validator returns existing user; repository saves and returns the same instance
        User existing = new User();
        existing.setUsername(username);
        existing.setPassword(password);

        when(authValidator.validateCredentials(username, password)).thenReturn(existing);
        when(userRepositoryPort.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        String newPassword = "newSecret";

        // Act
        authService.changePassword(username, password, newPassword);

        // Assert
        assertEquals(newPassword, existing.getPassword());
        verify(authValidator).validateCredentials(username, password);
        verify(userRepositoryPort).save(existing);
    }

    @Test
    void changePassword_ShouldPropagate_WhenAuthFails() {
        // Arrange: validator throws SecurityException
        when(authValidator.validateCredentials(username, "bad"))
                .thenThrow(new SecurityException("nope"));

        // Act & Assert
        SecurityException ex = assertThrows(SecurityException.class,
                () -> authService.changePassword(username, "bad", "whatever"));

        assertEquals("nope", ex.getMessage());
        verify(authValidator).validateCredentials(username, "bad");
        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    void toggleStatus_ShouldFlipActiveAndSave_WhenCredentialsValid() {
        // Arrange: validator returns a user with active = true
        User existing = new User();
        existing.setUsername(username);
        existing.setActive(true);

        when(authValidator.validateCredentials(username, password)).thenReturn(existing);
        when(userRepositoryPort.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        authService.toggleStatus(username, password);

        // Assert
        assertFalse(existing.isActive());
        verify(authValidator).validateCredentials(username, password);
        verify(userRepositoryPort).save(existing);
    }

    @Test
    void toggleStatus_ShouldPropagate_WhenAuthFails() {
        // Arrange: validator throws SecurityException
        when(authValidator.validateCredentials(username, "bad"))
                .thenThrow(new SecurityException("denied"));

        // Act & Assert
        SecurityException ex = assertThrows(SecurityException.class,
                () -> authService.toggleStatus(username, "bad"));

        assertEquals("denied", ex.getMessage());
        verify(authValidator).validateCredentials(username, "bad");
        verify(userRepositoryPort, never()).save(any());
    }
}
