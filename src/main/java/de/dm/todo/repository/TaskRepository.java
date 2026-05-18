package de.dm.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.dm.todo.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
