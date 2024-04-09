package com.ridvanacilan.camundaanimalpictureapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ridvanacilan.camundaanimalpictureapp.Service.PictureService;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;

@Component
public class AnimalPictureJobWorker {

    @Autowired
    private PictureService pictureService;

    @JobWorker(timeout = 1000000)
    public void getAnimalPicture(final ActivatedJob job, @Variable String animalType) {
        pictureService.downloadAndSaveRandomAnimalPicture(String.valueOf(job.getProcessInstanceKey()), animalType);
    }
}
