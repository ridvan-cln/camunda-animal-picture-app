package com.ridvanacilan.camundaanimalpictureapp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ridvanacilan.camundaanimalpictureapp.Service.PictureService;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

@CrossOrigin
@RestController
@RequestMapping("/animal-picture-process")
public class AnimalPictureProcessController {

    @Autowired
    private ZeebeClient client;
    @Autowired
    private PictureService pictureService;

    @PostMapping
    public ResponseEntity<Long> startProcess() {
        ProcessInstanceEvent process = client.newCreateInstanceCommand()
                .bpmnProcessId("Process_5dbz9ge")
                .latestVersion()
                .send()
                .join();

        return ResponseEntity.ok(process.getProcessInstanceKey());
    }

    @GetMapping(value = "{processInstanceKey}", produces = "image/jpeg")
    public ResponseEntity<byte[]> getAnimalPicture(@PathVariable String processInstanceKey) {
        byte[] image = pictureService.getAnimalPicture(processInstanceKey);
        if (image != null) {
            return ResponseEntity.ok().body(image);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
