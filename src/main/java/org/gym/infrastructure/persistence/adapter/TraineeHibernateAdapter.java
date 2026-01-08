package org.gym.infrastructure.persistence.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.gym.domain.model.Trainee;
import org.gym.domain.port.out.TraineeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TraineeHibernateAdapter implements TraineeRepositoryPort {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Trainee save(Trainee trainee) {
        if (trainee.getId() == null) {
            entityManager.persist(trainee);
            return trainee;
        } else {
            return entityManager.merge(trainee);
        }
    }

    @Override
    public Trainee findByUsername(String username) {
        try {
            return entityManager.createQuery(
                            "SELECT t FROM Trainee t JOIN t.user u WHERE u.username = :un", Trainee.class)
                    .setParameter("un", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void delete(String username) {
        Trainee trainee = findByUsername(username);
        if (trainee != null) {
            entityManager.remove(trainee);
        }
    }

    @Override
    public Trainee findById(Long id) {
        return entityManager.find(Trainee.class, id);
    }

    @Override
    public List<Trainee> findAll() {
        return entityManager.createQuery("SELECT t FROM Trainee t", Trainee.class).getResultList();
    }
}