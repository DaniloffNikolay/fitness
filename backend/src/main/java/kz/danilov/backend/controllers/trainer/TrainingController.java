package kz.danilov.backend.controllers.trainer;

import kz.danilov.backend.BackendApplication;
import kz.danilov.backend.dto.trainers.NewTaskDTO;
import kz.danilov.backend.dto.trainers.NewTrainingDTO;
import kz.danilov.backend.dto.trainers.TaskDTO;
import kz.danilov.backend.dto.trainers.TrainingDTO;
import kz.danilov.backend.models.Person;
import kz.danilov.backend.models.trainers.Exercise;
import kz.danilov.backend.models.trainers.Task;
import kz.danilov.backend.models.trainers.Trainer;
import kz.danilov.backend.models.trainers.Training;
import kz.danilov.backend.security.SecurityUtil;
import kz.danilov.backend.services.trainers.TrainersService;
import kz.danilov.backend.services.trainers.TrainingsService;
import kz.danilov.backend.util.ModelMapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainer/training")
public class TrainingController {
    private static final Logger log = LoggerFactory.getLogger(BackendApplication.class);

    private final TrainersService trainersService;

    private final ModelMapperUtil modelMapperUtil;

    private final TrainingsService trainingsService;

    @Autowired
    public TrainingController(TrainersService trainersService, ModelMapperUtil modelMapperUtil, TrainingsService trainingsService) {
        this.trainersService = trainersService;
        this.modelMapperUtil = modelMapperUtil;
        this.trainingsService = trainingsService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<TrainingDTO>> getAllTrainings(){
    Person person = SecurityUtil.getPerson();
        log.info("GET: /trainer/trainings/all  personId = " + person.getId());
    Trainer trainer = trainersService.findByPersonId(person.getId());
    List<Training> trainings = trainer.getTrainings();
    return ResponseEntity.status(HttpStatus.OK).body(modelMapperUtil.convertToListOfTrainingDTO(trainings));

    }

    @PostMapping("/new")
    public ResponseEntity<TrainingDTO> postNewTraining(@RequestBody NewTrainingDTO newTrainingDTO){
        Person person = SecurityUtil.getPerson();
        log.info("POST: /trainer/training/new" + "  personId = " + person.getId());
        Trainer trainer = trainersService.findByPersonId(person.getId());

        Training training = modelMapperUtil.convertToTraining(newTrainingDTO);
        training.setTrainer(trainer);
        Training newTraining = trainingsService.save(training);

        return ResponseEntity.status(HttpStatus.OK).body(modelMapperUtil.convertToTrainingDTO(newTraining));
    }

}
