package com.goodgateway.hhbookclub.domain.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.goodgateway.hhbookclub.domain.auth.dto.AuthResponseDto;
import com.goodgateway.hhbookclub.domain.user.dto.UserResponseDto;
import com.goodgateway.hhbookclub.domain.user.entity.User;
import com.goodgateway.hhbookclub.domain.user.repository.UserRepository;
import com.goodgateway.hhbookclub.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final GoogleAuthService googleAuthService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponseDto login(String provider, String code) {
        if (!"google".equalsIgnoreCase(provider)) {
            throw new IllegalArgumentException("Unsupported provider: " + provider);
        }

        String googleAccessToken = googleAuthService.getAccessToken(code);
        JsonNode userInfo = googleAuthService.getUserInfo(googleAccessToken);

        String email = userInfo.get("email").asText();
        String providerId = userInfo.get("id").asText();
        String nickname = userInfo.has("name") ? userInfo.get("name").asText()
                : "User_" + UUID.randomUUID().toString().substring(0, 8);
        String profileImage = userInfo.has("picture") ? userInfo.get("picture").asText() : null;

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(email)
                        .nickname(nickname)
                        .profileImage(profileImage)
                        .provider(provider)
                        .providerId(providerId)
                        .build()));

        String accessToken = jwtUtil.createAccessToken(email);
        String refreshToken = jwtUtil.createRefreshToken(email);

        user.updateRefreshToken(refreshToken);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(new UserResponseDto(user))
                .build();
    }
}
