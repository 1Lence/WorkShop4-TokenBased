package org.example.workshop4tokenbased.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record UserDtoRequest(
        @NotBlank String login,
        @NotBlank String password,
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "USER|ADMIN|USER_VIP") String role
) {
}