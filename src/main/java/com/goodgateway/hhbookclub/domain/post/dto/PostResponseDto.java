package com.goodgateway.hhbookclub.domain.post.dto;

import com.goodgateway.hhbookclub.domain.post.entity.Post;

import java.time.LocalDateTime;

public record PostResponseDto(
        Long id,
        String title,
        String content,
        String contentFormat,
        Integer viewCount,
        AuthorDto author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public record AuthorDto(Long id, String nickname, String profileImage) {
        public static AuthorDto from(com.goodgateway.hhbookclub.domain.user.entity.User user) {
            return new AuthorDto(user.getId(), user.getNickname(), user.getProfileImage());
        }
    }

    public static PostResponseDto from(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getContentFormat(),
                post.getViewCount(),
                AuthorDto.from(post.getUser()),
                post.getCreatedAt(),
                post.getUpdatedAt());
    }
}
