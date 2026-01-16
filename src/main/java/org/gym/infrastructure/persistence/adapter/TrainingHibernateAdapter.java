package org.gym.infrastructure.persistence.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.gym.domain.model.Trainee;
import org.gym.domain.model.Trainer;
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
        if (training.getId() == null) {
            entityManager.persist(training);
            return training;
        } else {
            return entityManager.merge(training);
        }
    }

    @Override
    public List<Training> findTraineeTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate,
                                                         String trainerName, String trainingType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> training = cq.from(Training.class);

        Fetch<Training, Trainee> traineeFetch = training.fetch("trainee", JoinType.LEFT);
        traineeFetch.fetch("user", JoinType.LEFT);

        Fetch<Training, Trainer> trainerFetch = training.fetch("trainer", JoinType.LEFT);
        trainerFetch.fetch("user", JoinType.LEFT);

        training.fetch("trainingType", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(training.get("trainee").get("user").get("username"), username));

        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(training.get("date"), fromDate));
        }
        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(training.get("date"), toDate));
        }
        if (trainerName != null && !trainerName.isBlank()) {
            predicates.add(cb.equal(training.get("trainer").get("user").get("firstName"), trainerName));
        }
        if (trainingType != null && !trainingType.isBlank()) {
            predicates.add(cb.equal(training.get("trainingType").get("trainingTypeName"), trainingType));
        }

        cq.where(predicates.toArray(new Predicate[0])).distinct(true);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<Training> findTrainerTrainingsByCriteria(String username, LocalDate fromDate, LocalDate toDate,
                                                         String traineeName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> training = cq.from(Training.class);

        Fetch<Training, Trainer> trainerFetch = training.fetch("trainer", JoinType.LEFT);
        trainerFetch.fetch("user", JoinType.LEFT);

        Fetch<Training, Trainee> traineeFetch = training.fetch("trainee", JoinType.LEFT);
        traineeFetch.fetch("user", JoinType.LEFT);

        training.fetch("trainingType", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(training.get("trainer").get("user").get("username"), username));

        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(training.get("date"), fromDate));
        }
        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(training.get("date"), toDate));
        }
        if (traineeName != null && !traineeName.isBlank()) {
            predicates.add(cb.equal(training.get("trainee").get("user").get("firstName"), traineeName));
        }

        cq.where(predicates.toArray(new Predicate[0])).distinct(true);
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public Training findById(Long id) {
        return entityManager.find(Training.class, id);
    }
}