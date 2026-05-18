package de.dm.todo.service;

import java.util.List;

import de.dm.todo.dto.TaskDto;
import de.dm.todo.dto.TaskUpdateDto;

public interface TaskService {
    TaskDto getTaskById(Long id);
    List<TaskDto> getAllTasks();
    TaskDto createTask(TaskDto taskDto);
    TaskDto updateTask(Long id, TaskUpdateDto taskDto);
    void deleteTask(Long id);

}
