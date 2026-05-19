package de.dm.todo.integration;

import de.dm.todo.dto.TaskCreateOrUpdateDto;
import de.dm.todo.dto.TaskDto;
import de.dm.todo.model.Status;
import de.dm.todo.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.client.RestTestClient;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainersConfiguration.class)
@AutoConfigureRestTestClient
@Sql(scripts = "/sql/add-tasks.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clear-tasks.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class TaskControllerIntegrationTest {

    @Autowired
    private RestTestClient restClient;

    @Test
    void shouldReturnAllTasks() {
        restClient.get().uri("/tasks")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<TaskDto>>() {
                })
                .value(tasks -> {
                    assertThat(tasks).isNotNull();
                    assertThat(tasks).hasSize(3);
                });
    }

    @Test
    void shouldReturnTaskById() {
        TaskDto task = restClient.get().uri("/tasks/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskDto.class)
                .returnResult()
                .getResponseBody();
        assertThat(task).isNotNull();
        assertThat(task.status()).isEqualTo(Status.TODO);
        assertThat(task.title()).isEqualTo("Buy milk");
    }

    @Test
    void shouldCreateTaskAndReturnVariable() {
        TaskCreateOrUpdateDto newTask = new TaskCreateOrUpdateDto
                ("title", "desc", Status.TODO, 0L);

        TaskDto savedTask = restClient.post().uri("/tasks")
                .body(newTask)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(TaskDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(savedTask).isNotNull();
        assertThat(savedTask.id()).isNotNull();
        assertThat(savedTask.title()).isEqualTo("title");
    }

    @Test
    void shouldUpdateTaskSuccessfully() {
        TaskDto currentTask = restClient.get().uri("/tasks/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(currentTask).isNotNull();

        TaskCreateOrUpdateDto validUpdate = new TaskCreateOrUpdateDto(
                "Updated title",
                "Updated description",
                Status.TODO,
                0L
        );

        TaskDto updatedTask = restClient.put().uri("/tasks/1")
                .body(validUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.title()).isEqualTo("Updated title");
    }

    @Test
    void shouldDeleteTaskAndReturnNotFoundOnSubsequentGet() {
        restClient.delete().uri("/tasks/2")
                .exchange()
                .expectStatus().isNoContent();

        restClient.get().uri("/tasks/2")
                .exchange()
                .expectStatus().isNotFound();
    }


    @Test
    void optimisticLockingTest() {
        TaskDto responseA = restClient.get().uri("/tasks/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskDto.class)
                .returnResult()
                .getResponseBody();

        TaskDto responseB = restClient.get().uri("/tasks/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(TaskDto.class)
                .returnResult()
                .getResponseBody();

        assertThat(responseA).isNotNull();
        assertThat(responseB).isNotNull();

        TaskDto responseC = new TaskDto(responseA.id(), responseA.title(), "desc", responseA.status(), responseA.version());
        restClient.put().uri("/tasks/1")
                .body(responseC)
                .exchange()
                .expectStatus().isOk();

        TaskDto responseD = new TaskDto(responseA.id(), responseA.title(), "description", responseA.status(), responseA.version());
        restClient.put().uri("/tasks/1")
                .body(responseD)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);

    }
}
