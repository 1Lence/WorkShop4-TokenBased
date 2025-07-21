package org.example.workshop4tokenbased.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
public record UserCredentialsDto(
        String login,
        @Email
        String email,
        //Добавить валидацию
        String password
) {
}