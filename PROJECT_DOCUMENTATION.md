# 마블 인사이드 (독서회 커뮤니티) 프로젝트 문서

## 📋 목차

1. [프로젝트 개요](#프로젝트-개요)
2. [요구사항 명세서](#요구사항-명세서)
3. [API 명세서](#api-명세서)
4. [데이터베이스 스키마 (ERD)](#데이터베이스-스키마-erd)
5. [프로젝트 평가](#프로젝트-평가)

---

## 프로젝트 개요

### 프로젝트명
**마블 인사이드 - 독서회 커뮤니티**

### 목적
독서를 좋아하는 사람들이 모여 책에 대한 생각과 감상을 공유하는 온라인 커뮤니티 플랫폼

### 주요 기능
- 소셜 로그인 (Google, Kakao)
- 독서록 작성 및 공유
- 별점 평가 시스템
- 댓글을 통한 소통
- 게시글 검색 기능

---

## 요구사항 명세서

### 1. 👤 회원 관리 (5개)

#### 1.1 소셜 로그인 🔴 높음
- OAuth 2.0을 통한 소셜 로그인 (Google, Kakao)
- 로그인 시 JWT 토큰 발급
- 신규 사용자는 자동으로 회원가입

#### 1.2 회원 프로필 설정 🟡 중간
- 닉네임, 프로필 사진, 관심 장르 설정 가능
- 프로필 정보는 언제든지 수정 가능

#### 1.3 로그아웃 🔴 높음
- 로그인된 사용자의 세션 종료
- 토큰 무효화 처리

#### 1.4 회원 탈퇴 🟢 낮음
- 회원 탈퇴 시 확인 절차 필요
- 탈퇴 후에도 작성한 게시글과 댓글은 보존 (작성자는 "탈퇴한 회원"으로 표시)

#### 1.5 마이페이지 🟡 중간
- 내가 작성한 게시글 목록
- 내가 작성한 댓글 목록
- 프로필 정보 수정
- 회원 탈퇴

---

### 2. ✍️ 독서록/게시글 (7개)

#### 2.1 게시글 작성 🔴 높음
- 로그인한 사용자만 작성 가능
- 입력 항목:
  - 제목
  - 책 제목
  - 책 저자
  - 내용 (에디터)
  - 독서 시작일
  - 독서 완료일
  - 평점 (별점 5점 만점)

#### 2.2 게시글 목록 조회 🔴 높음
- 카드형 레이아웃으로 표시
- 표시 정보: 책 표지, 책 제목, 저자, 평점, 작성일, 작성자
- 정렬 옵션: 최신순, 인기순, 평점순
- 페이지네이션 적용

#### 2.3 게시글 상세 조회 🔴 높음
- 전체 독서록 내용 표시
- 책 정보 표시
- 작성자 정보 표시
- 댓글 목록 표시

#### 2.4 게시글 수정 🟡 중간
- 작성자 본인만 수정 가능
- 수정 가능 항목: 제목, 내용, 평점, 독서 완료일

#### 2.5 게시글 삭제 🟡 중간
- 작성자 본인만 삭제 가능
- 삭제 시 확인 메시지 표시
- 삭제된 게시글은 복구 불가

#### 2.6 반응형 디자인 🟡 중간
- 모바일, 태블릿, 데스크톱 대응
- 모바일에서는 햄버거 메뉴 사용

---

### 3. 💬 댓글 (4개)

#### 3.1 댓글 작성 🔴 높음
- 로그인한 사용자만 작성 가능
- 최대 500자 제한

#### 3.2 댓글 조회 🔴 높음
- 작성 시간순으로 정렬
- 작성자 정보 표시 (닉네임, 프로필 사진)

#### 3.3 댓글 수정 🟡 중간
- 작성자 본인만 수정 가능
- 수정된 댓글은 "수정됨" 표시

#### 3.4 댓글 삭제 🟡 중간
- 작성자 본인만 삭제 가능
- 삭제된 댓글은 "삭제된 댓글입니다" 표시

---

### 4. 🔍 검색 (1개)

#### 4.1 게시글 검색 🟡 중간
- 검색 범위: 책 제목, 저자, 게시글 제목, 내용
- 검색 결과는 관련도순으로 정렬

---

### 5. 📖 도서 정보 (1개)

#### 5.1 책 정보 자동 완성 🟢 낮음
- 외부 API(예: 알라딘 API)를 통한 책 정보 자동 입력
- 책 표지, 저자, 출판사 자동 입력

---

## API 명세서

### 기본 정보
- Base URL: `/api`
- 인증 방식: Bearer Token (JWT)
- Content-Type: `application/json`

---

### 1. 👤 회원 관리 API

#### 1.1 소셜 로그인
```
POST /api/auth/login/{provider}
```

**Path Parameter:**
- `provider`: string (google, kakao)

**Request Body:**
```json
{
  "code": "OAuth authorization code",
  "redirectUri": "리다이렉트 URI"
}
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
  "user": {
    "id": 1,
    "email": "user@example.com",
    "nickname": "홍길동",
    "profileImage": "https://...",
    "favoriteGenres": ["소설", "에세이"],
    "isNewUser": false,
    "createdAt": "2024-12-01T00:00:00Z"
  }
}
```

**Status Codes:**
- 200: 로그인 성공
- 400: 잘못된 요청
- 401: 인증 실패

---

#### 1.2 로그아웃
```
POST /api/auth/logout
```

**Headers:**
- `Authorization: Bearer {accessToken}`

**Response (200 OK):**
```json
{
  "message": "로그아웃되었습니다."
}
```

---

#### 1.3 내 정보 조회
```
GET /api/users/me
```

**Headers:**
- `Authorization: Bearer {accessToken}`

**Response (200 OK):**
```json
{
  "id": 1,
  "email": "user@example.com",
  "nickname": "홍길동",
  "profileImage": "https://...",
  "favoriteGenres": ["소설", "에세이"],
  "createdAt": "2024-01-01T00:00:00Z"
}
```

**Status Codes:**
- 200: 조회 성공
- 401: 인증 필요

---

### 2. ✍️ 게시글 API

#### 2.1 게시글 목록 조회
```
GET /api/posts
```

**Query Parameters:**
- `page`: number (default: 1)
- `limit`: number (default: 20)
- `sort`: string (latest, popular, rating) (default: latest)
- `search`: string (optional)

**Response (200 OK):**
```json
{
  "posts": [
    {
      "id": 1,
      "title": "이책 정말 좋아요!",
      "bookTitle": "하여가",
      "bookAuthor": "김하나",
      "bookCover": "https://...",
      "rating": 5,
      "author": {
        "id": 1,
        "nickname": "홍길동"
      },
      "createdAt": "2024-12-01T00:00:00Z"
    }
  ],
  "totalCount": 100,
  "currentPage": 1,
  "totalPages": 5
}
```

---

#### 2.2 게시글 상세 조회
```
GET /api/posts/{id}
```

**Path Parameter:**
- `id`: number (게시글 ID)

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "이책 정말 좋아요!",
  "content": "상세 내용...",
  "bookTitle": "하여가",
  "bookAuthor": "김하나",
  "bookCover": "https://...",
  "rating": 5,
  "startDate": "2024-11-01",
  "endDate": "2024-11-15",
  "author": {
    "id": 1,
    "nickname": "홍길동",
    "profileImage": "https://..."
  },
  "viewCount": 123,
  "createdAt": "2024-12-01T00:00:00Z",
  "updatedAt": "2024-12-01T00:00:00Z"
}
```

**Status Codes:**
- 200: 조회 성공
- 404: 게시글 없음

---

#### 2.3 게시글 작성
```
POST /api/posts
```

**Headers:**
- `Authorization: Bearer {accessToken}`

**Request Body:**
```json
{
  "title": "게시글 제목",
  "content": "게시글 내용",
  "bookTitle": "책 제목",
  "bookAuthor": "책 저자",
  "bookCover": "https://...",
  "rating": 5,
  "startDate": "2024-11-01",
  "endDate": "2024-11-15"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "title": "이책 정말 좋아요!",
  "content": "상세 내용...",
  "bookTitle": "하여가",
  "bookAuthor": "김하나",
  "bookCover": "https://...",
  "rating": 5,
  "startDate": "2024-11-01",
  "endDate": "2024-11-15",
  "author": {
    "id": 1,
    "nickname": "홍길동",
    "profileImage": "https://..."
  },
  "viewCount": 0,
  "createdAt": "2024-12-01T00:00:00Z",
  "updatedAt": "2024-12-01T00:00:00Z"
}
```

**Status Codes:**
- 201: 생성 성공
- 400: 잘못된 요청
- 401: 인증 필요

---

#### 2.4 게시글 수정
```
PATCH /api/posts/{id}
```

**Headers:**
- `Authorization: Bearer {accessToken}`

**Path Parameter:**
- `id`: number

**Request Body:**
```json
{
  "title": "수정된 제목",
  "content": "수정된 내용",
  "rating": 4,
  "endDate": "2024-11-20"
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "수정된 제목",
  "content": "수정된 내용...",
  "bookTitle": "하여가",
  "bookAuthor": "김하나",
  "bookCover": "https://...",
  "rating": 4,
  "startDate": "2024-11-01",
  "endDate": "2024-11-20",
  "author": {
    "id": 1,
    "nickname": "홍길동",
    "profileImage": "https://..."
  },
  "viewCount": 123,
  "createdAt": "2024-12-01T00:00:00Z",
  "updatedAt": "2024-12-02T00:00:00Z"
}
```

**Status Codes:**
- 200: 수정 성공
- 401: 인증 필요
- 404: 게시글 없음

---

#### 2.5 게시글 삭제
```
DELETE /api/posts/{id}
```

**Headers:**
- `Authorization: Bearer {accessToken}`

**Path Parameter:**
- `id`: number

**Response (200 OK):**
```json
{
  "message": "게시글이 삭제되었습니다."
}
```

**Status Codes:**
- 200: 삭제 성공
- 401: 인증 필요
- 404: 게시글 없음

---

### 3. 💬 댓글 API

#### 3.1 댓글 목록 조회
```
GET /api/posts/{postId}/comments
```

**Path Parameter:**
- `postId`: number

**Response (200 OK):**
```json
{
  "comments": [
    {
      "id": 1,
      "content": "저도 이 책 읽었어요!",
      "author": {
        "id": 2,
        "nickname": "김철수",
        "profileImage": "https://..."
      },
      "createdAt": "2024-12-02T00:00:00Z",
      "updatedAt": "2024-12-02T00:00:00Z",
      "isEdited": false
    }
  ],
  "totalCount": 10
}
```

**Status Codes:**
- 200: 조회 성공
- 404: 게시글 없음

---

#### 3.2 댓글 작성
```
POST /api/posts/{postId}/comments
```

**Headers:**
- `Authorization: Bearer {accessToken}`

**Path Parameter:**
- `postId`: number

**Request Body:**
```json
{
  "content": "댓글 내용 (최대 500자)"
}
```

**Response (201 Created):**
```json
{
  "id": 1,
  "postId": 1,
  "content": "저도 이 책 읽었어요!",
  "author": {
    "id": 2,
    "nickname": "김철수",
    "profileImage": "https://..."
  },
  "createdAt": "2024-12-02T00:00:00Z",
  "updatedAt": "2024-12-02T00:00:00Z",
  "isEdited": false
}
```

**Status Codes:**
- 201: 생성 성공
- 400: 잘못된 요청
- 401: 인증 필요

---

#### 3.3 댓글 수정
```
PATCH /api/comments/{id}
```

**Headers:**
- `Authorization: Bearer {accessToken}`

**Path Parameter:**
- `id`: number

**Request Body:**
```json
{
  "content": "수정된 내용입니다."
}
```

**Response (200 OK):**
```json
{
  "id": 1,
  "postId": 1,
  "content": "수정된 내용입니다.",
  "author": {
    "id": 2,
    "nickname": "김철수",
    "profileImage": "https://..."
  },
  "createdAt": "2024-12-02T00:00:00Z",
  "updatedAt": "2024-12-02T01:00:00Z",
  "isEdited": true
}
```

**Status Codes:**
- 200: 수정 성공
- 401: 인증 필요
- 404: 댓글 없음

---

#### 3.4 댓글 삭제
```
DELETE /api/comments/{id}
```

**Headers:**
- `Authorization: Bearer {accessToken}`

**Path Parameter:**
- `id`: number

**Response (200 OK):**
```json
{
  "message": "댓글이 삭제되었습니다."
}
```

**Status Codes:**
- 200: 삭제 성공
- 401: 인증 필요
- 404: 댓글 없음

---

### 4. 🔍 검색 API

#### 4.1 게시글 검색
```
GET /api/search
```

**Query Parameters:**
- `q`: string (required, 검색어)
- `page`: number (default: 1)
- `limit`: number (default: 20)

**Response (200 OK):**
```json
{
  "results": [
    {
      "id": 1,
      "title": "이책 정말 좋아요!",
      "bookTitle": "하여가",
      "bookAuthor": "김하나",
      "bookCover": "https://...",
      "rating": 5,
      "author": {
        "nickname": "홍길동"
      },
      "createdAt": "2024-12-01T00:00:00Z"
    }
  ],
  "totalCount": 15,
  "query": "하여가"
}
```

**Status Codes:**
- 200: 검색 성공
- 400: 검색어 누락

---

## 데이터베이스 스키마 (ERD)

### 테이블 구조

#### 1. USER (사용자)
```sql
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 ID',
    email VARCHAR(255) NOT NULL UNIQUE COMMENT '이메일',
    nickname VARCHAR(50) NOT NULL COMMENT '닉네임',
    profile_image VARCHAR(500) COMMENT '프로필 이미지 URL',
    provider VARCHAR(20) NOT NULL COMMENT 'OAuth 제공자 (google, kakao)',
    provider_id VARCHAR(255) NOT NULL COMMENT 'OAuth 제공자 ID',
    favorite_genres JSON COMMENT '관심 장르 (JSON 배열)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '가입일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    deleted_at TIMESTAMP NULL COMMENT '탈퇴일시 (soft delete)',
    
    INDEX idx_email (email),
    INDEX idx_provider (provider, provider_id),
    INDEX idx_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='사용자';
```

#### 2. POST (게시글)
```sql
CREATE TABLE post (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '게시글 ID',
    user_id BIGINT NOT NULL COMMENT '작성자 ID',
    title VARCHAR(200) NOT NULL COMMENT '게시글 제목',
    content TEXT NOT NULL COMMENT '게시글 내용',
    book_title VARCHAR(200) NOT NULL COMMENT '책 제목',
    book_author VARCHAR(100) NOT NULL COMMENT '책 저자',
    book_cover VARCHAR(500) COMMENT '책 표지 URL',
    book_publisher VARCHAR(100) COMMENT '출판사',
    rating TINYINT NOT NULL COMMENT '평점 (1-5)',
    start_date DATE COMMENT '독서 시작일',
    end_date DATE COMMENT '독서 완료일',
    view_count INT DEFAULT 0 COMMENT '조회수',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '작성일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    deleted_at TIMESTAMP NULL COMMENT '삭제일시 (soft delete)',
    
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at),
    INDEX idx_rating (rating),
    INDEX idx_book_title (book_title),
    INDEX idx_deleted_at (deleted_at),
    
    CHECK (rating BETWEEN 1 AND 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='게시글';
```

#### 3. COMMENT (댓글)
```sql
CREATE TABLE comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '댓글 ID',
    post_id BIGINT NOT NULL COMMENT '게시글 ID',
    user_id BIGINT NOT NULL COMMENT '작성자 ID',
    content VARCHAR(500) NOT NULL COMMENT '댓글 내용 (max 500자)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '작성일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    deleted_at TIMESTAMP NULL COMMENT '삭제일시 (soft delete)',
    
    FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    
    INDEX idx_post_id (post_id),
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at),
    INDEX idx_deleted_at (deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='댓글';
```

### 관계도
```
USER (1) ──────< (N) POST
  │
  └──────< (N) COMMENT

POST (1) ──────< (N) COMMENT
```

### 샘플 데이터
```sql
-- 사용자 샘플 데이터
INSERT INTO user (email, nickname, profile_image, provider, provider_id, favorite_genres) VALUES
('user1@example.com', '홍길동', 'https://example.com/profile1.jpg', 'google', 'google123', '["소설", "에세이"]'),
('user2@example.com', '김철수', 'https://example.com/profile2.jpg', 'kakao', 'kakao456', '["시", "인문"]');

-- 게시글 샘플 데이터
INSERT INTO post (user_id, title, content, book_title, book_author, book_cover, book_publisher, rating, start_date, end_date, view_count) VALUES
(1, '이 책 정말 좋아요!', '하여가를 읽고 나서 정말 많은 생각을 하게 되었습니다. 특히...', '하여가', '김하나', 'https://example.com/book1.jpg', '문학동네', 5, '2024-11-01', '2024-11-15', 123),
(2, '강력 추천합니다', '완독하는 데 3일밖에 안 걸렸어요. 정말 재미있게 읽었습니다...', '82년생 김지영', '조남주', 'https://example.com/book2.jpg', '민음사', 4, '2024-11-10', '2024-11-13', 89);

-- 댓글 샘플 데이터
INSERT INTO comment (post_id, user_id, content) VALUES
(1, 2, '저도 이 책 읽었어요! 정말 공감되는 내용이 많았습니다.'),
(1, 1, '감사합니다! 다른 분들도 꼭 읽어보셨으면 좋겠어요.'),
(2, 1, '저도 관심있는 책이었는데 이 글 보고 바로 구매했습니다!');
```

---

## 프로젝트 평가

### ✅ 잘된 점

1. **요구사항 명세서**
   - 체계적인 17개 요구사항 정의
   - 도메인별 분류 및 우선순위 명확
   - 실제 개발에 바로 사용 가능

2. **API 명세서**
   - RESTful 설계 원칙 준수
   - 일관된 응답 형식
   - 명확한 인증 및 상태 코드 정의

3. **데이터베이스 스키마**
   - 명확한 테이블 구조
   - 적절한 인덱싱
   - Soft Delete 구현
   - 외래키 제약조건

### ❌ 개선 필요 사항

1. **누락된 기능**
   - 도서 정보 자동 완성 API 미구현
   - 페이지네이션 세부 로직 불명확

2. **데이터베이스 설계**
   - 책 정보 중복 저장 (정규화 고려 필요)
   - 별도 BOOK 테이블 검토 필요

3. **문서 완성도**
   - 기능 명세서 미완성
   - UI/UX 설계서 없음
   - 개발 일정 없음

### 종합 평가: 75/100

**MVP 개발 시작 가능 수준**이지만, 완전한 프로젝트 문서로는 보완이 필요합니다.

---

## 기술 스택 (권장)

### Frontend
- React 18+
- TypeScript
- TailwindCSS
- React Query
- Zustand

### Backend
- Node.js (Express) 또는 NestJS
- TypeScript
- MySQL 8.0+
- JWT
- OAuth 2.0

### DevOps
- Docker
- GitHub Actions
- AWS (EC2, RDS, S3)

---

**문서 작성일:** 2024-12-16
**프로젝트 버전:** 1.0.0
