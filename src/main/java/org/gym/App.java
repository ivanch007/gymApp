package org.gym;

import org.gym.domain.model.*;
import org.gym.facade.GymFacade;
import org.gym.infrastructure.config.AppConfig;
import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        GymFacade gymFacade = context.getBean(GymFacade.class);
        Server webServer = null;

        try {
            webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
            logger.info("H2 console started successfully.");
            logger.info("Web access: http://localhost:8082");
            logger.info("Connection settings -> JDBC URL: jdbc:h2:mem:gymdb | User: sa");


            TrainingType yogaType = new TrainingType();
            yogaType.setTrainingTypeName("Yoga");
            TrainingType savedType = gymFacade.createTrainingType(yogaType);
            logger.info("TrainingType created: {}", savedType.getTrainingTypeName());


            User userTrainer = new User();
            userTrainer.setFirstName("John");
            userTrainer.setLastName("Doe");

            Trainer trainer = new Trainer();
            trainer.setUser(userTrainer);
            trainer.setSpecialization(savedType);

            Trainer savedTrainer = gymFacade.createTrainer(trainer);
            logger.info("Trainer created. Username: {} | Password: {}",
                    savedTrainer.getUser().getUsername(), savedTrainer.getUser().getPassword());


            User userTrainee = new User();
            userTrainee.setFirstName("Jane");
            userTrainee.setLastName("Doe");

            Trainee trainee = new Trainee();
            trainee.setUser(userTrainee);
            trainee.setAddress("False street 123");
            trainee.setDateOfBirth(LocalDate.of(1998, 10, 15));

            Trainee savedTrainee = gymFacade.createTrainee(trainee);
            logger.info("Trainee created. Username: {}", savedTrainee.getUser().getUsername());


            Training training = new Training();
            training.setTrainer(savedTrainer);
            training.setTrainee(savedTrainee);
            training.setTrainingType(savedType);
            training.setTrainingName("Welcome Session");
            training.setDate(LocalDate.now());
            training.setDuration(45);

            Training savedTraining = gymFacade.createTraining(training);
            logger.info("Training registered: {} with duration {} min",
                    savedTraining.getTrainingName(), savedTraining.getDuration());


            logger.info("========================================================================");
            logger.info("SYSTEM ON PAUSE: The database is available in the browser.");
            logger.info("Press the ENTER key on this console to close the application...");
            logger.info("========================================================================");

            System.in.read();

        } catch (Exception e) {
            logger.error("Error during execution: ", e);
        } finally {
            if (webServer != null) {
                webServer.stop();
                logger.info("Server H2 stopped.");
            }
            context.close();
            logger.info("Closed Spring context.");
        }
    }
}