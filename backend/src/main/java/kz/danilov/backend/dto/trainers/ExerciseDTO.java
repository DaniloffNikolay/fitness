package kz.danilov.backend.dto.trainers;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

/**
 * User: Nikolai Danilov
 * Date: 29.10.2023
 */
public class ExerciseDTO {
    private int id;
    private String name;
    private String muscle;
    private String description;

    public ExerciseDTO() {
    }

    public ExerciseDTO(int id, String name, String muscle, String description) {
        this.id = id;
        this.name = name;
        this.muscle = muscle;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMuscle() {
        return muscle;
    }

    public void setMuscle(String muscle) {
        this.muscle = muscle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ExerciseDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", muscle='" + muscle + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
