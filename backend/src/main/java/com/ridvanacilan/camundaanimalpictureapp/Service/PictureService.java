package com.ridvanacilan.camundaanimalpictureapp.Service;

import java.util.Optional;

import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.ridvanacilan.camundaanimalpictureapp.Model.AnimalType;
import com.ridvanacilan.camundaanimalpictureapp.Model.Picture;
import com.ridvanacilan.camundaanimalpictureapp.Repositories.PictureRepository;

@Service
public class PictureService {
    private final RestClient restClient = RestClient.create();

    @Autowired
    private PictureRepository pictureRepository;

    public byte[] getAnimalPicture(String processInstanceKey) {
        Optional<Picture> picture = pictureRepository.findById(processInstanceKey);
        return picture.map(p -> p.getImage().getData()).orElse(null);
    }

    public void downloadAndSaveRandomAnimalPicture(String processInstanceKey, String animalType) {
        byte[] imageBytes = downloadRandomAnimalPicture(animalType);
        Binary image = new Binary(imageBytes);
        Picture picture = new Picture();
        picture.setImage(image);
        picture.setId(processInstanceKey);
        pictureRepository.save(picture);
    }

    private byte[] downloadRandomAnimalPicture(String animalType) {
        int width = 200;
        int height = 300;
        String url = AnimalType.fromString(animalType).getUrl(width, height);

        ResponseEntity<byte[]> response = restClient.get().uri(url).retrieve().toEntity(byte[].class);
        return response.getBody();
    }
}
