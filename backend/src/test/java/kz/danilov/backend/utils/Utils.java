package kz.danilov.backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.danilov.backend.dto.PersonDTO;
import kz.danilov.backend.dto.trainers.*;
import kz.danilov.backend.models.Person;
import kz.danilov.backend.models.trainers.*;
import kz.danilov.backend.util.ModelMapperUtil;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * User: Nikolai Danilov
 * Date: 04.01.2024
 */
public class Utils {

    public static final Person personAdmin = new Person(
            0,
            "admin",
            "$2a$10$NzE3.ehopU.LN6eqFby/nOtDNHq/x7XPjmxPqDoRKY2DDEqRlJl56", //admin
            "ROLE_ADMIN");
    public static final Person personTrainer = new Person(
            0,
            "trainer",
            "$2a$10$ziRMsJUXmUgTLSPenkWY4udkty4NOGmWuw/dyqjhZF3ky0mAAZsDK", //trainer
            "ROLE_TRAINER");
    public static final Person personUser = new Person(
            0,
            "user",
            "$2a$10$DVx9f1djz1PI/SK3J4uhseGbaGu0XuDZIJD/p8Zwthix.DvyXFtvm", //user
            "ROLE_USER");
    public static final Person personPremium = new Person(
            0,
            "premium",
            "$2a$10$2eAE6E/J1HXSw1EiQAukYeyxRdnzU73sbzbV1QvK.gNMdYwi43g.S", //premium
            "ROLE_PREMIUM"
    );

    public static List<Exercise> getExercises() {
        return Arrays.asList(
                new Exercise(0, "прес от пола", "прес", "Классическоеупражнение на мышцы преса", "D:\\Project\\db\\pressVideo.MOV", "D:\\Project\\db\\press.jpg", null, null),
                new Exercise(0, "классическое приседание", "квадрицепсы", "основа, которой нужно овладеть перед тем, как приступать к другим разновидностям этого приседаний", "D:\\Project\\db\\squatVideo.MOV", "D:\\Project\\db\\hands.jpg", null, null),
                new Exercise(0, "классическое отжимание", "большая грудная", "Примите упор лежа на руках и ногах. Ноги должны быть вместе, а ваши ладони направьте вперед, так чтобы они были на ширине плеч. Затем необходимо выпрямить руки, при этом важно чтобы шея, спина и ноги были прямыми. Сгибайте руки в локтях, опускаясь к полу.", "D:\\Project\\db\\push-upsVideo.MOV", "D:\\Project\\db\\hands.jpg", null, null)
        );
    }

    public static List<Exercise> getExercises(Trainer trainer) {
        List<Exercise> exercises = getExercises();
        exercises.forEach((e) -> e.setTrainer(trainer));
        return exercises;
    }

    public static List<Task> getTasks() {
        return Arrays.asList(
                new Task(0, "пресс новичка", "зачем нужно это поле?", null, 10, (byte) 0, null, null),
                new Task(0, "пресс продвинутого", "зачем нужно это поле?", null, 20, (byte) 0, null, null),
                new Task(0, "пресс профи", "зачем нужно это поле?", null, 30, (byte) 0, null, null),

                new Task(0, "приседание новичка", "зачем нужно это поле?", null, 20, (byte) 0, null, null),
                new Task(0, "приседание продвинутого", "зачем нужно это поле?", null, 50, (byte) 0, null, null),
                new Task(0, "приседание профи", "зачем нужно это поле?", null, 100, (byte) 0, null, null),

                new Task(0, "отжимание новичка", "зачем нужно это поле?", null, 10, (byte) 0, null, null),
                new Task(0, "отжимание супер новичка", "зачем нужно это поле?", null, 15, (byte) 0, null, null),
                new Task(0, "отжимание продвинутого", "зачем нужно это поле?", null, 20, (byte) 0, null, null),
                new Task(0, "отжимание супер продвинутого", "зачем нужно это поле?", null, 30, (byte) 0, null, null),
                new Task(0, "отжимание профи", "зачем нужно это поле?", null, 40, (byte) 0, null, null),
                new Task(0, "отжимание супер профи", "зачем нужно это поле?", null, 50, (byte) 0, null, null),

                new Task(0, "тест", "для теста типа", null, 60, (byte) 1, null, null),
                new Task(0, "тест", "для теста типа", null, 2, (byte) 2, null, null)
        );
    }

    public static List<Task> getTasks(Trainer trainer) {
        List<Task> tasks = getTasks();
        tasks.forEach((e) -> e.setTrainer(trainer));
        return tasks;
    }

    public static List<Training> getTrainings() {
        return Arrays.asList(
                new Training(0, "Стандартная тренировка", "Делаются все упражнения без спешки и прочего", null, null, null),
                new Training(0, "Улучшенная тренировка", "Делаются упор на мышци груди", null, null, null),
                new Training(0, "Силовая тренировка ног", "Тренировка посвещена приседанием", null, null, null)
        );
    }

    public static List<Training> getTrainings(Trainer trainer) {
        List<Training> trainings = getTrainings();
        trainings.forEach((e) -> e.setTrainer(trainer));
        return trainings;
    }

    public static ResultActions getResultActionsWithTokenButWithoutBody(MockMvc mockMvc, String url, String token) throws Exception {
        return mockMvc.perform(get(url)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON));
    }

    public static ResultActions getResultActionsWithTokenAndBody(MockMvc mockMvc, String url, String token, ObjectMapper objectMapper, Object object) throws Exception {
        return mockMvc.perform(get(url)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(object))
                .contentType(MediaType.APPLICATION_JSON));
    }

    public static ResultActions putResultActionsWithTokenAndBody(MockMvc mockMvc, String url, String token, ObjectMapper objectMapper, Object object) throws Exception {
        return mockMvc.perform(put(url)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(object))
                .contentType(MediaType.APPLICATION_JSON));
    }


    public static ResultActions postResultActionsWithTokenAndBody(MockMvc mockMvc, String url, String token, ObjectMapper objectMapper, Object object) throws Exception {
        return mockMvc.perform(post(url)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(object))
                .contentType(MediaType.APPLICATION_JSON));
    }

    public static ResultActions postResultActionsWithoutTokenButWithBody(MockMvc mockMvc, String url, ObjectMapper objectMapper, Object object) throws Exception {
        return mockMvc.perform(post(url)
                .content(objectMapper.writeValueAsString(object))
                .contentType(MediaType.APPLICATION_JSON));
    }

    public static PersonDTO createTestUserPersonDTO() {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName("Tom");
        personDTO.setPassword("password");

        return personDTO;
    }

    public static PersonDTO createTestTrainerPersonDTO() {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName("Trainer");
        personDTO.setPassword("Trainer");

        return personDTO;
    }

    public static NewExerciseDTO createTestNewExerciseDTO() {
        return new NewExerciseDTO("Упражнение", "какие-то", "все для теста и только для теста");
    }

    public static ExerciseDTO createTestExerciseDTO() {
        return new ExerciseDTO(0, "Упражнение", "какие-то", "все для теста и только для теста");
    }

    public static ExerciseDTO createTestEditExerciseDTO(Exercise exercise, ModelMapperUtil modelMapperUtil) {
        ExerciseDTO exerciseDTO =modelMapperUtil.convertToExerciseDTO(exercise);
        exerciseDTO.setDescription("тест успешный!!!");
        return exerciseDTO;
    }

    public static NewTaskDTO createTestNewTaskDTO(Exercise exercise) {
        return new NewTaskDTO("тестовый", "неизвестное описание", exercise.getId(), 50, (byte) 1);
    }

    public static NewTrainingDTO createTestNewTrainingDTO(){
        return new NewTrainingDTO( "testName", "test");
    }

    public static String getOldToken() {
        return "eyJ0eXAiOiJKV1QiLCJhbGciOSiJIUzI1NiJ9.eyJzdWIiOiJVc2VyIGRldGFpbHMiLCJpc3MiOiJmaXRuZXNzIiwiZXhwIjoxNjk2MTY0MDg5LCJpYXQiOjE2OTYxNjIyODksInVzZXJuYW1lIjoidGVzdCJ9.NRQcNhk7HqN71nqfR2Icv3f3nTbylXVBj2fixCY7608";
    }

    public static byte[] loadBuffer(String path) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
        byte[] buffer = new byte[bis.available()];
        bis.read(buffer);
        bis.close();
        return buffer;
    }
}
