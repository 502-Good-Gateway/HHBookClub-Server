package com.goodgateway.hhbookclub.domain.comment.dto;

import java.util.List;

public record CommentListResponseDto(
        List<CommentResponseDto> comments,
        int totalCount) {
    public static CommentListResponseDto from(List<com.goodgateway.hhbookclub.domain.comment.entity.Comment> comments) {
        return new CommentListResponseDto(
                comments.stream().map(CommentResponseDto::from).toList(),
                comments.size());
    }
}
