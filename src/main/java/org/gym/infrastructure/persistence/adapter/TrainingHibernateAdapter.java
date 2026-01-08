package org.gym.infrastructure.persistence.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.gym.domain.model.Training;
import org.gym.domain.port.out.TrainingRepositoryPort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TrainingHibernateAdapter implements TrainingRepositoryPort {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Training save(Training training) {
        entityManager.persist(training);
        return training;
    }

    @Override
    public List<Training> findTraineeTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate,
                                                         String trainerName, String trainingType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> training = cq.from(Training.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(training.get("trainee").get("user").get("username"), username));

        if (fromDate != null) predicates.add(cb.greaterThanOrEqualTo(training.get("trainingDate"), fromDate));
        if (toDate != null) predicates.add(cb.lessThanOrEqualTo(training.get("trainingDate"), toDate));
        if (trainerName != null && !trainerName.isBlank()) predicates.add(cb.equal(training.get("trainer").get("user")
                .get("firstName"), trainerName));
        if (trainingType != null && !trainingType.isBlank()) predicates.add(cb.equal(training.get("trainingType")
                .get("trainingTypeName"), trainingType));

        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Training> findTrainerTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate,
                                                         String traineeName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> training = cq.from(Training.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(training.get("trainer").get("user").get("username"), username));

        if (fromDate != null) predicates.add(cb.greaterThanOrEqualTo(training.get("trainingDate"), fromDate));
        if (toDate != null) predicates.add(cb.lessThanOrEqualTo(training.get("trainingDate"), toDate));
        if (traineeName != null && !traineeName.isBlank()) predicates.add(cb.equal(training.get("trainee").get("user")
                .get("firstName"), traineeName));

        cq.where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public Training findById(Long id) {
        return entityManager.find(Training.class, id);
    }
}