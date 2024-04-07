package com.ridvanacilan.camundaanimalpictureapp.Service;

import java.io.IOException;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ridvanacilan.camundaanimalpictureapp.Model.Picture;
import com.ridvanacilan.camundaanimalpictureapp.Repositories.PictureRepository;

@Service
public class PhotoService {

    @Autowired
    private PictureRepository photoRepo;

    public String addPhoto(String title, MultipartFile file) throws IOException { 
        Picture photo = new Picture(); 
        photo.setImage(
          new Binary(BsonBinarySubType.BINARY, file.getBytes())); 
        photo = photoRepo.insert(photo); return photo.getId(); 
    }

    public Picture getPhoto(String id) { 
        return photoRepo.findById(id).get(); 
    }
}
