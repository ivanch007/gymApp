package org.gym.infrastructure.persistence.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.gym.domain.model.Trainer;
import org.gym.domain.port.out.TrainerRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrainerHibernateAdapter implements TrainerRepositoryPort {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Trainer save(Trainer trainer) {
        if (trainer.getId() == null) {
            entityManager.persist(trainer);
            return trainer;
        } else {
            return entityManager.merge(trainer);
        }
    }

    @Override
    public Trainer findByUsername(String username) {
        try {
            return entityManager.createQuery(
                            "SELECT t FROM Trainer t " +
                                    "JOIN FETCH t.user u " +
                                    "JOIN FETCH t.specialization " +
                                    "WHERE u.username = :un", Trainer.class)
                    .setParameter("un", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Trainer findById(Long id) {
        return entityManager.find(Trainer.class, id);
    }

    @Override
    public List<Trainer> findAll() {
        return entityManager.createQuery(
                        "SELECT DISTINCT t FROM Trainer t " +
                                "JOIN FETCH t.user " +
                                "JOIN FETCH t.specialization " +
                                "LEFT JOIN FETCH t.trainees", Trainer.class)
                .getResultList();
    }
}