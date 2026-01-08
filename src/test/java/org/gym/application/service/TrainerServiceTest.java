// java
package org.gym.application.service;

import org.gym.domain.model.Trainer;
import org.gym.domain.model.User;
import org.gym.domain.model.TrainingType;
import org.gym.domain.port.out.TrainerRepositoryPort;
import org.gym.domain.port.out.UserRepositoryPort;
import org.gym.util.generator.PasswordGenerator;
import org.gym.util.generator.UserNameGenerator;
import org.gym.util.validator.TrainerValidator;
import org.gym.util.validator.AuthValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    private TrainerRepositoryPort trainerRepo;
    private UserRepositoryPort userRepo;
    private AuthValidator authValidator;
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        trainerRepo = mock(TrainerRepositoryPort.class);
        userRepo = mock(UserRepositoryPort.class);
        authValidator = mock(AuthValidator.class);

        trainerService = new TrainerService(trainerRepo, userRepo, authValidator);
    }

    @Test
    void createTrainer_ShouldSaveWithGeneratedCredentials_WhenValid() {
        Trainer trainer = new Trainer();
        User user = new User();
        trainer.setUser(user);

        when(trainerRepo.save(any())).thenAnswer(i -> {
            Trainer t = i.getArgument(0);
            t.setId(1L);
            return t;
        });

        try (MockedStatic<TrainerValidator> v = mockStatic(TrainerValidator.class);
             MockedStatic<UserNameGenerator> u = mockStatic(UserNameGenerator.class);
             MockedStatic<PasswordGenerator> p = mockStatic(PasswordGenerator.class)) {

            v.when(() -> TrainerValidator.validateForCreate(trainer)).thenAnswer(inv -> null);
            u.when(() -> UserNameGenerator.generateUserNameUnique(user, userRepo)).thenReturn("generatedUser");
            p.when(() -> PasswordGenerator.generateRandomPassword(10)).thenReturn("pwd123");

            Trainer saved = trainerService.createTrainer(trainer);

            assertNotNull(saved.getId());
            assertEquals(1L, saved.getId());
            assertEquals("generatedUser", saved.getUser().getUsername());
            assertEquals("pwd123", saved.getUser().getPassword());
            assertTrue(saved.getUser().isActive());
            verify(trainerRepo).save(any(Trainer.class));
        }
    }

    @Test
    void createTrainer_ShouldNotSave_WhenValidatorThrows() {
        Trainer trainer = new Trainer();
        trainer.setUser(new User());

        try (MockedStatic<TrainerValidator> v = mockStatic(TrainerValidator.class)) {
            v.when(() -> TrainerValidator.validateForCreate(trainer))
                    .thenThrow(new IllegalArgumentException("invalid"));

            assertThrows(IllegalArgumentException.class, () -> trainerService.createTrainer(trainer));
            verifyNoInteractions(trainerRepo);
        }
    }

    @Test
    void getTrainer_ShouldReturnEntity_WhenFound() {
        String username = "u1";
        Trainer expected = new Trainer();
        expected.setId(10L);
        expected.setUser(new User());
        expected.getUser().setUsername(username);

        when(trainerRepo.findByUsername(username)).thenReturn(expected);

        Trainer result = trainerService.getTrainer(username);

        assertSame(expected, result);
        verify(trainerRepo).findByUsername(username);
    }

    @Test
    void getTrainer_ShouldReturnNull_WhenNotFound() {
        when(trainerRepo.findByUsername("nouser")).thenReturn(null);

        Trainer result = trainerService.getTrainer("nouser");

        assertNull(result);
        verify(trainerRepo).findByUsername("nouser");
    }

    @Test
    void updateTrainer_ShouldValidateCredentialsAndSave_WhenExisting() {
        String username = "trainer1";
        String password = "pass";

        Trainer existing = new Trainer();
        existing.setId(3L);
        TrainingType oldType = new TrainingType();
        oldType.setTrainingTypeName("oldSpec");
        existing.setSpecialization(oldType);

        User existingUser = new User();
        existingUser.setFirstName("Old");
        existingUser.setLastName("OldLast");
        existing.setUser(existingUser);

        when(trainerRepo.findByUsername(username)).thenReturn(existing);
        when(trainerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Trainer incoming = new Trainer();
        TrainingType newType = new TrainingType();
        newType.setTrainingTypeName("newSpec");
        incoming.setSpecialization(newType);

        User incomingUser = new User();
        incomingUser.setFirstName("New");
        incomingUser.setLastName("NewLast");
        incoming.setUser(incomingUser);

        // authValidator mocked; by default does nothing (i.e., validation passes)

        Trainer result = trainerService.updateTrainer(username, password, incoming);

        assertEquals("newSpec", result.getSpecialization().getTrainingTypeName());
        assertEquals("New", result.getUser().getFirstName());
        assertEquals("NewLast", result.getUser().getLastName());
        verify(authValidator).validateCredentials(username, password);
        verify(trainerRepo).save(existing);
    }

    @Test
    void updateTrainer_ShouldThrow_WhenAuthFails() {
        String username = "t1";
        String password = "bad";

        Trainer incoming = new Trainer();

        doThrow(new SecurityException("bad credentials"))
                .when(authValidator).validateCredentials(username, password);

        assertThrows(SecurityException.class,
                () -> trainerService.updateTrainer(username, password, incoming));
        verify(trainerRepo, never()).save(any());
    }

    @Test
    void updateTrainer_ShouldThrow_WhenTrainerNotFound() {
        String username = "missing";
        String password = "p";

        when(trainerRepo.findByUsername(username)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> trainerService.updateTrainer(username, password, new Trainer()));
        verify(trainerRepo, never()).save(any());
    }
}
