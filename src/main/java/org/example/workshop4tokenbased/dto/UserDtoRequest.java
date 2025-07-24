package org.example.workshop4tokenbased.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

//TODO: Добавить нормальную валидацию пароля и почты
@Builder
public record UserDtoRequest(
        @NotBlank String login,
        @NotBlank @Size(min = 7) String password,
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "USER|ADMIN|USER_VIP") String role
) {
}