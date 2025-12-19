package com.goodgateway.hhbookclub.domain.comment.service;

import com.goodgateway.hhbookclub.domain.comment.dto.CommentListResponseDto;
import com.goodgateway.hhbookclub.domain.comment.dto.CommentRequestDto;
import com.goodgateway.hhbookclub.domain.comment.dto.CommentResponseDto;
import com.goodgateway.hhbookclub.domain.comment.entity.Comment;
import com.goodgateway.hhbookclub.domain.comment.repository.CommentRepository;
import com.goodgateway.hhbookclub.domain.post.entity.Post;
import com.goodgateway.hhbookclub.domain.post.repository.PostRepository;
import com.goodgateway.hhbookclub.domain.user.entity.User;
import com.goodgateway.hhbookclub.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public CommentListResponseDto getComments(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
        return CommentListResponseDto.from(comments);
    }

    @Transactional
    public CommentResponseDto createComment(String email, Long postId, CommentRequestDto request) {
        User user = findUserByEmail(email);
        Post post = findPostById(postId);

        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(request.content())
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentResponseDto.from(savedComment);
    }

    @Transactional
    public CommentResponseDto updateComment(String email, Long id, CommentRequestDto request) {
        Comment comment = findCommentById(id);
        validateAuthor(comment, email);
        comment.update(request.content());
        return CommentResponseDto.from(comment);
    }

    @Transactional
    public void deleteComment(String email, Long id) {
        Comment comment = findCommentById(id);
        validateAuthor(comment, email);
        comment.delete();
    }

    private Comment findCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. ID: " + id));
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. Email: " + email));
    }

    private void validateAuthor(Comment comment, String email) {
        if (!comment.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("댓글 수정/삭제 권한이 없습니다.");
        }
    }
}
