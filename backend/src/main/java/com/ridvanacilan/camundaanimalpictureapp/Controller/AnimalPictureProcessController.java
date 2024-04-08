package com.ridvanacilan.camundaanimalpictureapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ridvanacilan.camundaanimalpictureapp.Repositories.PictureRepository;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import com.ridvanacilan.camundaanimalpictureapp.Model.Picture;

@CrossOrigin
@RestController
@RequestMapping("/animal-picture-process")
public class AnimalPictureProcessController {

    @Autowired
    private ZeebeClient client;
    @Autowired
    private PictureRepository pictureRepository;

    @PostMapping(produces = "application/json")
    public ResponseEntity<Long> startProcess() {
        ProcessInstanceEvent future = client.newCreateInstanceCommand()
                .bpmnProcessId("Process_5dbz9ge")
                .latestVersion()
                .send()
                .join();

        return ResponseEntity.ok(future.getProcessInstanceKey());
    }

    @GetMapping(value = "{processInstanceKey}", produces = "image/jpeg")
    public ResponseEntity<byte[]> getAnimalPicture(@PathVariable String processInstanceKey) {
        Optional<Picture> picture = pictureRepository.findById(processInstanceKey);
        return picture.map(p -> ResponseEntity.ok().body(p.getImage().getData()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
