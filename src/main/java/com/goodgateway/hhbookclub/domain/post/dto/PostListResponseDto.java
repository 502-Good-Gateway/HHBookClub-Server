package com.goodgateway.hhbookclub.domain.post.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PostListResponseDto(
        List<PostSummaryDto> posts,
        long totalCount,
        int currentPage,
        int totalPages) {
    public record PostSummaryDto(
            Long id,
            String title,
            Integer viewCount,
            AuthorSummaryDto author,
            java.time.LocalDateTime createdAt) {
        public record AuthorSummaryDto(Long id, String nickname) {
        }
    }

    public static PostListResponseDto from(Page<com.goodgateway.hhbookclub.domain.post.entity.Post> page) {
        List<PostSummaryDto> posts = page.getContent().stream()
                .map(post -> new PostSummaryDto(
                        post.getId(),
                        post.getTitle(),
                        post.getViewCount(),
                        new PostSummaryDto.AuthorSummaryDto(post.getUser().getId(), post.getUser().getNickname()),
                        post.getCreatedAt()))
                .toList();

        return new PostListResponseDto(
                posts,
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getTotalPages());
    }
}
