package org.gym;

import org.gym.domain.model.*;
import org.gym.facade.GymFacade;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class App 
{
    public static void main( String[] args )
    {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("org.gym");

        GymFacade gymFacade = context.getBean(GymFacade.class);

        TrainingType biceps = new TrainingType();
        biceps.setTrainingTypeName("Biceps Workout");
        TrainingType savedBiceps = gymFacade.createTrainingType(biceps);
        System.out.println("Training Type created with ID: " + savedBiceps.getId() + " and Name: "
                + savedBiceps.getTrainingTypeName());

        User user = new User();
        user.setFirstName("Ivan");
        user.setLastName("Ocampo");
        User savedUser = gymFacade.createUser(user);
        System.out.println("User created with ID: " + savedUser.getId() + " Name: " + savedUser.getFirstName()
                + " Lastname: " +savedUser.getLastName() + " and Username: " + savedUser.getUsername() + " password: "
                +  savedUser.getPassword());

        Trainer trainer = new Trainer();
        trainer.setUserId(savedUser.getId());
        trainer.setSpecialization("Strength Training");
        Trainer savedTrainer = gymFacade.createTrainer(trainer);
        System.out.println("Trainer creado id=" + savedTrainer.getId() + " userId=" + savedTrainer.getUserId());

        Trainee trainee = new Trainee();
        trainee.setUserId(savedUser.getId());
        trainee.setAddress("Calle 123");
        trainee.setDateOfBirth(java.time.LocalDate.of(1995, 5, 20));
        Trainee savedTrainee = gymFacade.createTrainee(trainee);
        System.out.println("Trainee creado id=" + savedTrainee.getId() + " userId=" + savedTrainee.getUserId());

        Training training = new Training();
        training.setTrainerUserId(savedTrainer.getUserId());
        training.setTraineeUserId(savedTrainee.getUserId());
        training.setTrainingType(savedBiceps.getTrainingTypeName());
        training.setTraingName("Morning Session");
        training.setDate(java.time.LocalDate.now());
        training.setDuration(60);
        Training savedTraining = gymFacade.createTraining(training);
        System.out.println("Training creado id: " + savedTraining.getId() + " name: " + savedTraining.getTraingName());

        context.close();
    }


}
