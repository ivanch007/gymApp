package org.gym.application.service;

import org.gym.domain.model.Trainee;
import org.gym.domain.port.out.TraineeRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

public class TraineeServiceTest {

    private TraineeRepositoryPort traineeRepositoryPort;
    private TraineeService traineeService;

    @BeforeEach
    void setUp() {
        traineeRepositoryPort = mock(TraineeRepositoryPort.class);
        traineeService = new TraineeService();
        traineeService.setRepo(traineeRepositoryPort);
    }

    @Test
    void createTrainee_ShouldGenerateId_WhenIdIsNull() {
        Trainee trainee = new Trainee(); // id null inicialmente

        when(traineeRepositoryPort.save(any(Trainee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Trainee result = traineeService.createTrainee(trainee);

        assertNotNull(result.getId());
        assertTrue(result.getId() > 0);
        verify(traineeRepositoryPort).save(result);
    }

    @Test
    void updateTrainee_ShouldCallSaveAndReturnUpdated() {
        Trainee trainee = new Trainee();
        trainee.setId(10L);

        when(traineeRepositoryPort.save(any(Trainee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Trainee updated = traineeService.updateTrainee(trainee);

        verify(traineeRepositoryPort).save(trainee);
        assertSame(trainee, updated);
    }

    @Test
    void getTrainee_ShouldReturnFromRepository() {
        Trainee expected = new Trainee();
        expected.setId(55L);

        when(traineeRepositoryPort.findById(55L)).thenReturn(expected);

        Trainee actual = traineeService.getTrainee(55L);

        assertSame(expected, actual);
        verify(traineeRepositoryPort).findById(55L);
    }

    @Test
    void deleteTrainee_ShouldDelegateToRepository() {
        doNothing().when(traineeRepositoryPort).delete(123L);

        traineeService.deleteTrainee(123L);

        verify(traineeRepositoryPort).delete(123L);
    }
}
