package com.goodgateway.hhbookclub.domain.auth.controller;

import com.goodgateway.hhbookclub.domain.auth.dto.AuthResponseDto;
import com.goodgateway.hhbookclub.domain.auth.dto.LoginRequestDto;
import com.goodgateway.hhbookclub.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login/{provider}")
    public ResponseEntity<AuthResponseDto> login(
            @PathVariable String provider,
            @RequestBody LoginRequestDto request) {
        if (request.getCode() == null) {
            return ResponseEntity.badRequest().build();
        }

        AuthResponseDto result = authService.login(provider, request.getCode());
        return ResponseEntity.ok(result);
    }
}
