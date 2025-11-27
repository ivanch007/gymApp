package org.gym.domain.port.out;

import org.gym.domain.model.Trainee;

import java.util.List;

public interface TraineeRepositoryPort {
    Trainee save(Trainee trainee);
    Trainee findById(Long id);
    List<Trainee> findAll();
    void delete(Long id);

}
