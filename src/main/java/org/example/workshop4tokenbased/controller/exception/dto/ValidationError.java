package org.example.workshop4tokenbased.controller.exception.dto;

import lombok.Builder;

@Builder
public record ValidationError(
        String field,
        String message
){

}