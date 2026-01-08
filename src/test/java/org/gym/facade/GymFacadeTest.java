package org.gym.facade;

import org.gym.domain.model.*;
import org.gym.domain.port.in.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GymFacadeTest {

    private AuthManagementPort authPort;
    private TraineeManagementPort traineePort;
    private TrainerManagementPort trainerPort;
    private TrainingManagementPort trainingPort;
    private TrainingTypeManagementPort typePort;
    private UserManagementPort userPort;

    private GymFacade gymFacade;

    @BeforeEach
    void setUp() {
        authPort = mock(AuthManagementPort.class);
        traineePort = mock(TraineeManagementPort.class);
        trainerPort = mock(TrainerManagementPort.class);
        trainingPort = mock(TrainingManagementPort.class);
        typePort = mock(TrainingTypeManagementPort.class);
        userPort = mock(UserManagementPort.class);

        gymFacade = new GymFacade(
                authPort,
                traineePort,
                trainerPort,
                trainingPort,
                typePort,
                userPort
        );
    }

    @Test
    void authenticate_ShouldDelegateAndReturnTrue_WhenCredentialsValid() {
        when(authPort.authenticate("u", "p")).thenReturn(true);

        assertTrue(gymFacade.authenticate("u", "p"));
        verify(authPort).authenticate("u", "p");
    }

    @Test
    void authenticate_ShouldReturnFalse_WhenCredentialsInvalid() {
        when(authPort.authenticate("u", "bad")).thenReturn(false);

        assertFalse(gymFacade.authenticate("u", "bad"));
        verify(authPort).authenticate("u", "bad");
    }

    @Test
    void changePassword_ShouldCallAuthPort() {
        doNothing().when(authPort).changePassword("u", "old", "new");

        gymFacade.changePassword("u", "old", "new");

        verify(authPort).changePassword("u", "old", "new");
    }

    @Test
    void toggleUserStatus_ShouldCallAuthToggle() {
        doNothing().when(authPort).toggleStatus("u", "p");

        gymFacade.toggleUserStatus("u", "p");

        verify(authPort).toggleStatus("u", "p");
    }

    @Test
    void createTrainee_ShouldDelegateAndReturn_WhenOk() {
        Trainee t = new Trainee();
        when(traineePort.createTrainee(t)).thenReturn(t);

        Trainee result = gymFacade.createTrainee(t);

        assertSame(t, result);
        verify(traineePort).createTrainee(t);
    }

    @Test
    void createTrainee_ShouldPropagateException_WhenServiceFails() {
        Trainee t = new Trainee();
        when(traineePort.createTrainee(t)).thenThrow(new RuntimeException("db"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> gymFacade.createTrainee(t));
        assertEquals("db", ex.getMessage());
        verify(traineePort).createTrainee(t);
    }

    @Test
    void updateTrainee_ShouldDelegate_WithCredentials() {
        String user = "trainee1";
        String pass = "pwd";
        Trainee incoming = new Trainee();
        Trainee updated = new Trainee();
        updated.setId(5L);

        when(traineePort.updateTrainee(user, pass, incoming)).thenReturn(updated);

        Trainee result = gymFacade.updateTrainee(user, pass, incoming);

        assertSame(updated, result);
        verify(traineePort).updateTrainee(user, pass, incoming);
    }

    @Test
    void deleteTrainee_ShouldCallPortAndPropagate_WhenFails() {
        doNothing().when(traineePort).deleteTrainee("tuser", "pwd");

        gymFacade.deleteTrainee("tuser", "pwd");
        verify(traineePort).deleteTrainee("tuser", "pwd");

        doThrow(new IllegalStateException("nope"))
                .when(traineePort).deleteTrainee("bad", "pwd");

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> gymFacade.deleteTrainee("bad", "pwd"));
        assertEquals("nope", ex.getMessage());
    }

    @Test
    void createTraining_And_getTraineeTrainings_GetTrainerTrainings_Scenarios() {
        Training tr = new Training();
        when(trainingPort.createTraining(tr)).thenReturn(tr);

        Training saved = gymFacade.createTraining(tr);
        assertSame(tr, saved);
        verify(trainingPort).createTraining(tr);

        List<Training> list = Arrays.asList(new Training(), new Training());
        LocalDate from = LocalDate.now().minusDays(7);
        LocalDate to = LocalDate.now();

        when(trainingPort.getTraineeTrainings("u", from, to, "trainerA", "TYPE"))
                .thenReturn(list);

        List<Training> res = gymFacade.getTraineeTrainings("u", from, to, "trainerA", "TYPE");
        assertSame(list, res);
        verify(trainingPort).getTraineeTrainings("u", from, to, "trainerA", "TYPE");

        when(trainingPort.getTrainerTrainings("t", from, to, "traineeX"))
                .thenReturn(Collections.emptyList());

        List<Training> empty = gymFacade.getTrainerTrainings("t", from, to, "traineeX");
        assertTrue(empty.isEmpty());
        verify(trainingPort).getTrainerTrainings("t", from, to, "traineeX");
    }

    @Test
    void createTrainingType_And_getTrainingType_Scenarios() {
        TrainingType type = new TrainingType();
        type.setId(99L);
        when(typePort.create(type)).thenReturn(type);

        TrainingType created = gymFacade.createTrainingType(type);
        assertSame(type, created);
        verify(typePort).create(type);

        when(typePort.get(99L)).thenReturn(type);
        TrainingType got = gymFacade.getTrainingType(99L);
        assertSame(type, got);
        verify(typePort).get(99L);

        when(typePort.get(123L)).thenReturn(null);
        assertNull(gymFacade.getTrainingType(123L));
        verify(typePort).get(123L);
    }

    @Test
    void getUserByUsername_ShouldDelegate() {
        User u = new User();
        u.setUsername("juan");
        when(userPort.getUserByUsername("juan")).thenReturn(u);

        User result = gymFacade.getUserByUsername("juan");
        assertSame(u, result);
        verify(userPort).getUserByUsername("juan");
    }
}
