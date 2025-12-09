package org.gym.domain.model;

import java.time.LocalDate;

public class Training {
    private Long id;
    private Long traineeUserId;
    private Long trainerUserId;
    private String trainingName;
    private Long trainingTypeId;
    private LocalDate date;
    private Integer duration;

    public Training() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTraineeUserId() {
        return traineeUserId;
    }

    public void setTraineeUserId(Long traineeUserId) {
        this.traineeUserId = traineeUserId;
    }

    public Long getTrainerUserId() {
        return trainerUserId;
    }

    public void setTrainerUserId(Long trainerUserId) {
        this.trainerUserId = trainerUserId;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public Long getTrainingTypeId() {
        return trainingTypeId;
    }

    public void setTrainingTypeId(Long trainingType) {
        this.trainingTypeId = trainingType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
