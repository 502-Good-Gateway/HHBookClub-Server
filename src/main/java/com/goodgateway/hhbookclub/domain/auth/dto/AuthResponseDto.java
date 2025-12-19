package com.goodgateway.hhbookclub.domain.auth.dto;

import com.goodgateway.hhbookclub.domain.user.dto.UserResponseDto;
import lombok.Builder;

@Builder
public record AuthResponseDto(
        String accessToken,
        String refreshToken,
        UserResponseDto user) {
}
