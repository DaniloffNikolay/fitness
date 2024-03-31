package kz.danilov.backend.util;

import kz.danilov.backend.dto.PersonDTO;
import kz.danilov.backend.dto.PersonDataDTO;
import kz.danilov.backend.dto.PersonOnlyWithNameDTO;
import kz.danilov.backend.dto.trainers.*;
import kz.danilov.backend.models.Person;
import kz.danilov.backend.models.trainers.Exercise;
import kz.danilov.backend.models.trainers.Task;
import kz.danilov.backend.models.trainers.Training;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ModelMapperUtil {
    private final ModelMapper modelMapper;

    @Autowired
    public ModelMapperUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PersonDTO convertToPersonDTO(Person person) {
        return this.modelMapper.map(person, PersonDTO.class);
    }

    public Person convertToPerson(PersonDTO personDTO) {
        return this.modelMapper.map(personDTO, Person.class);
    }

    public PersonDataDTO convertToPersonDataDTO(Person person) {
        return this.modelMapper.map(person, PersonDataDTO.class);
    }

    public List<PersonDataDTO> convertToPersonDataDTOList(List<Person> persons) {
        List<PersonDataDTO> personDataDTOList = new ArrayList<>(persons.size());
        for (Person person : persons) {
            personDataDTOList.add(convertToPersonDataDTO(person));
        }

        return personDataDTOList;
    }

    public List<ExerciseDTO> convertToListOfExerciseDTO(List<Exercise> exercises) {
        List<ExerciseDTO> exerciseDTOList = new ArrayList<>(exercises.size());
        for (Exercise exercise : exercises) {
            exerciseDTOList.add(this.modelMapper.map(exercise, ExerciseDTO.class));
        }

        return exerciseDTOList;
    }
    public List<TrainingDTO> convertToListOfTrainingDTO(List<Training> trainings) {
        List<TrainingDTO> trainingDTOList = new ArrayList<>(trainings.size());
        for(Training training: trainings) {
            trainingDTOList.add(this.modelMapper.map(training, TrainingDTO.class));
        }
        return trainingDTOList;
    }

    public Exercise convertToExercise(NewExerciseDTO newExerciseDTO) {
        return this.modelMapper.map(newExerciseDTO, Exercise.class);
    }

    public ExerciseDTO convertToExerciseDTO(Exercise exercise) {
        return this.modelMapper.map(exercise, ExerciseDTO.class);
    }

    public List<TaskDTO> convertToListOfTaskDTO(List<Task> tasks) {
        List<TaskDTO> tasksDTOList = new ArrayList<>(tasks.size());
        for (Task task : tasks) {
            tasksDTOList.add(this.modelMapper.map(task, TaskDTO.class));
        }

        return tasksDTOList;
    }

    public Task convertToTask(TaskDTO taskDTO) {
        return this.modelMapper.map(taskDTO, Task.class);
    }

    public TaskDTO convertToTaskDTO(NewTaskDTO taskDTO) {
        return this.modelMapper.map(taskDTO, TaskDTO.class);
    }

    public TaskDTO convertToTaskDTO(Task Task) {
        return this.modelMapper.map(Task, TaskDTO.class);
    }
}
