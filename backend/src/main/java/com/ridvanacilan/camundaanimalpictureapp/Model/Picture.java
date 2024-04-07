package com.ridvanacilan.camundaanimalpictureapp.Model;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pictures")
public class Picture {
    @Id
    private String id;

    private String job;

    private Binary image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(Binary image) {
        this.image = image;
    }

    public Binary getImage() {
        return image;
    }
}