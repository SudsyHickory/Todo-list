package de.dm.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskUpdateDto(
    @NotBlank String title,
    String description,
    @NotNull TaskStatusDto status
) {}
