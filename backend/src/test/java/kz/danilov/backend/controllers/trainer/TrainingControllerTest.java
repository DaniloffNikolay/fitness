package kz.danilov.backend.controllers.trainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.danilov.backend.dto.trainers.NewTaskDTO;
import kz.danilov.backend.dto.trainers.NewTrainingDTO;
import kz.danilov.backend.dto.trainers.TrainingDTO;
import kz.danilov.backend.models.Person;
import kz.danilov.backend.models.trainers.Exercise;
import kz.danilov.backend.models.trainers.Task;
import kz.danilov.backend.models.trainers.Trainer;
import kz.danilov.backend.models.trainers.Training;
import kz.danilov.backend.security.JWTUtil;
import kz.danilov.backend.services.PeopleService;
import kz.danilov.backend.services.trainers.*;
import kz.danilov.backend.util.ModelMapperUtil;
import kz.danilov.backend.utils.Utils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * User: Nikolai Danilov
 * Date: 31.03.2024
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TrainingControllerTest {

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;
    private final PeopleService peopleService;
    private final JWTUtil jwtUtil;
    private final TrainersService trainersService;
    private final ExercisesService exercisesService;
    private final TasksService tasksService;
    private final TrainingsService trainingsService;
    private final ModelMapperUtil modelMapperUtil;

    @Autowired
    public TrainingControllerTest(ObjectMapper objectMapper, MockMvc mockMvc, PeopleService peopleService, JWTUtil jwtUtil, TrainersService trainersService, ExercisesService exercisesService, TasksService tasksService, TrainingsService trainingsService, ModelMapperUtil modelMapperUtil) {
        this.objectMapper = objectMapper;
        this.mockMvc = mockMvc;
        this.peopleService = peopleService;
        this.jwtUtil = jwtUtil;
        this.trainersService = trainersService;
        this.exercisesService = exercisesService;
        this.tasksService = tasksService;
        this.trainingsService = trainingsService;
        this.modelMapperUtil = modelMapperUtil;
    }

    private Person personTrainer = null;
    private Trainer trainer = null;
    private String trainerToken = null;
    private List<Training> trainings;
    private List<Task> tasks;
    private List<Exercise> exercises;

    @BeforeAll
    public void initialize() {
        resetDb();
        personTrainer = peopleService.save(Utils.personTrainer);
        trainer = trainersService.saveNewTrainer(personTrainer.getId());
        trainerToken = jwtUtil.generateToken(personTrainer.getName());

        exercises = Utils.getExercises(trainer);
        exercises.forEach(exercisesService::save);
        tasks = Utils.getTasks(trainer);
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);

            switch (i) {
                case 0, 1, 2, 13 -> task.setExercise(exercises.get(0));
                case 3, 4, 5, 14 -> task.setExercise(exercises.get(1));
                case 6, 7, 8, 9, 10, 11, 12 -> task.setExercise(exercises.get(2));
            }

            tasksService.save(task);
        }

        trainings = Utils.getTrainings(trainer);

        trainings.get(0).setTasks(tasks);
        List<Task> tasksForTrainingTwo = Arrays.asList(tasks.get(3), tasks.get(4), tasks.get(5));
        trainings.get(1).setTasks(tasksForTrainingTwo);
        List<Task> tasksForTrainingThree = Arrays.asList(tasks.get(6), tasks.get(7), tasks.get(8), tasks.get(9), tasks.get(10), tasks.get(11));
        trainings.get(2).setTasks(tasksForTrainingThree);

        trainingsService.save(trainings.get(0));
        trainingsService.save(trainings.get(1));
        trainingsService.save(trainings.get(2));
    }

    @AfterAll
    public void after() {
        resetDb();
    }

    private void resetDb() {
        trainersService.deleteAll();
        peopleService.deleteAll();
        exercisesService.deleteAll();
        trainingsService.deleteAll();
        tasksService.deleteAll();
    }

    @Test
    void getAllTasks() throws Exception {
        getListTrainingsThenCheckIt();
    }

    private void getListTrainingsThenCheckIt() throws Exception {
        String data = Utils.getResultActionsWithTokenButWithoutBody(mockMvc,
                        "/trainer/training/all",
                        trainerToken)
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(data.contains("" + trainings.get(0).getId()));
    }
    @Test
    void postNewTraining() throws Exception{
        NewTrainingDTO newTrainingDTO = Utils.createTestNewTrainingDTO();
        postNewTrainingDTOAndCheckIt(newTrainingDTO);
    }

    private void postNewTrainingDTOAndCheckIt(NewTrainingDTO newTrainingDTO) throws Exception {
        String data = Utils.postResultActionsWithTokenAndBody(mockMvc,
                        "/trainer/training/new",
                        trainerToken,
                        objectMapper,
                        newTrainingDTO)
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(data.contains("" + newTrainingDTO.getDescription()));

        boolean result = false;
        for (Training training : trainingsService.findAll()) {
            if (training.getName().equals(newTrainingDTO.getName())) {
                if (training.getTrainer().getId() == trainer.getId()) {
                    result = true;
                    break;
                }
            }
        }

        assertTrue(result);
    }
}
