package de.dm.todo.service;

import java.util.List;
import java.util.Objects;

import de.dm.todo.dto.TaskCreateOrUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import de.dm.todo.dto.TaskDto;
import de.dm.todo.exception.TaskNotFoundException;
import de.dm.todo.model.Task;
import de.dm.todo.repository.TaskRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    public TaskDto getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<TaskDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public Page<TaskDto> getAllTasksWithPageable(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(this::toDto);
    }

    @Transactional
    public TaskDto createTask(TaskCreateOrUpdateDto taskDto) {
        Task task = new Task(
            taskDto.title(),
            taskDto.description(),
            taskDto.status()
        );

        Task savedTask = taskRepository.save(task);
        return toDto(savedTask);
    }

    @Transactional
    public TaskDto updateTask(Long id, TaskCreateOrUpdateDto taskDto) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));

        if (!Objects.equals(task.getVersion(), taskDto.version())) {
            throw new ObjectOptimisticLockingFailureException(Task.class, id);
        }

        task.setTitle(taskDto.title());
        task.setDescription(taskDto.description());
        task.setStatus(taskDto.status());

        return toDto(taskRepository.save(task));
    }

    @Transactional
    public void deleteTask(Long id) {
        taskRepository.findById(id)
            .ifPresentOrElse(
                    taskRepository::delete,
                    () -> { throw new TaskNotFoundException(id); }
            );
    }

    private TaskDto toDto(Task task) {
        return new TaskDto(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getVersion()
        );
    }


}
