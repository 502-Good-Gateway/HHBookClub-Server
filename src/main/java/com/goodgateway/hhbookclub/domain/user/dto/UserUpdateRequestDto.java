package com.goodgateway.hhbookclub.domain.user.dto;

import java.util.List;

public record UserUpdateRequestDto(
        String nickname,
        String profileImage,
        List<String> favoriteGenres) {
}
