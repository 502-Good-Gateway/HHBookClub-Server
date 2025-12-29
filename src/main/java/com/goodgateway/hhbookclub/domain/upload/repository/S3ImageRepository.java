package com.goodgateway.hhbookclub.domain.upload.repository;

import com.goodgateway.hhbookclub.domain.upload.entity.S3Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface S3ImageRepository extends JpaRepository<S3Image, Long> {
    Optional<S3Image> findByUrl(String url);

    List<S3Image> findAllByPostId(Long postId);

    List<S3Image> findAllByPostIsNullAndCreatedAtBefore(LocalDateTime dateTime);
}
