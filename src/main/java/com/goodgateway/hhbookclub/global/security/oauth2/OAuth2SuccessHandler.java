package com.goodgateway.hhbookclub.global.security.oauth2;

import com.goodgateway.hhbookclub.domain.user.entity.User;
import com.goodgateway.hhbookclub.domain.user.repository.UserRepository;
import com.goodgateway.hhbookclub.global.security.jwt.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        OAuth2UserPrincipal principal = (OAuth2UserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        String accessToken = jwtUtil.createAccessToken(user.getEmail());
        String refreshToken = jwtUtil.createRefreshToken(user.getEmail());

        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        // 프론트엔드가 없는 경우 서버 자체 콜백 페이지로 리다이렉트
        // 프론트엔드 구현 후에는 frontendUrl + "/oauth/callback"으로 변경
        String redirectUrl = UriComponentsBuilder.fromUriString("/oauth/callback")
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString();

        response.sendRedirect(redirectUrl);
    }
}
