package com.goodgateway.hhbookclub.domain.post.entity;

import com.goodgateway.hhbookclub.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@SQLRestriction("deleted_at IS NULL")
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "book_title", nullable = false, length = 200)
    private String bookTitle;

    @Column(name = "book_author", nullable = false, length = 100)
    private String bookAuthor;

    @Column(name = "book_cover", length = 500)
    private String bookCover;

    @Column(name = "book_publisher", length = 100)
    private String bookPublisher;

    @Column(nullable = false)
    private Integer rating;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public Post(User user, String title, String content, String bookTitle, String bookAuthor,
            String bookCover, String bookPublisher, Integer rating, LocalDate startDate, LocalDate endDate) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookCover = bookCover;
        this.bookPublisher = bookPublisher;
        this.rating = rating;
        this.startDate = startDate;
        this.endDate = endDate;
        this.viewCount = 0;
    }

    public void update(String title, String content, Integer rating, LocalDate endDate) {
        if (title != null)
            this.title = title;
        if (content != null)
            this.content = content;
        if (rating != null)
            this.rating = rating;
        if (endDate != null)
            this.endDate = endDate;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void incrementViewCount() {
        this.viewCount++;
    }
}
