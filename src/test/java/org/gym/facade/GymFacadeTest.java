package org.gym.facade;

import org.gym.domain.model.*;
import org.gym.domain.port.in.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GymFacadeTest {

    @Mock
    private UserManagementPort userManagementPort;
    @Mock
    private TrainerManagementPort trainerManagementPort;
    @Mock
    private TraineeManagementPort traineeManagementPort;
    @Mock
    private TrainingManagementPort trainingManagementPort;
    @Mock
    private TrainingTypeManagementPort trainingTypeManagementPort;

    @InjectMocks
    private GymFacade facade;

    @Test
    void createUser_delegaEnUserManagementPort() {
        User user = mock(User.class);
        when(userManagementPort.createUser(user)).thenReturn(user);

        User result = facade.createUser(user);

        assertSame(user, result);
        verify(userManagementPort).createUser(user);
    }

    @Test
    void updateUser_delegaEnUserManagementPort() {
        User user = mock(User.class);
        when(userManagementPort.updateUser(user)).thenReturn(user);

        User result = facade.updateUser(user);

        assertSame(user, result);
        verify(userManagementPort).updateUser(user);
    }

    @Test
    void getUser_delegaEnUserManagementPort() {
        User user = mock(User.class);
        when(userManagementPort.getUser(1L)).thenReturn(user);

        User result = facade.getUser(1L);

        assertSame(user, result);
        verify(userManagementPort).getUser(1L);
    }

    @Test
    void createTrainer_delegaEnTrainerManagementPort() {
        Trainer trainer = mock(Trainer.class);
        when(trainerManagementPort.createTrainer(trainer)).thenReturn(trainer);

        Trainer result = facade.createTrainer(trainer);

        assertSame(trainer, result);
        verify(trainerManagementPort).createTrainer(trainer);
    }

    @Test
    void updateTrainer_delegaEnTrainerManagementPort() {
        Trainer trainer = mock(Trainer.class);
        when(trainerManagementPort.updateTrainer(trainer)).thenReturn(trainer);

        Trainer result = facade.updateTrainer(trainer);

        assertSame(trainer, result);
        verify(trainerManagementPort).updateTrainer(trainer);
    }

    @Test
    void getTrainer_delegaEnTrainerManagementPort() {
        Trainer trainer = mock(Trainer.class);
        when(trainerManagementPort.getTrainer(2L)).thenReturn(trainer);

        Trainer result = facade.getTrainer(2L);

        assertSame(trainer, result);
        verify(trainerManagementPort).getTrainer(2L);
    }

    @Test
    void createTrainee_delegaEnTraineeManagementPort() {
        Trainee trainee = mock(Trainee.class);
        when(traineeManagementPort.createTrainee(trainee)).thenReturn(trainee);

        Trainee result = facade.createTrainee(trainee);

        assertSame(trainee, result);
        verify(traineeManagementPort).createTrainee(trainee);
    }

    @Test
    void updateTrainee_delegaEnTraineeManagementPort() {
        Trainee trainee = mock(Trainee.class);
        when(traineeManagementPort.updateTrainee(trainee)).thenReturn(trainee);

        Trainee result = facade.updateTrainee(trainee);

        assertSame(trainee, result);
        verify(traineeManagementPort).updateTrainee(trainee);
    }

    @Test
    void getTrainee_delegaEnTraineeManagementPort() {
        Trainee trainee = mock(Trainee.class);
        when(traineeManagementPort.getTrainee(3L)).thenReturn(trainee);

        Trainee result = facade.getTrainee(3L);

        assertSame(trainee, result);
        verify(traineeManagementPort).getTrainee(3L);
    }

    @Test
    void deleteTrainee_delegaEnTraineeManagementPort() {
        facade.deleteTrainee(4L);

        verify(traineeManagementPort).deleteTrainee(4L);
    }

    @Test
    void createTraining_delegaEnTrainingManagementPort() {
        Training training = mock(Training.class);
        when(trainingManagementPort.createTraining(training)).thenReturn(training);

        Training result = facade.createTraining(training);

        assertSame(training, result);
        verify(trainingManagementPort).createTraining(training);
    }

    @Test
    void getTraining_delegaEnTrainingManagementPort() {
        Training training = mock(Training.class);
        when(trainingManagementPort.getTraining(5L)).thenReturn(training);

        Training result = facade.getTraining(5L);

        assertSame(training, result);
        verify(trainingManagementPort).getTraining(5L);
    }

    @Test
    void createTrainingType_delegaEnTrainingTypeManagementPort() {
        TrainingType type = mock(TrainingType.class);
        when(trainingTypeManagementPort.create(type)).thenReturn(type);

        TrainingType result = facade.createTrainingType(type);

        assertSame(type, result);
        verify(trainingTypeManagementPort).create(type);
    }

    @Test
    void getTrainingType_delegaEnTrainingTypeManagementPort() {
        TrainingType type = mock(TrainingType.class);
        when(trainingTypeManagementPort.get(6L)).thenReturn(type);

        TrainingType result = facade.getTrainingType(6L);

        assertSame(type, result);
        verify(trainingTypeManagementPort).get(6L);
    }

    @Test
    void updateTrainingType_delegaEnTrainingTypeManagementPort() {
        TrainingType type = mock(TrainingType.class);
        when(trainingTypeManagementPort.update(type)).thenReturn(type);

        TrainingType result = facade.updateTrainingType(type);

        assertSame(type, result);
        verify(trainingTypeManagementPort).update(type);
    }
}

