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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.process.test.api.ZeebeTestEngine;

public class AnimalProcessTest extends CamundaAnimalPictureAppApplicationTests {

  private static final String STARTEVENT_ID = "StartEvent_1";
  private static final String SERVICETASK_ID = "Activity_0ra0ixs";
  private static final String USERTASK_ID = "decide-animal";
  private static final String PROCESS_ID = "Process_5dbz9ge";

  @Autowired
  private ZeebeClient zeebe;
  @Autowired
  private ZeebeTestEngine zeebeTestEngine;

  @Test
  public void testProcessIsDeployedAndCanBeStarted() {
    ProcessInstanceEvent processInstance = zeebe.newCreateInstanceCommand()
        .bpmnProcessId(PROCESS_ID)
        .latestVersion()
        .send()
        .join();

    assertThat(processInstance).isActive();
  }

  @Test
  public void testProcessGetCatPicture() throws InterruptedException, TimeoutException {
    // given
    Map<String, Object> variables = Collections.singletonMap("animalType", "cat");

    // when
    ProcessInstanceEvent processInstance = zeebe.newCreateInstanceCommand()
        .bpmnProcessId(PROCESS_ID)
        .latestVersion()
        .send()
        .join();

    // then process is waiting for user task to be completed
    assertThat(processInstance)
        .hasPassedElement(STARTEVENT_ID)
        .hasNotPassedElement(USERTASK_ID)
        .isActive();
    
    // when
    waitForUserTaskAndComplete(
        USERTASK_ID, variables);

    // then process is waiting for service task to be completed
    assertThat(processInstance)
        .hasPassedElementsInOrder(STARTEVENT_ID, USERTASK_ID)
        .hasNotPassedElement(SERVICETASK_ID)
        .isActive();

    // when
    completeServiceTask("getAnimalPicture");

    // then
    assertThat(processInstance)
        .hasPassedElementsInOrder(STARTEVENT_ID, USERTASK_ID, SERVICETASK_ID)
        .hasVariableWithValue("animalType", "cat")
        .isCompleted();
  }

  // source:
  // https://github.com/camunda-community-hub/camunda-8-examples/blob/main/twitter-review-java-springboot/src/test/java/org/camunda/community/examples/twitter/TestTwitterProcess.java
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

  // source:
  // https://github.com/camunda/zeebe-process-test/blob/main/examples/src/test/java/io/camunda/zeebe/process/test/examples/PullRequestProcessTest.java
  private void completeServiceTask(final String jobType)
      throws InterruptedException, TimeoutException {
    completeServiceTasks(jobType, 1);
  }
  private void completeServiceTasks(final String jobType, final int count)
      throws InterruptedException, TimeoutException {

    final var activateJobsResponse = zeebe.newActivateJobsCommand().jobType(jobType).maxJobsToActivate(count).send()
        .join();

    final int activatedJobCount = activateJobsResponse.getJobs().size();
    if (activatedJobCount < count) {
      Assertions.fail(
          "Unable to activate %d jobs, because only %d were activated."
              .formatted(count, activatedJobCount));
    }

    for (int i = 0; i < count; i++) {
      final var job = activateJobsResponse.getJobs().get(i);

      zeebe.newCompleteCommand(job.getKey()).send().join();
    }

    zeebeTestEngine.waitForIdleState(Duration.ofSeconds(1));
  }
}
