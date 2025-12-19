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

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content; // Markdown raw content

    @Column(name = "content_format", nullable = false, length = 10)
    private String contentFormat = "MD";

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
    public Post(User user, String title, String content, String contentFormat) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.contentFormat = contentFormat != null ? contentFormat : "MD";
        this.viewCount = 0;
    }

    public void update(String title, String content) {
        if (title != null)
            this.title = title;
        if (content != null)
            this.content = content;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void incrementViewCount() {
        this.viewCount++;
    }
}
