package com.goodgateway.hhbookclub.domain.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuth2CallbackController {

    @GetMapping("/login/oauth2/code/{provider}")
    public String callback(@PathVariable String provider, @RequestParam String code) {
        return "Login Successful! Your code is: " + code;
    }
}
