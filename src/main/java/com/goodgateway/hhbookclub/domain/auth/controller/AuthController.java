package com.goodgateway.hhbookclub.domain.auth.controller;

import com.goodgateway.hhbookclub.domain.auth.dto.AuthResponseDto;
import com.goodgateway.hhbookclub.domain.auth.dto.LoginRequestDto;
import com.goodgateway.hhbookclub.domain.auth.service.AuthService;
import com.goodgateway.hhbookclub.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        // Since we are stateless (JWT), we just return success.
        // Client should discard the token.
        return ResponseEntity.ok(ApiResponse.success("로그아웃되었습니다.", null));
    }

    @PostMapping("/login/{provider}")
    public ResponseEntity<ApiResponse<AuthResponseDto>> login(
            @PathVariable String provider,
            @RequestBody LoginRequestDto request) {
        if (request.code() == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Authorization code is missing"));
        }

        AuthResponseDto result = authService.login(provider, request.code());
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
