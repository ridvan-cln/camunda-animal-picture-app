package com.ridvanacilan.camundaanimalpictureapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import io.camunda.zeebe.spring.test.ZeebeSpringTest;

@SpringBootTest
@AutoConfigureDataMongo
@ZeebeSpringTest
class CamundaAnimalPictureAppApplicationTests {

	@Test
	void contextLoads() {
	}

}
