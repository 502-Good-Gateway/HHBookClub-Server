package com.goodgateway.hhbookclub.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodgateway.hhbookclub.domain.user.dto.UserResponseDto;
import com.goodgateway.hhbookclub.domain.user.dto.UserUpdateRequestDto;
import com.goodgateway.hhbookclub.domain.user.service.UserService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private UserService userService;

        @MockBean
        private JwtFilter jwtFilter;

        @MockBean
        private JwtUtil jwtUtil;

        @Test
        @DisplayName("Get My Info Success")
        @WithMockUser(username = "test@email.com")
        void getMyInfo_success() throws Exception {
                // given
                UserResponseDto userDto = new UserResponseDto(1L, "test@email.com", "nickname", "profile",
                                Collections.emptyList());
                given(userService.getUserByEmail("test@email.com")).willReturn(userDto);

                // when & then
                mockMvc.perform(get("/api/users/me"))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("success"))
                                .andExpect(jsonPath("$.data.email").value("test@email.com"));
        }

        @Test
        @DisplayName("Update My Info Success")
        @WithMockUser(username = "test@email.com")
        void updateMyInfo_success() throws Exception {
                // given
                UserUpdateRequestDto request = new UserUpdateRequestDto("newNick", null, List.of("Horror"));
                UserResponseDto responseDto = new UserResponseDto(1L, "test@email.com", "newNick", "profile",
                                List.of("Horror"));

                given(userService.updateUserByEmail(eq("test@email.com"), any(UserUpdateRequestDto.class)))
                                .willReturn(responseDto);

                // when & then
                mockMvc.perform(patch("/api/users/me")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.status").value("success"))
                                .andExpect(jsonPath("$.data.nickname").value("newNick"))
                                .andExpect(jsonPath("$.data.favoriteGenres[0]").value("Horror"));
        }
}
