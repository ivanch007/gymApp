// language: java
package org.gym.application.service;

import org.gym.domain.model.Training;
import org.gym.domain.port.out.TrainingRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TrainingServiceTest {

    private TrainingRepositoryPort trainingRepositoryPort;
    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        trainingRepositoryPort = mock(TrainingRepositoryPort.class);
        trainingService = new TrainingService();
        trainingService.setRepo(trainingRepositoryPort);
    }

    @Test
    void createTraining_ShouldGenerateId_WhenIdIsNull() {
        Training training = new Training(); // id nulo inicialmente

        when(trainingRepositoryPort.save(any(Training.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Training result = trainingService.createTraining(training);

        assertNotNull(result.getId(), "El ID debe generarse cuando era null");
        assertTrue(result.getId() > 0, "El ID generado debe ser mayor que 0");
        verify(trainingRepositoryPort).save(result);
    }

    @Test
    void createTraining_ShouldNotOverwriteId_WhenIdPresent() {
        Training training = new Training();
        training.setId(999L);

        when(trainingRepositoryPort.save(any(Training.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Training result = trainingService.createTraining(training);

        assertEquals(999L, result.getId(), "No debe sobrescribirse el ID si ya existe");
        verify(trainingRepositoryPort).save(training);
    }

    @Test
    void getTraining_ShouldDelegateToRepository() {
        Training expected = new Training();
        expected.setId(123L);

        when(trainingRepositoryPort.findById(123L)).thenReturn(expected);

        Training actual = trainingService.getTraining(123L);

        assertSame(expected, actual);
        verify(trainingRepositoryPort).findById(123L);
    }
}
