package com.goodgateway.hhbookclub.domain.user.controller;

import com.goodgateway.hhbookclub.domain.user.dto.UserResponseDto;
import com.goodgateway.hhbookclub.domain.user.dto.UserUpdateRequestDto;
import com.goodgateway.hhbookclub.domain.user.service.UserService;
import com.goodgateway.hhbookclub.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserByEmail(userDetails.getUsername())));
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateMyInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserUpdateRequestDto request) {
        return ResponseEntity
                .ok(ApiResponse.success(userService.updateUserByEmail(userDetails.getUsername(), request)));
    }
}
