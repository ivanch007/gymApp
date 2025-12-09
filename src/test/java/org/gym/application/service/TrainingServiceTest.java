package org.gym.application.service;

import org.gym.domain.model.Trainee;
import org.gym.domain.model.Trainer;
import org.gym.domain.model.Training;
import org.gym.domain.model.TrainingType;
import org.gym.domain.port.out.TraineeRepositoryPort;
import org.gym.domain.port.out.TrainerRepositoryPort;
import org.gym.domain.port.out.TrainingRepositoryPort;
import org.gym.domain.port.out.TrainingTypeRepositoryPort;
import org.gym.util.generator.IdGenerator;
import org.gym.util.validator.TrainingValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TrainingServiceTest {

    private TrainingRepositoryPort trainingRepo;
    private TraineeRepositoryPort traineeRepo;
    private TrainerRepositoryPort trainerRepo;
    private TrainingTypeRepositoryPort typeRepo;

    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        trainingRepo = mock(TrainingRepositoryPort.class);
        traineeRepo = mock(TraineeRepositoryPort.class);
        trainerRepo = mock(TrainerRepositoryPort.class);
        typeRepo = mock(TrainingTypeRepositoryPort.class);

        trainingService = new TrainingService(
                trainingRepo,
                traineeRepo,
                trainerRepo,
                typeRepo
        );
    }


    @Test
    void createTraining_ShouldGenerateId_WhenIdIsNull() {
        Training training = validTraining();
        training.setId(null);

        mockAssociationsExist();
        when(trainingRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        try (MockedStatic<TrainingValidator> validator = mockStatic(TrainingValidator.class);
             MockedStatic<IdGenerator> idGen = mockStatic(IdGenerator.class)) {

            validator.when(() -> TrainingValidator.validateForCreate(training)).thenAnswer(inv -> null);


            Training saved = trainingService.createTraining(training);

            assertNotNull(saved.getId());
            assertEquals(111L, saved.getId());
            verify(trainingRepo).save(saved);
        }
    }

    @Test
    void createTraining_ShouldNotOverwriteId_WhenIdExists() {
        Training training = validTraining();
        training.setId(999L);

        mockAssociationsExist();
        when(trainingRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        try (MockedStatic<TrainingValidator> validator = mockStatic(TrainingValidator.class)) {
            validator.when(() -> TrainingValidator.validateForCreate(training)).thenAnswer(inv -> null);

            Training saved = trainingService.createTraining(training);

            assertEquals(999L, saved.getId());
            verify(trainingRepo).save(training);
        }
    }

    @Test
    void createTraining_ShouldThrow_WhenTrainerDoesNotExist() {
        Training training = validTraining();

        when(trainerRepo.findById(training.getTrainerUserId())).thenReturn(null);
        when(traineeRepo.findById(anyLong())).thenReturn(new Trainee());
        when(typeRepo.findById(anyLong())).thenReturn(new TrainingType());

        try (MockedStatic<TrainingValidator> validator = mockStatic(TrainingValidator.class)) {
            validator.when(() -> TrainingValidator.validateForCreate(training)).thenAnswer(inv -> null);

            assertThrows(IllegalStateException.class,
                    () -> trainingService.createTraining(training),
                    "Training must fail if trainer does not exist");
            verify(trainerRepo).findById(training.getTrainerUserId());
        }
    }

    @Test
    void createTraining_ShouldThrow_WhenTraineeDoesNotExist() {
        Training training = validTraining();

        when(trainerRepo.findById(anyLong())).thenReturn(new Trainer());
        when(traineeRepo.findById(training.getTraineeUserId())).thenReturn(null);
        when(typeRepo.findById(anyLong())).thenReturn(new TrainingType());

        try (MockedStatic<TrainingValidator> validator = mockStatic(TrainingValidator.class)) {
            validator.when(() -> TrainingValidator.validateForCreate(training)).thenAnswer(inv -> null);

            assertThrows(IllegalStateException.class,
                    () -> trainingService.createTraining(training),
                    "Training must fail if trainee does not exist");
            verify(traineeRepo).findById(training.getTraineeUserId());
        }
    }

    @Test
    void createTraining_ShouldThrow_WhenTrainingTypeDoesNotExist() {
        Training training = validTraining();

        when(trainerRepo.findById(anyLong())).thenReturn(new Trainer());
        when(traineeRepo.findById(anyLong())).thenReturn(new Trainee());
        when(typeRepo.findById(training.getTrainingTypeId())).thenReturn(null);

        try (MockedStatic<TrainingValidator> validator = mockStatic(TrainingValidator.class)) {
            validator.when(() -> TrainingValidator.validateForCreate(training)).thenAnswer(inv -> null);

            assertThrows(IllegalStateException.class,
                    () -> trainingService.createTraining(training),
                    "Training must fail if training type does not exist");
            verify(typeRepo).findById(training.getTrainingTypeId());
        }
    }


    @Test
    void getTraining_ShouldReturnEntity_WhenExists() {
        Training expected = new Training();
        expected.setId(123L);

        when(trainingRepo.findById(123L)).thenReturn(expected);

        Training result = trainingService.getTraining(123L);

        assertSame(expected, result);
        verify(trainingRepo).findById(123L);
    }

    @Test
    void getTraining_ShouldReturnNull_WhenNotFound() {
        when(trainingRepo.findById(999L)).thenReturn(null);

        Training result = trainingService.getTraining(999L);

        assertNull(result);
    }

    private Training validTraining() {
        Training t = new Training();
        t.setTraineeUserId(100L);
        t.setTrainerUserId(200L);
        t.setTrainingTypeId(300L);
        t.setTrainingName("Morning Session");
        t.setDuration(60);
        t.setDate(LocalDate.now());
        return t;
    }

    private void mockAssociationsExist() {
        when(trainerRepo.findById(anyLong())).thenReturn(new Trainer());
        when(traineeRepo.findById(anyLong())).thenReturn(new Trainee());
        when(typeRepo.findById(anyLong())).thenReturn(new TrainingType());
    }
}