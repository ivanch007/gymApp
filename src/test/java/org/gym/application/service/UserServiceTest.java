package org.gym.application.service;

import org.gym.domain.model.User;
import org.gym.domain.port.out.UserRepositoryPort;
import org.gym.util.generator.IdGenerator;
import org.gym.util.generator.PasswordGenerator;
import org.gym.util.generator.UserNameGenerator;
import org.gym.util.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepositoryPort userRepo;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepo = mock(UserRepositoryPort.class);
        userService = new UserService(userRepo);
    }

    @Test
    void createUser_ShouldGenerateId_WhenIdIsNull() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        when(userRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        try (MockedStatic<UserValidator> uv = mockStatic(UserValidator.class);
             MockedStatic<IdGenerator> idg = mockStatic(IdGenerator.class);
             MockedStatic<UserNameGenerator> ung = mockStatic(UserNameGenerator.class);
             MockedStatic<PasswordGenerator> pg = mockStatic(PasswordGenerator.class)) {

            uv.when(() -> UserValidator.validateUserForCreate(user)).thenAnswer(inv -> null);
            idg.when(IdGenerator::generateId).thenReturn(111L);
            ung.when(() -> UserNameGenerator.generateUserNameUnique(user, userRepo)).thenReturn("john.doe");
            pg.when(() -> PasswordGenerator.generateRandomPassword(10)).thenReturn("pwd1234567");

            User result = userService.createUser(user);

            assertNotNull(result.getId());
            assertEquals(111L, result.getId());
            assertEquals("john.doe", result.getUsername());
            assertEquals("pwd1234567", result.getPassword());
            verify(userRepo).save(result);
        }
    }

    @Test
    void createUser_ShouldNotOverwriteId_WhenIdExists() {
        User user = new User();
        user.setId(100L);
        user.setFirstName("Ana");
        user.setLastName("Lopez");

        when(userRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        try (MockedStatic<UserValidator> uv = mockStatic(UserValidator.class);
             MockedStatic<UserNameGenerator> ung = mockStatic(UserNameGenerator.class);
             MockedStatic<PasswordGenerator> pg = mockStatic(PasswordGenerator.class)) {

            uv.when(() -> UserValidator.validateUserForCreate(user)).thenAnswer(inv -> null);
            ung.when(() -> UserNameGenerator.generateUserNameUnique(user, userRepo)).thenReturn("ana.lopez");
            pg.when(() -> PasswordGenerator.generateRandomPassword(10)).thenReturn("abcdef1234");

            User result = userService.createUser(user);

            assertEquals(100L, result.getId());
            assertEquals("ana.lopez", result.getUsername());
            verify(userRepo).save(result);
        }
    }

    @Test
    void createUser_ShouldGeneratePassword() {
        User user = new User();
        user.setFirstName("Mike");
        user.setLastName("Tyson");

        when(userRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        try (MockedStatic<UserValidator> uv = mockStatic(UserValidator.class);
             MockedStatic<UserNameGenerator> ung = mockStatic(UserNameGenerator.class);
             MockedStatic<PasswordGenerator> pg = mockStatic(PasswordGenerator.class);
             MockedStatic<IdGenerator> idg = mockStatic(IdGenerator.class)) {

            uv.when(() -> UserValidator.validateUserForCreate(user)).thenAnswer(inv -> null);
            ung.when(() -> UserNameGenerator.generateUserNameUnique(user, userRepo)).thenReturn("mike.tyson");
            pg.when(() -> PasswordGenerator.generateRandomPassword(10)).thenReturn("passw0rd10");
            idg.when(IdGenerator::generateId).thenReturn(222L);

            User result = userService.createUser(user);

            assertNotNull(result.getPassword());
            assertEquals(10, result.getPassword().length());
            assertEquals("passw0rd10", result.getPassword());
        }
    }

    @Test
    void createUser_ShouldGenerateUniqueUsername_WhenDuplicateExists() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        User existing = new User();
        existing.setUsername("john.doe");

        when(userRepo.findByUsername("john.doe")).thenReturn(existing);
        when(userRepo.findByUsername("john.doe1")).thenReturn(null);

        when(userRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        try (MockedStatic<UserValidator> uv = mockStatic(UserValidator.class);
             MockedStatic<PasswordGenerator> pg = mockStatic(PasswordGenerator.class);
             MockedStatic<IdGenerator> idg = mockStatic(IdGenerator.class)) {

            uv.when(() -> UserValidator.validateUserForCreate(user)).thenAnswer(inv -> null);
            pg.when(() -> PasswordGenerator.generateRandomPassword(10)).thenReturn("pwd1111111");
            idg.when(IdGenerator::generateId).thenReturn(333L);

            User result = userService.createUser(user);

            assertEquals("john.doe1", result.getUsername());
        }
    }

    @Test
    void createUser_ShouldGenerateJohnDoe2_WhenJohnDoeAndJohnDoe1Exist() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        User u1 = new User(); u1.setUsername("john.doe");
        User u2 = new User(); u2.setUsername("john.doe1");

        when(userRepo.findByUsername("john.doe")).thenReturn(u1);
        when(userRepo.findByUsername("john.doe1")).thenReturn(u2);
        when(userRepo.findByUsername("john.doe2")).thenReturn(null);

        when(userRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        try (MockedStatic<UserValidator> uv = mockStatic(UserValidator.class);
             MockedStatic<PasswordGenerator> pg = mockStatic(PasswordGenerator.class);
             MockedStatic<IdGenerator> idg = mockStatic(IdGenerator.class)) {

            uv.when(() -> UserValidator.validateUserForCreate(user)).thenAnswer(inv -> null);
            pg.when(() -> PasswordGenerator.generateRandomPassword(10)).thenReturn("pwd2222222");
            idg.when(IdGenerator::generateId).thenReturn(444L);

            User result = userService.createUser(user);

            assertEquals("john.doe2", result.getUsername());
        }
    }


    @Test
    void createUser_ShouldThrow_WhenMissingFirstName() {
        User user = new User();
        user.setLastName("Perez");

        assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(user));
    }

    @Test
    void updateUser_ShouldSaveAndReturnUpdated() {
        User user = new User();
        user.setId(20L);
        user.setFirstName("Luis");
        user.setLastName("Martinez");

        when(userRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        try (MockedStatic<UserValidator> uv = mockStatic(UserValidator.class)) {
            uv.when(() -> UserValidator.validateForUpdate(user)).thenAnswer(inv -> null);

            User result = userService.updateUser(user);

            assertEquals(20L, result.getId());
            verify(userRepo).save(user);
        }
    }

    @Test
    void getUser_ShouldReturnEntity() {
        User expected = new User();
        expected.setId(77L);

        when(userRepo.findById(77L)).thenReturn(expected);

        User result = userService.getUser(77L);

        assertSame(expected, result);
    }

    @Test
    void getUser_ShouldReturnNull_WhenIdIsNull() {
        assertNull(userService.getUser(null));
    }
}