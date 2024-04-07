package com.ridvanacilan.camundaanimalpictureapp;

import org.bson.types.Binary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ridvanacilan.camundaanimalpictureapp.Model.Picture;
import com.ridvanacilan.camundaanimalpictureapp.Repositories.PictureRepository;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;

@Component
public class AnimalPictureJobWorker {

    private final PictureRepository pictureRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    public AnimalPictureJobWorker(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    @JobWorker(timeout = 1000000)
    public void getAnimalPicture(final ActivatedJob job, @Variable String animalType) {
        System.out.println("Processing job: " + job);

        byte[] imageBytes = getRandomAnimalPicture(animalType);
        Binary image = new Binary(imageBytes);
        Picture picture = new Picture();
        picture.setImage(image);
        picture.setId(String.valueOf(job.getProcessInstanceKey()));
        pictureRepository.save(picture);
    }

    private byte[] getRandomAnimalPicture(String animalType) {
        int width = 200;
        int height = 300;
        String url;
        switch (animalType.toLowerCase()) {
            case "cat":
                url = "https://placekitten.com/" + width + "/" + height;
                break;
            case "dog":
                url = "https://place.dog/" + width + "/" + height;
                break;
            case "bear":
                url = "https://placebear.com/" + width + "/" + height;
                break;
            default:
                throw new IllegalArgumentException("Unsupported animal type: " + animalType);
        }

        ResponseEntity<byte[]> response = restTemplate.getForEntity(url, byte[].class);
        System.out.println(response.getStatusCode());
        return response.getBody();
    }
}
