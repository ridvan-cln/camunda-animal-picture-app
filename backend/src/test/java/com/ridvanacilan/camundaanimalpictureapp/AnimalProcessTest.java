package com.ridvanacilan.camundaanimalpictureapp;

import static io.camunda.zeebe.process.test.assertions.BpmnAssert.assertThat;
import static io.camunda.zeebe.protocol.Protocol.USER_TASK_JOB_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;
import io.camunda.zeebe.spring.test.ZeebeSpringTest;

@SpringBootTest
@ZeebeSpringTest
public class AnimalProcessTest {

  @MockBean
  private AnimalPictureJobWorker worker;
  @Autowired
  private ZeebeClient zeebe;
  @Autowired
  private ZeebeTestEngine zeebeTestEngine;

  @Test
  public void getCatPicture() throws InterruptedException, TimeoutException {
    Map<String, Object> variables = Collections.singletonMap("animalType", "cat");

    ProcessInstanceEvent processInstance = zeebe.newCreateInstanceCommand()
        .bpmnProcessId("Process_5dbz9ge")
        .latestVersion()
        .send()
        .join();

    waitForUserTaskAndComplete(
        "decide-animal", variables);

    assertThat(processInstance)
        .hasPassedElement("decide-animal")
        .isActive();

    // verify that the worker was called
    // not sure why this is not working
    // Mockito.verify(worker).getAnimalPicture(Mockito.any(), eq("cat"));
  }

  public void waitForUserTaskAndComplete(String userTaskId, Map<String, Object> variables)
      throws InterruptedException, TimeoutException {
    // Let the workflow engine do whatever it needs to do
    zeebeTestEngine.waitForIdleState(Duration.ofMinutes(5));

    // Now get all user tasks
    List<ActivatedJob> jobs = zeebe
        .newActivateJobsCommand()
        .jobType(USER_TASK_JOB_TYPE)
        .maxJobsToActivate(1)
        .workerName("waitForUserTaskAndComplete")
        .send()
        .join()
        .getJobs();

    // Should be only one
    assertTrue(jobs.size() > 0, "Job for user task '" + userTaskId + "' does not exist");
    ActivatedJob userTaskJob = jobs.get(0);
    // Make sure it is the right one
    if (userTaskId != null) {
      assertEquals(userTaskId, userTaskJob.getElementId());
    }

    // And complete it passing the variables
    if (variables != null && variables.size() > 0) {
      zeebe.newCompleteCommand(userTaskJob.getKey()).variables(variables).send().join();
    } else {
      zeebe.newCompleteCommand(userTaskJob.getKey()).send().join();
    }
  }
}
