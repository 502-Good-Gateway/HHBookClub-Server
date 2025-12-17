package com.goodgateway.hhbookclub.domain.auth.dto;

import com.goodgateway.hhbookclub.domain.user.dto.UserResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponseDto {
    private String accessToken;
    private String refreshToken;
    private UserResponseDto user;
}
