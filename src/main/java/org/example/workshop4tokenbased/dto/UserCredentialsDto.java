package org.example.workshop4tokenbased.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserCredentialsDto(
        @NotBlank String login,
        @NotBlank String password
) {
}