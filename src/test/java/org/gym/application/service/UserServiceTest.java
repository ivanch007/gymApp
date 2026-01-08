package org.gym.application.service;

import org.gym.domain.model.User;
import org.gym.domain.port.out.UserRepositoryPort;
import org.gym.util.generator.PasswordGenerator;
import org.gym.util.generator.UserNameGenerator;
import org.gym.util.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private UserService userService;

    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = new User();
        validUser.setFirstName("John");
        validUser.setLastName("Doe");
    }

    @Test
    void shouldCreateUserWhenUserIsValid() {
        // Arrange
        when(userRepositoryPort.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User u = invocation.getArgument(0);
                    u.setId(1L);
                    return u;
                });

        try (MockedStatic<UserValidator> uv = mockStatic(UserValidator.class);
             MockedStatic<UserNameGenerator> un = mockStatic(UserNameGenerator.class);
             MockedStatic<PasswordGenerator> pw = mockStatic(PasswordGenerator.class)) {

            uv.when(() -> UserValidator.validateUserForCreate(validUser)).thenAnswer(i -> null);
            un.when(() -> UserNameGenerator.generateUserNameUnique(validUser, userRepositoryPort)).thenReturn("generatedUser");
            pw.when(() -> PasswordGenerator.generateRandomPassword(10)).thenReturn("pwd123");

            // Act
            User result = userService.createUser(validUser);

            // Assert
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("generatedUser", result.getUsername());
            assertEquals("pwd123", result.getPassword());
            assertTrue(result.isActive());

            verify(userRepositoryPort, times(1)).save(result);
        }
    }

    @Test
    void shouldThrowExceptionWhenCreatingUserWithInvalidData() {
        // Arrange
        User invalidUser = new User();
        try (MockedStatic<UserValidator> uv = mockStatic(UserValidator.class)) {
            uv.when(() -> UserValidator.validateUserForCreate(invalidUser))
                    .thenThrow(new IllegalArgumentException("invalid"));

            // Act & Assert
            assertThrows(
                    IllegalArgumentException.class,
                    () -> userService.createUser(invalidUser)
            );

            verify(userRepositoryPort, never()).save(any());
        }
    }

    // ---------- updateUser ----------

    @Test
    void shouldUpdateUserWhenUserExists() {
        // Arrange
        User existingUser = new User();
        existingUser.setUsername("ivan.ocampo");
        existingUser.setFirstName("John");
        existingUser.setLastName("OldLastName");

        User incoming = new User();
        incoming.setUsername("ivan.ocampo");
        incoming.setFirstName("Ivan");
        incoming.setLastName("NewLastName");

        when(userRepositoryPort.findByUsername("ivan.ocampo"))
                .thenReturn(existingUser);

        when(userRepositoryPort.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.updateUser(incoming);

        // Assert
        assertEquals("NewLastName", result.getLastName());
        assertEquals("Ivan", result.getFirstName());
        verify(userRepositoryPort).findByUsername("ivan.ocampo");
        verify(userRepositoryPort).save(existingUser);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingUser() {
        // Arrange
        User user = new User();
        user.setUsername("not.exists");

        when(userRepositoryPort.findByUsername("not.exists"))
                .thenReturn(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUser(user)
        );

        assertEquals("User not found: not.exists", exception.getMessage());
        verify(userRepositoryPort, never()).save(any());
    }

    // ---------- getUserByUsername ----------

    @Test
    void shouldReturnUserWhenUsernameExists() {
        // Arrange
        User user = new User();
        user.setUsername("ivan.ocampo");

        when(userRepositoryPort.findByUsername("ivan.ocampo"))
                .thenReturn(user);

        // Act
        User result = userService.getUserByUsername("ivan.ocampo");

        // Assert
        assertNotNull(result);
        assertEquals("ivan.ocampo", result.getUsername());
    }

    @Test
    void shouldReturnNullWhenUsernameDoesNotExist() {
        // Arrange
        when(userRepositoryPort.findByUsername("unknown"))
                .thenReturn(null);

        // Act
        User result = userService.getUserByUsername("unknown");

        // Assert
        assertNull(result);
    }
}