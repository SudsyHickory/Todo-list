package de.dm.todo.controller;

import de.dm.todo.dto.TaskDto;
import de.dm.todo.dto.TaskCreateOrUpdateDto;
import de.dm.todo.service.TaskService;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAll() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/pageable")
    public ResponseEntity<Page<TaskDto>> getAllWithPageable(@PageableDefault(size = 12) Pageable pageable) {
        Page<TaskDto> todos = taskService.getAllTasksWithPageable(pageable);
        return ResponseEntity.ok(todos);
    }

    @GetMapping("{id}")
    public ResponseEntity<TaskDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping
    public ResponseEntity<TaskDto> create(@Valid @RequestBody TaskCreateOrUpdateDto taskDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(taskDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> update(@PathVariable Long id, @Valid @RequestBody TaskCreateOrUpdateDto taskDto) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDto));
    }
}
