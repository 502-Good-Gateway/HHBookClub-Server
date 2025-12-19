package com.goodgateway.hhbookclub.domain.auth.controller;

import com.goodgateway.hhbookclub.global.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2CallbackController {

    @GetMapping("/login/oauth2/code/{provider}")
    public ResponseEntity<ApiResponse<String>> callback(@PathVariable String provider, @RequestParam String code) {
        return ResponseEntity.ok(ApiResponse.success("Login Successful! Your code is: " + code));
    }
}
