package de.dm.todo.web;

import de.dm.todo.controller.TaskController;
import de.dm.todo.dto.TaskCreateOrUpdateDto;
import de.dm.todo.exception.TaskNotFoundException;
import de.dm.todo.model.Status;
import de.dm.todo.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;


import static org.mockito.Mockito.*;

@WebMvcTest(TaskController.class)
@AutoConfigureRestTestClient
public class TaskControllerWebMvcTest {

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private TaskService taskService;

    @Test
    void shouldReturnBadRequestWhenTitleIsBlank()
    {
        TaskCreateOrUpdateDto taskCreateOrUpdateDto = new TaskCreateOrUpdateDto("", "desc", Status.TODO, 0L);

        restTestClient.post().uri("/tasks")
                .body(taskCreateOrUpdateDto)
                .exchange()
                .expectStatus().isBadRequest();
        verifyNoInteractions(taskService);
    }

    @Test
    void shouldReturnNotFoundWhenTaskNotFound()
    {
        Long nonExistingId = 999L;
        Mockito.when(taskService.getTaskById(nonExistingId))
                .thenThrow(new TaskNotFoundException(nonExistingId));

        restTestClient.get().uri("/tasks/"+nonExistingId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void shouldReturnBadRequestWhenStatusIsNull()
    {
        TaskCreateOrUpdateDto taskCreateOrUpdateDto = new TaskCreateOrUpdateDto("title", "desc", null, 0L);

        restTestClient.post().uri("/tasks")
                .body(taskCreateOrUpdateDto)
                .exchange()
                .expectStatus().isBadRequest();
        verifyNoInteractions(taskService);
    }
}
