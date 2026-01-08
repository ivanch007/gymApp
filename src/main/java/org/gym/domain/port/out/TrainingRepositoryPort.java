package org.gym.domain.port.out;

import org.gym.domain.model.Training;

import java.time.LocalDate;
import java.util.List;

public interface TrainingRepositoryPort {
    Training save(Training training);
    Training findById(Long id);
    List<Training> findTraineeTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate,
                                                  String trainerName,String trainingType);
    List<Training> findTrainerTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate,
                                                  String traineeName);
}
