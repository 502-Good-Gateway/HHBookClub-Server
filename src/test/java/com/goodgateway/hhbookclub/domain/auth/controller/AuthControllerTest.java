package com.goodgateway.hhbookclub.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodgateway.hhbookclub.domain.auth.dto.AuthResponseDto;
import com.goodgateway.hhbookclub.domain.auth.dto.LoginRequestDto;
import com.goodgateway.hhbookclub.domain.auth.service.AuthService;
import com.goodgateway.hhbookclub.domain.user.dto.UserResponseDto;
import com.goodgateway.hhbookclub.global.security.jwt.JwtFilter;
import com.goodgateway.hhbookclub.global.security.jwt.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for unit test
@MockBean(JpaMetamodelMappingContext.class)
class AuthControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private AuthService authService;

        @MockBean
        private JwtFilter jwtFilter;

        @MockBean
        private JwtUtil jwtUtil;

        @Test
        @DisplayName("Login Success")
        void login_success() throws Exception {
                // given
                String provider = "google";
                String code = "auth_code";
                LoginRequestDto request = new LoginRequestDto(code);

                UserResponseDto userDto = new UserResponseDto(1L, "test@email.com", "nickname", "profile",
                                Collections.emptyList());
                AuthResponseDto responseDto = new AuthResponseDto("access", "refresh", userDto);

                given(authService.login(anyString(), anyString())).willReturn(responseDto);

                // when & then
                mockMvc.perform(post("/api/auth/login/{provider}", provider)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("success"))
                                .andExpect(jsonPath("$.data.accessToken").value("access"))
                                .andExpect(jsonPath("$.data.user.email").value("test@email.com"));
        }

        @Test
        @DisplayName("Login Fail - Missing Code")
        void login_fail_missing_code() throws Exception {
                // given
                String provider = "google";
                LoginRequestDto request = new LoginRequestDto(null);

                // when & then
                mockMvc.perform(post("/api/auth/login/{provider}", provider)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andDo(print())
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value("error"))
                                .andExpect(jsonPath("$.message").value("Authorization code is missing"));
        }

        @Test
        @DisplayName("Logout Success")
        void logout_success() throws Exception {
                // when & then
                mockMvc.perform(post("/api/auth/logout"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("success"))
                                .andExpect(jsonPath("$.message").value("로그아웃되었습니다."));
        }
}
