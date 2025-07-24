package org.example.workshop4tokenbased.controller.exception.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ErrorResponse(
        LocalDateTime timestamp,
        HttpStatus status,
        String message,
        String url,
        List<ValidationError> validationErrors

) {
}