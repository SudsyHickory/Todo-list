package de.dm.todo.dto;

import de.dm.todo.model.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskCreateOrUpdateDto(
    @NotBlank String title,
    String description,
    @NotNull Status status,
    Long version
) {}
