// java
package org.gym.application.service;

import jakarta.persistence.EntityNotFoundException;
import org.gym.domain.model.Trainee;
import org.gym.domain.model.User;
import org.gym.domain.port.out.TraineeRepositoryPort;
import org.gym.domain.port.out.UserRepositoryPort;
import org.gym.util.generator.PasswordGenerator;
import org.gym.util.generator.UserNameGenerator;
import org.gym.util.validator.TraineeValidator;
import org.gym.util.validator.AuthValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceTest {

    private TraineeRepositoryPort traineeRepo;
    private UserRepositoryPort userRepo;
    private AuthValidator authValidator;
    private TraineeService traineeService;

    @BeforeEach
    void setUp() {
        traineeRepo = mock(TraineeRepositoryPort.class);
        userRepo = mock(UserRepositoryPort.class);
        authValidator = mock(AuthValidator.class);

        traineeService = new TraineeService(traineeRepo, userRepo, authValidator);
    }

    @Test
    void createTrainee_ShouldSaveWithGeneratedCredentials_WhenValid() {
        Trainee trainee = new Trainee();
        User user = new User();
        trainee.setUser(user);
        trainee.setAddress("Calle X");
        trainee.setDateOfBirth(LocalDate.of(1990,1,1));

        when(traineeRepo.save(any())).thenAnswer(inv -> {
            Trainee t = inv.getArgument(0);
            t.setId(42L);
            return t;
        });

        try (MockedStatic<TraineeValidator> v = mockStatic(TraineeValidator.class);
             MockedStatic<UserNameGenerator> u = mockStatic(UserNameGenerator.class);
             MockedStatic<PasswordGenerator> p = mockStatic(PasswordGenerator.class)) {

            v.when(() -> TraineeValidator.validateForCreate(trainee)).thenAnswer(inv -> null);
            u.when(() -> UserNameGenerator.generateUserNameUnique(user, userRepo)).thenReturn("genUser");
            p.when(() -> PasswordGenerator.generateRandomPassword(10)).thenReturn("genPwd");

            Trainee saved = traineeService.createTrainee(trainee);

            assertNotNull(saved.getId());
            assertEquals(42L, saved.getId());
            assertEquals("genUser", saved.getUser().getUsername());
            assertEquals("genPwd", saved.getUser().getPassword());
            assertTrue(saved.getUser().isActive());
            verify(traineeRepo).save(trainee);
        }
    }

    @Test
    void createTrainee_ShouldNotSave_WhenValidatorThrows() {
        Trainee trainee = new Trainee();
        trainee.setUser(new User());

        try (MockedStatic<TraineeValidator> v = mockStatic(TraineeValidator.class)) {
            v.when(() -> TraineeValidator.validateForCreate(trainee))
                    .thenThrow(new IllegalArgumentException("invalid trainee"));

            assertThrows(IllegalArgumentException.class, () -> traineeService.createTrainee(trainee));
            verifyNoInteractions(traineeRepo);
        }
    }

    @Test
    void updateTrainee_ShouldValidateCredentialsAndSave_WhenExisting() {
        String username = "tuser";
        String password = "pwd";

        Trainee existing = new Trainee();
        existing.setId(10L);
        existing.setAddress("Old Addr");
        User existingUser = new User();
        existingUser.setFirstName("Old");
        existingUser.setLastName("OldLast");
        existing.setUser(existingUser);

        when(traineeRepo.findByUsername(username)).thenReturn(existing);
        when(traineeRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Trainee incoming = new Trainee();
        incoming.setAddress("New Addr");
        User incomingUser = new User();
        incomingUser.setFirstName("New");
        incomingUser.setLastName("NewLast");
        incoming.setUser(incomingUser);

        // authValidator default (no exception) => passes
        Trainee result = traineeService.updateTrainee(username, password, incoming);

        assertEquals(10L, result.getId());
        assertEquals("New Addr", result.getAddress());
        assertEquals("New", result.getUser().getFirstName());
        assertEquals("NewLast", result.getUser().getLastName());
        verify(authValidator).validateCredentials(username, password);
        verify(traineeRepo).save(existing);
    }

    @Test
    void updateTrainee_ShouldThrow_WhenAuthFails() {
        String username = "tuser";
        String password = "bad";

        Trainee incoming = new Trainee();

        doThrow(new SecurityException("bad creds"))
                .when(authValidator).validateCredentials(username, password);

        assertThrows(SecurityException.class,
                () -> traineeService.updateTrainee(username, password, incoming));
        verify(traineeRepo, never()).save(any());
    }

    @Test
    void updateTrainee_ShouldThrowEntityNotFound_WhenMissing() {
        String username = "missing";
        String password = "p";

        when(traineeRepo.findByUsername(username)).thenReturn(null);

        Trainee incoming = new Trainee();

        assertThrows(EntityNotFoundException.class,
                () -> traineeService.updateTrainee(username, password, incoming));
        verify(traineeRepo, never()).save(any());
    }

    @Test
    void getTrainee_ShouldReturnEntity_WhenFound() {
        String username = "juan";
        Trainee expected = new Trainee();
        expected.setId(5L);
        expected.setUser(new User());
        expected.getUser().setUsername(username);

        when(traineeRepo.findByUsername(username)).thenReturn(expected);

        Trainee result = traineeService.getTrainee(username);

        assertSame(expected, result);
        verify(traineeRepo).findByUsername(username);
    }


    @Test
    void deleteTrainee_ShouldValidateCredentialsAndDelete() {
        String username = "toDelete";
        String password = "pwd";

        traineeService.deleteTrainee(username, password);

        verify(authValidator).validateCredentials(username, password);
        verify(traineeRepo).delete(username);
    }

    @Test
    void deleteTrainee_ShouldThrow_WhenAuthFails() {
        String username = "x";
        String password = "bad";

        doThrow(new SecurityException("nope")).when(authValidator).validateCredentials(username, password);

        assertThrows(SecurityException.class, () -> traineeService.deleteTrainee(username, password));
        verify(traineeRepo, never()).delete(any());
    }
}
