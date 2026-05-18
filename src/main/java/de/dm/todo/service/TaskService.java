package de.dm.todo.service;

import java.util.List;

import de.dm.todo.dto.TaskDto;

public interface TaskService {
    TaskDto getTaskById(Long id);
    List<TaskDto> getAllTasks();
    TaskDto createTask(TaskDto taskDto);
    TaskDto updateTask(Long id, TaskDto taskDto);
    void deleteTask(Long id);

}
