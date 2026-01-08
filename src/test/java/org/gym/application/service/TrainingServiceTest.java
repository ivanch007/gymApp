package org.gym.application.service;

import org.gym.domain.model.Trainee;
import org.gym.domain.model.Trainer;
import org.gym.domain.model.Training;
import org.gym.domain.model.TrainingType;
import org.gym.util.validator.TrainingValidator;
import org.gym.domain.port.out.TrainingRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceTest {

    private TrainingRepositoryPort trainingRepo;
    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        trainingRepo = mock(TrainingRepositoryPort.class);
        trainingService = new TrainingService(trainingRepo);
    }

    @Test
    void createTraining_ShouldSaveAndReturnAssignedId_WhenValid() {
        Training training = validTraining();
        training.setId(null);

        when(trainingRepo.save(any())).thenAnswer(inv -> {
            Training t = inv.getArgument(0);
            t.setId(111L);
            return t;
        });

        try (MockedStatic<TrainingValidator> validator = mockStatic(TrainingValidator.class)) {
            validator.when(() -> TrainingValidator.validateForCreate(training)).thenAnswer(inv -> null);

            Training saved = trainingService.createTraining(training);

            assertNotNull(saved.getId());
            assertEquals(111L, saved.getId());
            verify(trainingRepo).save(training);
        }
    }

    @Test
    void createTraining_ShouldPropagatePersistenceException() {
        Training training = validTraining();
        training.setId(null);

        when(trainingRepo.save(any())).thenThrow(new RuntimeException("DB error"));

        try (MockedStatic<TrainingValidator> validator = mockStatic(TrainingValidator.class)) {
            validator.when(() -> TrainingValidator.validateForCreate(training)).thenAnswer(inv -> null);

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> trainingService.createTraining(training));
            assertEquals("DB error", ex.getMessage());
            verify(trainingRepo).save(training);
        }
    }

    @Test
    void createTraining_ShouldNotCallSave_WhenValidatorThrows() {
        Training training = validTraining();
        training.setId(null);

        try (MockedStatic<TrainingValidator> validator = mockStatic(TrainingValidator.class)) {
            validator.when(() -> TrainingValidator.validateForCreate(training))
                    .thenThrow(new IllegalArgumentException("invalid request"));

            assertThrows(IllegalArgumentException.class,
                    () -> trainingService.createTraining(training));
            verifyNoInteractions(trainingRepo);
        }
    }

    @Test
    void getTraineeTrainings_ShouldReturnList_WhenFound() {
        String username = "user1";
        LocalDate from = LocalDate.now().minusDays(7);
        LocalDate to = LocalDate.now();
        String trainerName = "trainerA";
        String trainingType = "CARDIO";

        List<Training> expected = Arrays.asList(new Training(), new Training());
        when(trainingRepo.findTraineeTrainingsByCriteria(username, from, to, trainerName, trainingType))
                .thenReturn(expected);

        List<Training> result = trainingService.getTraineeTrainings(username, from, to, trainerName, trainingType);

        assertSame(expected, result);
        verify(trainingRepo).findTraineeTrainingsByCriteria(username, from, to, trainerName, trainingType);
    }

    @Test
    void getTraineeTrainings_ShouldReturnEmptyList_WhenNone() {
        when(trainingRepo.findTraineeTrainingsByCriteria(any(), any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        List<Training> result = trainingService.getTraineeTrainings("u", null, null, null, null);

        assertTrue(result.isEmpty());
        verify(trainingRepo).findTraineeTrainingsByCriteria("u", null, null, null, null);
    }

    @Test
    void getTrainerTrainings_ShouldReturnList_WhenFound() {
        String username = "trainer1";
        LocalDate from = LocalDate.now().minusDays(30);
        LocalDate to = LocalDate.now();
        String traineeName = "traineeX";

        List<Training> expected = Arrays.asList(new Training());
        when(trainingRepo.findTrainerTrainingsByCriteria(username, from, to, traineeName))
                .thenReturn(expected);

        List<Training> result = trainingService.getTrainerTrainings(username, from, to, traineeName);

        assertEquals(1, result.size());
        verify(trainingRepo).findTrainerTrainingsByCriteria(username, from, to, traineeName);
    }

    @Test
    void getTrainerTrainings_ShouldReturnEmptyList_WhenNone() {
        when(trainingRepo.findTrainerTrainingsByCriteria(any(), any(), any(), any()))
                .thenReturn(Collections.emptyList());

        List<Training> result = trainingService.getTrainerTrainings("t", null, null, null);

        assertTrue(result.isEmpty());
        verify(trainingRepo).findTrainerTrainingsByCriteria("t", null, null, null);
    }

    // helpers

    private Training validTraining() {
        Trainee trainee = new Trainee();
        trainee.setId(100L);

        Trainer trainer = new Trainer();
        trainer.setId(200L);

        TrainingType trainingType = new TrainingType();
        trainingType.setId(300L);

        Training t = new Training();
        t.setTrainee(trainee);
        t.setTrainer(trainer);
        t.setTrainingType(trainingType);
        t.setTrainingName("Morning Session");
        t.setDuration(60);
        t.setDate(LocalDate.now());
        return t;
    }

}
