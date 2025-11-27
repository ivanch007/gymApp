package org.gym.infrastructure.storage;

import org.gym.domain.model.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class StorageConfig {

    @Bean(name = "userStorage")
    public Map<Long, User> userStorage() {
        return new HashMap<>();
    }

    @Bean(name = "trainerStorage")
    public Map<Long, Trainer> trainerStorage() {
        return new HashMap<>();
    }

    @Bean(name = "traineeStorage")
    public Map<Long, Trainee> traineeStorage() {
        return new HashMap<>();
    }

    @Bean(name = "trainingStorage")
    public Map<Long, Training> trainingStorage() {
        return new HashMap<>();
    }

    @Bean(name = "trainingTypeStorage")
    public Map<Long, TrainingType> trainingTypeStorage() {
        return new HashMap<>();
    }
}
