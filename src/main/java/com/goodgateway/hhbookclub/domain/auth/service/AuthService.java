package com.goodgateway.hhbookclub.domain.auth.service;

import com.goodgateway.hhbookclub.domain.auth.dto.AuthResponseDto;
import com.goodgateway.hhbookclub.domain.user.dto.UserResponseDto;
import com.goodgateway.hhbookclub.domain.user.entity.User;
import com.goodgateway.hhbookclub.domain.user.repository.UserRepository;
import com.goodgateway.hhbookclub.global.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponseDto refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String email = jwtUtil.getEmailFromToken(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new IllegalArgumentException("Refresh token mismatch");
        }

        String newAccessToken = jwtUtil.createAccessToken(email);
        String newRefreshToken = jwtUtil.createRefreshToken(email);

        user.updateRefreshToken(newRefreshToken);

        return AuthResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .user(UserResponseDto.from(user))
                .build();
    }
}
