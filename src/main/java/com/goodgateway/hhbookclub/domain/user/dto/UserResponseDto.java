package com.goodgateway.hhbookclub.domain.user.dto;

import com.goodgateway.hhbookclub.domain.user.entity.User;
import java.util.List;

public record UserResponseDto(
        Long id,
        String email,
        String nickname,
        String profileImage,
        List<String> favoriteGenres) {
    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImage(),
                parseFavoriteGenres(user.getFavoriteGenres()));
    }

    private static List<String> parseFavoriteGenres(String json) {
        if (json == null || json.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        String content = json.trim();
        if (content.startsWith("[") && content.endsWith("]")) {
            content = content.substring(1, content.length() - 1);
        }
        if (content.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return java.util.Arrays.stream(content.split(","))
                .map(s -> s.trim().replaceAll("^\"|\"$", ""))
                .collect(java.util.stream.Collectors.toList());
    }
}
