package com.example.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TodoRequest(
        @NotBlank String title,
        @NotNull Boolean completed
) {
}