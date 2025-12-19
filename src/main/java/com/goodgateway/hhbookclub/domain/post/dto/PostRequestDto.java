package com.goodgateway.hhbookclub.domain.post.dto;

public record PostRequestDto(
        String title,
        String content,
        String contentFormat // "MD" by default
) {
}
