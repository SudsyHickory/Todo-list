package de.dm.todo.dto;

public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatusDto status;

    public TaskDto() {
    }

    public TaskDto(Long id, String title, String description, TaskStatusDto status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatusDto getStatus() {
        return status;
    }

    public void setStatus(TaskStatusDto status) {
        this.status = status;
    }
}
