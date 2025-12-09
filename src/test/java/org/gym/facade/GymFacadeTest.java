package org.gym.facade;

import org.gym.domain.model.*;
import org.gym.domain.port.in.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GymFacadeTest {

    private UserManagementPort userPort;
    private TrainerManagementPort trainerPort;
    private TraineeManagementPort traineePort;
    private TrainingManagementPort trainingPort;
    private TrainingTypeManagementPort typePort;

    private GymFacade gymFacade;

    @BeforeEach
    void setUp() {
        userPort = mock(UserManagementPort.class);
        trainerPort = mock(TrainerManagementPort.class);
        traineePort = mock(TraineeManagementPort.class);
        trainingPort = mock(TrainingManagementPort.class);
        typePort = mock(TrainingTypeManagementPort.class);

        gymFacade = new GymFacade(
                userPort,
                trainerPort,
                traineePort,
                trainingPort,
                typePort
        );
    }


    @Test
    void createUser_ShouldDelegateToUserService() {
        User u = new User();
        when(userPort.createUser(u)).thenReturn(u);

        User result = gymFacade.createUser(u);

        assertSame(u, result);
        verify(userPort).createUser(u);
    }

    @Test
    void updateUser_ShouldCallUserService() {
        User u = new User();
        u.setId(10L);
        when(userPort.updateUser(u)).thenReturn(u);

        User result = gymFacade.updateUser(u);

        assertSame(u, result);
        verify(userPort).updateUser(u);
    }

    @Test
    void getUser_ShouldCallUserService() {
        User expected = new User();
        expected.setId(22L);

        when(userPort.getUser(22L)).thenReturn(expected);

        User result = gymFacade.getUser(22L);

        assertSame(expected, result);
        verify(userPort).getUser(22L);
    }


    @Test
    void createTrainer_ShouldDelegateToTrainerService() {
        Trainer trainer = new Trainer();
        when(trainerPort.createTrainer(trainer)).thenReturn(trainer);

        Trainer result = gymFacade.createTrainer(trainer);

        assertSame(trainer, result);
        verify(trainerPort).createTrainer(trainer);
    }

    @Test
    void getTrainer_ShouldCallTrainerService() {
        Trainer trainer = new Trainer();
        trainer.setId(30L);

        when(trainerPort.getTrainer(30L)).thenReturn(trainer);

        Trainer result = gymFacade.getTrainer(30L);

        assertSame(trainer, result);
        verify(trainerPort).getTrainer(30L);
    }

    @Test
    void createTrainee_ShouldDelegateToTraineeService() {
        Trainee t = new Trainee();
        when(traineePort.createTrainee(t)).thenReturn(t);

        Trainee result = gymFacade.createTrainee(t);

        assertSame(t, result);
        verify(traineePort).createTrainee(t);
    }

    @Test
    void deleteTrainee_ShouldCallTraineeService() {
        gymFacade.deleteTrainee(55L);
        verify(traineePort).deleteTrainee(55L);
    }


    @Test
    void createTraining_ShouldDelegateToTrainingService() {
        Training tr = new Training();
        when(trainingPort.createTraining(tr)).thenReturn(tr);

        Training result = gymFacade.createTraining(tr);

        assertSame(tr, result);
        verify(trainingPort).createTraining(tr);
    }

    @Test
    void getTraining_ShouldCallTrainingService() {
        Training tr = new Training();
        tr.setId(44L);

        when(trainingPort.getTraining(44L)).thenReturn(tr);

        Training result = gymFacade.getTraining(44L);

        assertSame(tr, result);
        verify(trainingPort).getTraining(44L);
    }



    @Test
    void createTrainingType_ShouldDelegateToService() {
        TrainingType type = new TrainingType();
        when(typePort.create(type)).thenReturn(type);

        TrainingType result = gymFacade.createTrainingType(type);

        assertSame(type, result);
        verify(typePort).create(type);
    }

    @Test
    void updateTrainingType_ShouldCallService() {
        TrainingType type = new TrainingType();
        type.setId(66L);

        when(typePort.update(type)).thenReturn(type);

        TrainingType result = gymFacade.updateTrainingType(type);

        assertSame(type, result);
        verify(typePort).update(type);
    }

    @Test
    void getTrainingType_ShouldCallService() {
        TrainingType type = new TrainingType();
        type.setId(77L);

        when(typePort.get(77L)).thenReturn(type);

        TrainingType result = gymFacade.getTrainingType(77L);

        assertSame(type, result);
        verify(typePort).get(77L);
    }
}
