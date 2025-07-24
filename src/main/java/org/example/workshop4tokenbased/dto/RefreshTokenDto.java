package org.example.workshop4tokenbased.dto;

import lombok.Builder;

@Builder
public record RefreshTokenDto(
        String refreshToken
){
}