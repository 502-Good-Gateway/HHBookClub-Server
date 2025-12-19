package com.goodgateway.hhbookclub.domain.comment.controller;

import com.goodgateway.hhbookclub.domain.comment.dto.CommentListResponseDto;
import com.goodgateway.hhbookclub.domain.comment.dto.CommentRequestDto;
import com.goodgateway.hhbookclub.domain.comment.dto.CommentResponseDto;
import com.goodgateway.hhbookclub.domain.comment.service.CommentService;
import com.goodgateway.hhbookclub.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentListResponseDto>> getComments(@PathVariable Long postId) {
        return ResponseEntity.ok(ApiResponse.success(commentService.getComments(postId)));
    }

    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long postId,
            @RequestBody CommentRequestDto request) {
        CommentResponseDto response = commentService.createComment(userDetails.getUsername(), postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PatchMapping("/api/comments/{id}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> updateComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id,
            @RequestBody CommentRequestDto request) {
        return ResponseEntity
                .ok(ApiResponse.success(commentService.updateComment(userDetails.getUsername(), id, request)));
    }

    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        commentService.deleteComment(userDetails.getUsername(), id);
        return ResponseEntity.ok(ApiResponse.success("댓글이 삭제되었습니다.", null));
    }
}
