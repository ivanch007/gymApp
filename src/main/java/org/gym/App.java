package org.gym;

import org.gym.domain.model.*;
import org.gym.facade.GymFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;


public class App 
{
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args )
    {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("org.gym");

        GymFacade gymFacade = context.getBean(GymFacade.class);

        TrainingType biceps = new TrainingType();
        biceps.setTrainingTypeName("Biceps Workout");

        TrainingType savedBiceps = gymFacade.createTrainingType(biceps);
        logger.info("TrainingType created id={} name={}",
                savedBiceps.getId(), savedBiceps.getTrainingTypeName());

        User user = new User();
        user.setFirstName("john");
        user.setLastName("doe");
        User savedUser = gymFacade.createUser(user);
        logger.info("User created id={} username={} password={}",
                savedUser.getId(), savedUser.getUsername(), savedUser.getPassword());

        Trainer trainer = new Trainer();
        trainer.setUserId(savedUser.getId());
        trainer.setSpecialization("Strength Training");
        Trainer savedTrainer = gymFacade.createTrainer(trainer);
        logger.info("Trainer created id={} userId={}",
                savedTrainer.getId(), savedTrainer.getUserId());

        Trainee trainee = new Trainee();
        trainee.setUserId(savedUser.getId());
        trainee.setAddress("street 123");
        trainee.setDateOfBirth(java.time.LocalDate.of(1995, 5, 20));
        Trainee savedTrainee = gymFacade.createTrainee(trainee);
        logger.info("Trainee created id={} userId={}",
                savedTrainee.getId(), savedTrainee.getUserId());

        Training training = new Training();
        training.setTrainerUserId(savedTrainer.getId());
        training.setTraineeUserId(savedTrainee.getId());
        training.setTrainingTypeId(savedBiceps.getId());
        training.setTrainingName("Morning Session");
        training.setDate(LocalDate.now());
        training.setDuration(60);
        Training savedTraining = gymFacade.createTraining(training);
        logger.info("Training created id={} name={}",
                savedTraining.getId(), savedTraining.getTrainingName());

        context.close();
    }


}
