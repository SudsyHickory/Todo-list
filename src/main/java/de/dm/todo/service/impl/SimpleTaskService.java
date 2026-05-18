package de.dm.todo.service.impl;

import java.util.List;

import de.dm.todo.dto.TaskUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import de.dm.todo.dto.TaskDto;
import de.dm.todo.exception.TaskNotFoundException;
import de.dm.todo.model.Task;
import de.dm.todo.repository.TaskRepository;
import de.dm.todo.service.TaskService;

@Service
@RequiredArgsConstructor
public class SimpleTaskService implements TaskService {

    private final TaskRepository taskRepository;

    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));

        return toDto(task);
    }

    public List<TaskDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(this::toDto).toList();
    }

    public TaskDto createTask(TaskDto taskDto) {
        Task task = new Task(
            taskDto.title(),
            taskDto.description(),
            taskDto.status()
        );

        Task savedTask = taskRepository.save(task);
        return toDto(savedTask);
    }

    public TaskDto updateTask(Long id, TaskUpdateDto taskDto) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setStatus(taskDto.status());

        Task updatedTask = taskRepository.save(task);
        return toDto(updatedTask);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }

        taskRepository.deleteById(id);
    }

    private TaskDto toDto(Task task) {
        return new TaskDto(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus()
        );
    }
}
