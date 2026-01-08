package org.gym.infrastructure.persistence.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.gym.domain.model.TrainingType;
import org.gym.domain.port.out.TrainingTypeRepositoryPort;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class TrainingTypeHibernateAdapter implements TrainingTypeRepositoryPort {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public TrainingType save(TrainingType trainingType) {
        if (trainingType.getId() == null) {
            entityManager.persist(trainingType);
            return trainingType;
        } else {
            return entityManager.merge(trainingType);
        }
    }

    @Override
    public TrainingType findById(Long id) {
        return entityManager.find(TrainingType.class, id);
    }

    @Override
    public List<TrainingType> findAll() {
        return entityManager.createQuery("SELECT tt FROM TrainingType tt", TrainingType.class).getResultList();
    }
}