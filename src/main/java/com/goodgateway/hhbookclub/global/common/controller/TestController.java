package com.goodgateway.hhbookclub.global.common.controller;

import com.goodgateway.hhbookclub.global.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    /**
     * Simple endpoint to check if the user is authenticated.
     * Requires a valid JWT access token.
     */
    @GetMapping("/check")
    public ResponseEntity<ApiResponse<String>> checkAuthentication() {
        return ResponseEntity.ok(ApiResponse.success("Ok"));
    }
}
