package com.ridvanacilan.camundaanimalpictureapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import io.camunda.zeebe.spring.test.ZeebeSpringTest;

@SpringBootTest
@TestPropertySource(properties = {"spring.config.name=test"})
@ZeebeSpringTest
class CamundaAnimalPictureAppApplicationTests {

	@Test
	void contextLoads() {
	}

}
