package com.goodgateway.hhbookclub.domain.comment.dto;

import com.goodgateway.hhbookclub.domain.comment.entity.Comment;

import java.time.LocalDateTime;

public record CommentResponseDto(
        Long id,
        Long postId,
        String content,
        AuthorDto author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean isEdited) {
    public record AuthorDto(Long id, String nickname, String profileImage) {
        public static AuthorDto from(com.goodgateway.hhbookclub.domain.user.entity.User user) {
            return new AuthorDto(user.getId(), user.getNickname(), user.getProfileImage());
        }
    }

    public static CommentResponseDto from(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getPost().getId(),
                comment.getContent(),
                AuthorDto.from(comment.getUser()),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.isEdited());
    }
}
