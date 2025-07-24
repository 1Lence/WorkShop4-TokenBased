package org.example.workshop4tokenbased.dto;

import lombok.Builder;

@Builder
public record UserDtoResponse (
        Long id,
        String login,
        String password,
        String email,
        String role
) {
}