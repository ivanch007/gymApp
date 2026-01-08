package org.gym.domain.port.in;

import org.gym.domain.model.Training;

import java.time.LocalDate;
import java.util.List;

public interface TrainingManagementPort {
    Training createTraining(Training training);
    List<Training> getTraineeTrainings(String username, LocalDate fromDate, LocalDate toDate, String trainerName,
                                       String trainingType);
    List<Training> getTrainerTrainings(String username, LocalDate fromDate, LocalDate toDate, String traineeName);

}
