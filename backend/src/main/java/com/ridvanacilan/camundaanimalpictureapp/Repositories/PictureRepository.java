package com.ridvanacilan.camundaanimalpictureapp.Repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ridvanacilan.camundaanimalpictureapp.Model.Picture;

public interface PictureRepository extends MongoRepository<Picture, String> {
}
