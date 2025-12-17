package com.goodgateway.hhbookclub.domain.user.dto;

import com.goodgateway.hhbookclub.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private final Long id;
    private final String email;
    private final String nickname;
    private final String profileImage;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
    }
}
