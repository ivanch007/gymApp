package org.gym.application.service;

import org.gym.domain.model.Trainer;
import org.gym.domain.port.out.TrainerRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TrainerServiceTest {

    private TrainerRepositoryPort trainerRepositoryPort;
    private TrainerService trainerService;

    @BeforeEach
    void setUp() {
        trainerRepositoryPort = mock(TrainerRepositoryPort.class);
        trainerService = new TrainerService();
        trainerService.setRepo(trainerRepositoryPort);
    }

    @Test
    void createTrainer_ShouldGenerateId_WhenIdIsNull() {
        Trainer trainer = new Trainer(); // id nulo inicialmente

        when(trainerRepositoryPort.save(any(Trainer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Trainer result = trainerService.createTrainer(trainer);

        assertNotNull(result.getId());
        assertTrue(result.getId() > 0);
        verify(trainerRepositoryPort).save(result);
    }

    @Test
    void updateTrainer_ShouldCallSaveAndReturnUpdated() {
        Trainer trainer = new Trainer();
        trainer.setId(10L);

        when(trainerRepositoryPort.save(any(Trainer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Trainer updated = trainerService.updateTrainer(trainer);

        verify(trainerRepositoryPort).save(trainer);
        assertSame(trainer, updated);
    }

    @Test
    void getTrainer_ShouldReturnTrainerFromRepository() {
        Trainer expected = new Trainer();
        expected.setId(55L);

        when(trainerRepositoryPort.findById(55L)).thenReturn(expected);

        Trainer actual = trainerService.getTrainer(55L);

        assertSame(expected, actual);
        verify(trainerRepositoryPort).findById(55L);
    }
}
