package org.gym.application.service;

import org.gym.domain.model.Trainee;
import org.gym.domain.model.User;
import org.gym.domain.port.out.TraineeRepositoryPort;
import org.gym.domain.port.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceTest {

    private TraineeRepositoryPort traineeRepo;
    private UserRepositoryPort userRepo;
    private TraineeService traineeService;

    @BeforeEach
    void setUp() {
        traineeRepo = mock(TraineeRepositoryPort.class);
        userRepo = mock(UserRepositoryPort.class);

        traineeService = new TraineeService(traineeRepo, userRepo);
    }

    @Test
    void createTrainee_ShouldGenerateId_WhenIdIsNull() {
        Trainee trainee = new Trainee();
        trainee.setUserId(1L);
        trainee.setAddress("Some Street");
        trainee.setDateOfBirth(LocalDate.of(1990, 1, 1));

        when(userRepo.findById(1L)).thenReturn(new User());
        when(traineeRepo.save(any(Trainee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Trainee result = traineeService.createTrainee(trainee);

        assertNotNull(result.getId());
        verify(userRepo).findById(1L);
        verify(traineeRepo).save(any(Trainee.class));
    }

    @Test
    void createTrainee_ShouldThrow_WhenUserDoesNotExist() {
        Trainee trainee = new Trainee();
        trainee.setUserId(99L);
        trainee.setAddress("Address");
        trainee.setDateOfBirth(LocalDate.of(1995, 5, 5));

        when(userRepo.findById(99L)).thenReturn(null);

        assertThrows(IllegalStateException.class,
                () -> traineeService.createTrainee(trainee));
        verify(userRepo).findById(99L);
    }

    @Test
    void updateTrainee_ShouldValidateAndSave() {
        Trainee trainee = new Trainee();
        trainee.setId(20L);
        trainee.setUserId(2L);
        trainee.setAddress("Upd Street");
        trainee.setDateOfBirth(LocalDate.of(1992, 2, 2));

        when(traineeRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        Trainee result = traineeService.updateTrainee(trainee);

        assertEquals(20L, result.getId());
        verify(traineeRepo).save(trainee);
    }

    @Test
    void getTrainee_ShouldReturnEntity() {
        Trainee expected = new Trainee();
        expected.setId(55L);

        when(traineeRepo.findById(55L)).thenReturn(expected);

        Trainee result = traineeService.getTrainee(55L);

        assertSame(expected, result);
        verify(traineeRepo).findById(55L);
    }

    @Test
    void deleteTrainee_ShouldCallRepository() {
        traineeService.deleteTrainee(88L);

        verify(traineeRepo).delete(88L);
    }
}