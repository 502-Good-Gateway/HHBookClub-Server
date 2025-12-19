# 마블 인사이드 (범용 커뮤니티 · 블로그형) 프로젝트 문서

## 📋 목차
1. 프로젝트 개요
2. 요구사항 명세서
3. API 명세서
4. 데이터베이스 스키마 (ERD)
5. 프로젝트 평가

---

## 1. 프로젝트 개요

### 프로젝트명
**마블 인사이드 (Marvel Inside)**

### 목적
사용자가 로그인하여 **마크다운 기반으로 글을 작성**하고,  
글의 흐름 중 **원하는 위치에 이미지를 삽입**하여  
티스토리와 같은 블로그 형태의 게시글을 작성·공유할 수 있는 커뮤니티 플랫폼

### 주요 기능
- 소셜 로그인 (Google)
- 마크다운 기반 게시글 작성 / 수정 / 삭제
- 글 본문 내 이미지 자유 삽입
- 댓글 기능
- 게시글 검색

---

## 2. 요구사항 명세서

### 2.1 회원 관리
- OAuth 2.0 기반 소셜 로그인
- JWT 기반 인증 (Access / Refresh Token)
- 프로필 관리 (닉네임, 프로필 이미지)
- 마이페이지 (내 게시글 / 댓글 조회)
- 회원 탈퇴 (Soft Delete)

### 2.2 게시글 (블로그형)

- 로그인 사용자만 작성 가능
- 제목, 본문 필수
- **본문은 Markdown 형식으로 작성**
- 글 작성 중 이미지 업로드 가능
- 이미지 업로드 후 반환된 URL을 **본문의 원하는 위치에 삽입**
- 게시글 목록 / 상세 / 수정 / 삭제
- 조회수 증가
- 페이지네이션 및 정렬 지원

#### 게시글 작성 예시 (Markdown)
```md
Redis 동시성 문제를 해결한 경험을 정리한다.

![](https://cdn.marble.com/images/redis.png)

이 문제는 Redis의 원자 연산을 활용해 해결했다.
```

### 2.3 댓글
- 로그인 사용자만 작성 가능
- 댓글 작성 / 수정 / 삭제
- 게시글 상세 페이지에서 조회

### 2.4 검색
- 제목 + 본문(Markdown 원문) 기반 통합 검색
- 검색 결과 페이지네이션

---

## 3. API 명세서

### 공통
- Base URL: `/api`
- Authorization: Bearer Token (JWT)

### 회원 API
```
POST   /api/auth/login/{provider}
GET    /api/users/me
```

### 게시글 API
```
GET    /api/posts
GET    /api/posts/{id}
POST   /api/posts
PATCH  /api/posts/{id}
DELETE /api/posts/{id}
```

#### 게시글 등록 요청 예시
```json
{
  "title": "Redis 동시성 문제 해결",
  "content": "Redis 동시성 문제를 정리한다.\n\n![](https://cdn.xxx/img.png)",
  "contentFormat": "MD"
}
```

### 이미지 업로드 API
```
POST /api/upload
```
- multipart/form-data
- Response: 업로드된 이미지 URL
```json
{
  "imageUrl": "https://cdn.marble.com/images/abc.png"
}
```

### 댓글 API
```
GET    /api/posts/{postId}/comments
POST   /api/posts/{postId}/comments
PATCH  /api/comments/{id}
DELETE /api/comments/{id}
```

---

## 4. 데이터베이스 스키마 (ERD)

### USER
```sql
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    nickname VARCHAR(50) NOT NULL,
    profile_image VARCHAR(500),
    provider VARCHAR(20) NOT NULL,
    provider_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);
```

### POST
```sql
CREATE TABLE post (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content LONGTEXT NOT NULL,        -- Markdown 원문 저장
    content_format VARCHAR(10) NOT NULL DEFAULT 'MD',
    view_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);
```

### COMMENT
```sql
CREATE TABLE comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);
```

---

## 5. 프로젝트 평가

### 강점
- Markdown 기반 블로그형 게시글 구조
- 이미지 위치 자유도가 높은 콘텐츠 설계
- 단순하고 확장 가능한 DB 구조
- REST API 구조 명확

### 개선 포인트
- 미사용 이미지 정리를 위한 배치 처리
- Markdown 렌더링 시 XSS 방지 처리
- Full-Text Index를 활용한 검색 성능 개선
- CDN 기반 이미지 제공

---

**문서 버전:** 1.1.0  
**작성일:** 2025-12-19
