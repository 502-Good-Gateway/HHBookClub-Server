package com.goodgateway.hhbookclub.domain.user.service;

import com.goodgateway.hhbookclub.domain.user.dto.UserResponseDto;
import com.goodgateway.hhbookclub.domain.user.dto.UserUpdateRequestDto;
import com.goodgateway.hhbookclub.domain.user.entity.User;
import com.goodgateway.hhbookclub.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponseDto getUser(Long userId) {
        User user = findUserById(userId);
        return UserResponseDto.from(user);
    }

    @Transactional
    public UserResponseDto updateUser(Long userId, UserUpdateRequestDto request) {
        User user = findUserById(userId);

        if (request.nickname() != null) {
            user.updateProfile(request.nickname(), user.getProfileImage());
        }
        if (request.profileImage() != null) {
            user.updateProfile(user.getNickname(), request.profileImage());
        }
        if (request.favoriteGenres() != null) {
            user.updateFavoriteGenres(convertListToJson(request.favoriteGenres()));
        }

        return UserResponseDto.from(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserByEmail(String email) {
        User user = findUserByEmail(email);
        return UserResponseDto.from(user);
    }

    @Transactional
    public UserResponseDto updateUserByEmail(String email, UserUpdateRequestDto request) {
        User user = findUserByEmail(email);

        if (request.nickname() != null) {
            String newProfileImage = request.profileImage() != null ? request.profileImage()
                    : user.getProfileImage();
            user.updateProfile(request.nickname(), newProfileImage);
        } else if (request.profileImage() != null) {
            user.updateProfile(user.getNickname(), request.profileImage());
        }

        if (request.favoriteGenres() != null) {
            user.updateFavoriteGenres(convertListToJson(request.favoriteGenres()));
        }

        return UserResponseDto.from(user);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    private String convertListToJson(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        // Simple JSON serialization: ["a", "b"]
        String joined = list.stream()
                .map(s -> "\"" + s + "\"")
                .collect(Collectors.joining(","));
        return "[" + joined + "]";
    }
}
