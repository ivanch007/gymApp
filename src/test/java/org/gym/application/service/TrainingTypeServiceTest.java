package org.gym.application.service;

import org.gym.domain.model.TrainingType;
import org.gym.domain.port.out.TrainingTypeRepositoryPort;
import org.gym.util.validator.TrainingTypeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TrainingTypeServiceTest {

    private TrainingTypeRepositoryPort trainingTypeRepo;
    private TrainingTypeService trainingTypeService;

    @BeforeEach
    void setUp() {
        trainingTypeRepo = mock(TrainingTypeRepositoryPort.class);
        trainingTypeService = new TrainingTypeService(trainingTypeRepo);
    }



    @Test
    void create_ShouldThrow_WhenNameIsNull() {

        TrainingType type = new TrainingType();
        type.setTrainingTypeName(null);

        assertThrows(IllegalArgumentException.class,
                () -> trainingTypeService.create(type),
                "Missing name must cause validation error");
    }

    @Test
    void get_ShouldReturnEntity_WhenExists() {
        TrainingType expected = new TrainingType();
        expected.setId(10L);

        when(trainingTypeRepo.findById(10L)).thenReturn(expected);

        TrainingType result = trainingTypeService.get(10L);

        assertSame(expected, result);
        verify(trainingTypeRepo).findById(10L);
    }

    @Test
    void get_ShouldReturnNull_WhenIdIsNull() {
        TrainingType result = trainingTypeService.get(null);
        assertNull(result);
    }

    // java
    @Test
    void update_ShouldValidateAndSave() {
        TrainingType type = new TrainingType();
        type.setId(22L);
        type.setTrainingTypeName("Boxing");


        when(trainingTypeRepo.findById(22L)).thenReturn(type);
        when(trainingTypeRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        try (MockedStatic<TrainingTypeValidator> validator = mockStatic(TrainingTypeValidator.class)) {
            validator.when(() -> TrainingTypeValidator.validateForUpdate(type)).thenAnswer(inv -> null);

            TrainingType result = trainingTypeService.update(type);

            assertEquals(22L, result.getId());
            verify(trainingTypeRepo).save(same(type));
        }
    }
}