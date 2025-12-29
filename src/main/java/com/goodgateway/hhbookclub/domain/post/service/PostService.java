package com.goodgateway.hhbookclub.domain.post.service;

import com.goodgateway.hhbookclub.domain.post.dto.PostListResponseDto;
import com.goodgateway.hhbookclub.domain.post.dto.PostRequestDto;
import com.goodgateway.hhbookclub.domain.post.dto.PostResponseDto;
import com.goodgateway.hhbookclub.domain.post.entity.Post;
import com.goodgateway.hhbookclub.domain.post.repository.PostRepository;
import com.goodgateway.hhbookclub.domain.upload.entity.S3Image;
import com.goodgateway.hhbookclub.domain.upload.repository.S3ImageRepository;
import com.goodgateway.hhbookclub.domain.user.entity.User;
import com.goodgateway.hhbookclub.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3ImageRepository s3ImageRepository;

    @Transactional(readOnly = true)
    public PostListResponseDto getPosts(int page, int limit, String sort, String search) {
        Sort sortBy = switch (sort) {
            case "popular" -> Sort.by(Sort.Direction.DESC, "viewCount");
            default -> Sort.by(Sort.Direction.DESC, "createdAt"); // latest
        };

        Pageable pageable = PageRequest.of(page - 1, limit, sortBy);
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
                .contentFormat(request.contentFormat())
                .build();

        Post savedPost = postRepository.save(post);
        linkImagesToPost(savedPost, request.content());
        return PostResponseDto.from(savedPost);
    }

    @Transactional
    public PostResponseDto updatePost(String email, Long id, PostRequestDto request) {
        Post post = findPostById(id);
        validateAuthor(post, email);

        post.update(request.title(), request.content());
        linkImagesToPost(post, request.content());
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

    private void linkImagesToPost(Post post, String content) {
        if (content == null)
            return;

        // Extract S3 image URLs from markdown
        List<String> imageUrls = extractImageUrls(content);

        // Update S3Image entities to associate with this post
        for (String url : imageUrls) {
            s3ImageRepository.findByUrl(url).ifPresent(s3Image -> {
                s3Image.setPost(post);
            });
        }

        // Optional: Unlink images that are no longer in the content
        List<S3Image> linkedImages = s3ImageRepository
                .findAllByPostId(post.getId());
        for (S3Image image : linkedImages) {
            if (!imageUrls.contains(image.getUrl())) {
                image.setPost(null);
            }
        }
    }

    private List<String> extractImageUrls(String content) {
        List<String> urls = new ArrayList<>();
        // Simple regex find markdown images ![alt](url) or just URLs
        // Adjust regex based on your image URL pattern
        // Example: https://bucket.s3.region.amazonaws.com/images/uuid.png
        Pattern pattern = Pattern
                .compile("https://[\\w-]+\\.s3\\.[\\w-]+\\.amazonaws\\.com/images/[\\w.-]+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            urls.add(matcher.group());
        }
        return urls;
    }
}
