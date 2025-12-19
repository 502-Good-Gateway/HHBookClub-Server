# 마블 인사이드 (범용 커뮤니티) 프로젝트 문서

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
사용자가 로그인하여 자유롭게 게시글을 작성하고, 이미지와 함께 콘텐츠를 공유하며 댓글로 소통할 수 있는 범용 커뮤니티 플랫폼

### 주요 기능
- 소셜 로그인 (Google, Kakao)
- 게시글 작성 / 수정 / 삭제
- 이미지 첨부 게시글
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

### 2.2 게시글

- 로그인 사용자만 작성 가능
- 제목, 내용 필수
- 이미지 첨부 가능 (다중 이미지)
- 게시글 목록 / 상세 / 수정 / 삭제
- 조회수 증가
- 페이지네이션 및 정렬 지원

### 2.3 댓글

- 로그인 사용자만 작성 가능
- 댓글 작성 / 수정 / 삭제
- 게시글 상세 페이지에서 조회

### 2.4 검색

- 제목 + 내용 기반 통합 검색
- 검색 결과 페이지네이션

---

## 3. API 명세서

### 인증
- Base URL: `/api`
- Authorization: Bearer Token (JWT)

### 회원 API

POST /api/auth/login/{provider}  
GET /api/users/me  

### 게시글 API

GET /api/posts  
GET /api/posts/{id}  
POST /api/posts  
PATCH /api/posts/{id}  
DELETE /api/posts/{id}  

### 이미지 업로드 API

POST /api/upload  
- multipart/form-data
- Response: 업로드된 이미지 URL

### 댓글 API

GET /api/posts/{postId}/comments  
POST /api/posts/{postId}/comments  
PATCH /api/comments/{id}  
DELETE /api/comments/{id}  

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
    content TEXT NOT NULL,
    view_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);
```

### POST_IMAGE
```sql
CREATE TABLE post_image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    sort_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE
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
- 로그인 기반 커뮤니티의 핵심 기능 충실
- 확장 가능한 이미지 분리 테이블 설계
- REST API 구조 명확

### 개선 포인트
- 검색 성능 최적화 (Full-Text Index)
- 관리자 기능 및 신고 시스템 확장 가능
- CDN 기반 이미지 제공 고려

---

**문서 버전:** 1.0.0  
**작성일:** 2025-12-19
