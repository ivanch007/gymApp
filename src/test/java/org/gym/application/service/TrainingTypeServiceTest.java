package org.gym.application.service;

import org.gym.domain.model.TrainingType;
import org.gym.domain.port.out.TrainingTypeRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrainingTypeServiceTest {

    private TrainingTypeRepositoryPort trainingTypeRepositoryPort;
    private TrainingTypeService trainingTypeService;

    @BeforeEach
    void setUp() {
        trainingTypeRepositoryPort = mock(TrainingTypeRepositoryPort.class);
        trainingTypeService = new TrainingTypeService(trainingTypeRepositoryPort);
    }

    @Test
    void create_ShouldGenerateId_WhenIdIsNull() {
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Cardio");

        ArgumentCaptor<TrainingType> captor = ArgumentCaptor.forClass(TrainingType.class);

        when(trainingTypeRepositoryPort.save(any(TrainingType.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TrainingType result = trainingTypeService.create(trainingType);

        verify(trainingTypeRepositoryPort).save(captor.capture());
        TrainingType saved = captor.getValue();

        assertNotNull(saved.getId(), "El ID debe ser generado automáticamente");
        assertEquals("Cardio", saved.getTrainingTypeName());
        assertEquals(result.getId(), saved.getId(), "El ID retornado debe ser el mismo");
    }

    @Test
    void get_ShouldDelegateToRepository() {
        TrainingType expected = new TrainingType();
        expected.setId(123L);

        when(trainingTypeRepositoryPort.findById(123L)).thenReturn(expected);

        TrainingType actual = trainingTypeService.get(123L);

        assertSame(expected, actual);
        verify(trainingTypeRepositoryPort).findById(123L);
    }

    @Test
    void update_ShouldCallSaveAndReturnUpdated() {
        TrainingType existing = new TrainingType();
        existing.setId(1L);
        existing.setTrainingTypeName("Old");

        when(trainingTypeRepositoryPort.save(any(TrainingType.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        TrainingType updated = trainingTypeService.update(existing);

        verify(trainingTypeRepositoryPort).save(existing);
        assertEquals("Old", updated.getTrainingTypeName());
    }
}
