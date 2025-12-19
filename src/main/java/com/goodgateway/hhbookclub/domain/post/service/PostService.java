package com.goodgateway.hhbookclub.domain.post.service;

import com.goodgateway.hhbookclub.domain.post.dto.PostListResponseDto;
import com.goodgateway.hhbookclub.domain.post.dto.PostRequestDto;
import com.goodgateway.hhbookclub.domain.post.dto.PostResponseDto;
import com.goodgateway.hhbookclub.domain.post.entity.Post;
import com.goodgateway.hhbookclub.domain.post.repository.PostRepository;
import com.goodgateway.hhbookclub.domain.user.entity.User;
import com.goodgateway.hhbookclub.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public PostListResponseDto getPosts(int page, int limit, String sort, String search) {
        Sort sortBy = switch (sort) {
            case "popular" -> Sort.by(Sort.Direction.DESC, "viewCount");
            case "rating" -> Sort.by(Sort.Direction.DESC, "rating");
            default -> Sort.by(Sort.Direction.DESC, "createdAt"); // latest
        };

        Pageable pageable = PageRequest.of(page - 1, limit, sortBy); // page is 1-indexed from client
        Page<Post> postPage = postRepository.findAllWithSearch(search, pageable);

        return PostListResponseDto.from(postPage);
    }

    @Transactional
    public PostResponseDto getPost(Long id) {
        Post post = findPostById(id);
        post.incrementViewCount();
        return PostResponseDto.from(post);
    }

    @Transactional
    public PostResponseDto createPost(String email, PostRequestDto request) {
        User user = findUserByEmail(email);

        Post post = Post.builder()
                .user(user)
                .title(request.title())
                .content(request.content())
                .bookTitle(request.bookTitle())
                .bookAuthor(request.bookAuthor())
                .bookCover(request.bookCover())
                .bookPublisher(request.bookPublisher())
                .rating(request.rating())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .build();

        Post savedPost = postRepository.save(post);
        return PostResponseDto.from(savedPost);
    }

    @Transactional
    public PostResponseDto updatePost(String email, Long id, PostRequestDto request) {
        Post post = findPostById(id);
        validateAuthor(post, email);

        post.update(request.title(), request.content(), request.rating(), request.endDate());
        return PostResponseDto.from(post);
    }

    @Transactional
    public void deletePost(String email, Long id) {
        Post post = findPostById(id);
        validateAuthor(post, email);
        post.delete();
    }

    private Post findPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + id));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. Email: " + email));
    }

    private void validateAuthor(Post post, String email) {
        if (!post.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("게시글 수정/삭제 권한이 없습니다.");
        }
    }
}
