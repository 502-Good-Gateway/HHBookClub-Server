package com.goodgateway.hhbookclub.domain.post.repository;

import com.goodgateway.hhbookclub.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE " +
            "(:search IS NULL OR :search = '' OR " +
            "p.title LIKE %:search% OR p.content LIKE %:search%)")
    Page<Post> findAllWithSearch(@Param("search") String search, Pageable pageable);

    Page<Post> findByUserId(Long userId, Pageable pageable);
}
