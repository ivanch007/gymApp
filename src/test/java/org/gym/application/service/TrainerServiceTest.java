package org.gym.application.service;

import org.gym.domain.model.Trainer;
import org.gym.domain.model.User;
import org.gym.domain.port.out.TrainerRepositoryPort;
import org.gym.domain.port.out.UserRepositoryPort;
import org.gym.util.validator.TrainerValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerServiceTest {

    private TrainerRepositoryPort trainerRepo;
    private UserRepositoryPort userRepo;
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        trainerRepo = mock(TrainerRepositoryPort.class);
        userRepo   = mock(UserRepositoryPort.class);

        trainerService = new TrainerService(trainerRepo, userRepo);
    }

    @Test
    void createTrainer_ShouldGenerateId_WhenIdIsNull() {
        Trainer trainer = new Trainer();
        trainer.setUserId(10L);


        when(userRepo.findById(10L)).thenReturn(new User());
        when(trainerRepo.save(any())).thenAnswer(i -> i.getArgument(0));


        try (MockedStatic<TrainerValidator> validator = mockStatic(TrainerValidator.class)) {
            validator.when(() -> TrainerValidator.validateForCreate(trainer)).thenAnswer(inv -> null);

            Trainer result = trainerService.createTrainer(trainer);

            assertNotNull(result.getId());
            assertTrue(result.getId() > 0);
            verify(trainerRepo).save(result);
            verify(userRepo).findById(10L);
        }
    }

    @Test
    void createTrainer_ShouldThrow_WhenAssociatedUserNotFound() {
        Trainer trainer = new Trainer();
        trainer.setUserId(99L);

        when(userRepo.findById(99L)).thenReturn(null);

        try (MockedStatic<TrainerValidator> validator = mockStatic(TrainerValidator.class)) {
            validator.when(() -> TrainerValidator.validateForCreate(trainer)).thenAnswer(inv -> null);

            assertThrows(IllegalArgumentException.class,
                    () -> trainerService.createTrainer(trainer));
            verify(userRepo).findById(99L);
        }
    }

    @Test
    void updateTrainer_ShouldValidateAndSave() {
        Trainer trainer = new Trainer();
        trainer.setId(20L);

        when(trainerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        try (MockedStatic<TrainerValidator> validator = mockStatic(TrainerValidator.class)) {
            validator.when(() -> TrainerValidator.validateForUpdate(trainer)).thenAnswer(inv -> null);

            Trainer result = trainerService.updateTrainer(trainer);

            assertEquals(20L, result.getId());
            verify(trainerRepo).save(trainer);
        }
    }

    @Test
    void getTrainer_ShouldReturnEntity() {
        Trainer expected = new Trainer();
        expected.setId(55L);

        when(trainerRepo.findById(55L)).thenReturn(expected);

        Trainer result = trainerService.getTrainer(55L);

        assertSame(expected, result);
        verify(trainerRepo).findById(55L);
    }

    @Test
    void createTrainer_ShouldNotOverrideExistingId() {
        Trainer trainer = new Trainer();
        trainer.setId(999L);
        trainer.setUserId(1L);

        when(userRepo.findById(1L)).thenReturn(new User());
        when(trainerRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        try (MockedStatic<TrainerValidator> v = mockStatic(TrainerValidator.class)) {
            v.when(() -> TrainerValidator.validateForCreate(any())).thenAnswer(i -> null);

            Trainer result = trainerService.createTrainer(trainer);

            assertEquals(999L, result.getId());
        }
    }

}