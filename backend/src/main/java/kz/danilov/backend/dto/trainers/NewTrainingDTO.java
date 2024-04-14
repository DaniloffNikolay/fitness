package kz.danilov.backend.dto.trainers;

public class NewTrainingDTO {

    private String name;

    private String description;



    public NewTrainingDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public NewTrainingDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
