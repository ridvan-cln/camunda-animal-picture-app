package com.ridvanacilan.camundaanimalpictureapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.camunda.zeebe.spring.client.annotation.Deployment;

@SpringBootApplication
@Deployment(resources = "classpath:v2.bpmn")
public class CamundaAnimalPictureAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamundaAnimalPictureAppApplication.class, args);
	}

}
