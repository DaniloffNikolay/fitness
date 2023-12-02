package kz.danilov.backend.controllers;

import kz.danilov.backend.BackendApplication;
import kz.danilov.backend.models.Person;
import kz.danilov.backend.models.trainers.Exercise;
import kz.danilov.backend.models.trainers.Trainer;
import kz.danilov.backend.security.SecurityUtil;
import kz.danilov.backend.services.trainers.ExercisesService;
import kz.danilov.backend.services.trainers.TrainersService;
import kz.danilov.backend.util.ModelMapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Controller
@RequestMapping("/trainer")
public class TrainerController {

    private static final Logger log = LoggerFactory.getLogger(BackendApplication.class);

    private final TrainersService trainersService;
    private final ExercisesService exercisesService;
    private final ModelMapperUtil modelMapperUtil;

    @Autowired
    public TrainerController(TrainersService trainersService, ExercisesService exercisesService, ModelMapperUtil modelMapperUtil) {
        this.trainersService = trainersService;
        this.exercisesService = exercisesService;
        this.modelMapperUtil = modelMapperUtil;
    }

    @GetMapping
    public ResponseEntity<Trainer> getTrainer() {
        Person person = SecurityUtil.getPerson();
        log.info("GET: /trainer/exercises  personId = " + person.getId());

        Trainer trainer = trainersService.findByPersonId(person.getId());

        return ResponseEntity.status(HttpStatus.OK).body(trainer);
    }

    @GetMapping("/exercises")
    public ResponseEntity<List<Exercise>> getAllExercises() {
        Person person = SecurityUtil.getPerson();
        log.info("GET: /trainer/exercises  personId = " + person.getId());

        Trainer trainer = trainersService.findByPersonId(person.getId());
        List<Exercise> exercises = trainer.getExercises();

        return ResponseEntity.status(HttpStatus.OK).body(exercises);
    }

    @GetMapping("/get_image/{id}")
    public ResponseEntity<?> getImage(@PathVariable("id") int id) throws IOException {
        Person person = SecurityUtil.getPerson();
        log.info("GET: /trainer/get_image/" + id +   "  personId = " + person.getId());

        Exercise exercise = exercisesService.findById(id);
        Trainer trainer = trainersService.findByPersonId(person.getId());

        if (exercise != null && exercise.getTrainer() == trainer) {
            byte[] image = Files.readAllBytes(new File(exercise.getImage()).toPath());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.valueOf("image/jpeg"))
                    .body(image);
        } else
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND).body("exercise not fund");
    }

    @GetMapping("/get_video/{id}")
    public ResponseEntity<FileSystemResource> getFullVideo(@PathVariable("id") int id) {
        Person person = SecurityUtil.getPerson();
        log.info("GET: /trainer/get_video/" + id +   "  personId = " + person.getId());

        Exercise exercise = exercisesService.findById(id);
        Trainer trainer = trainersService.findByPersonId(person.getId());
        if (exercise != null && exercise.getTrainer() == trainer) {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(new FileSystemResource(exercise.getVideo()));
        } else
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND).body(null);
    }
}
