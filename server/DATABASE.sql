DROP DATABASE clover;

CREATE DATABASE clover;

USE clover;

-- 1. 회원 (member) 테이블
CREATE TABLE member
(
    member_id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '회원 고유 식별자',
    login_id          VARCHAR(50)  NOT NULL UNIQUE COMMENT '로그인 ID (6~25 자리, 영어/숫자/특수문자)',
    password          VARCHAR(255) NOT NULL COMMENT '비밀번호(BCrypt 암호화)',
    totp_secret       VARCHAR(64)  NULL COMMENT 'TOTP 비밀키(Base32)',
    is_totp_enabled   TINYINT      NOT NULL DEFAULT 0 COMMENT '2차 인증 사용 여부 (0: 미사용, 1: 사용)',
    name              VARCHAR(20)  NOT NULL COMMENT '회원 이름',
    nickname          VARCHAR(40)  NOT NULL UNIQUE COMMENT '커뮤니티 닉네임',
    email             VARCHAR(100) NOT NULL UNIQUE COMMENT '이메일 주소',
    is_email_verified TINYINT      NOT NULL DEFAULT 0 COMMENT '이메일 인증 여부 (0: 미인증, 1: 인증됨)',
    gender            VARCHAR(10) COMMENT '성별(MALE, FEMALE)',
    role              VARCHAR(20)  NOT NULL DEFAULT 'USER' COMMENT '권한 (USER, ADMIN)',
    member_rank       VARCHAR(20)  NOT NULL DEFAULT 'SEED' COMMENT '회원 등급 (SEED, SPROUT, THREE_LEAF, FOUR_LEAF, GOLDEN)',
    is_deleted        TINYINT      NOT NULL DEFAULT 0 COMMENT '탈퇴 정보 (0: 정상 회원, 1: 탈퇴 회원)',
    created_at        DATETIME(6)  NOT NULL COMMENT '가입 일시',
    updated_at        DATETIME(6)  NOT NULL COMMENT '정보 수정 일시'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 2. 패스키 (passkey_credential) 테이블
CREATE TABLE passkey_credential
(
    passkey_id    BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '패스키 고유 식별자',
    member_id     BIGINT      NOT NULL COMMENT '회원 PK',
    credential_id BLOB        NOT NULL COMMENT '기기에서 생성한 고유 자격증명 ID',
    public_key    BLOB        NOT NULL COMMENT '검증을 위한 기기의 공개키',
    sign_count    BIGINT      NOT NULL DEFAULT 0 COMMENT '복제 방지용 서명 카운트',
    user_handle   BLOB        NOT NULL COMMENT '사용자 식별용 고유 핸들',
    created_at    DATETIME(6) NOT NULL COMMENT '패스키 생성 일자',
    updated_at    DATETIME(6) NOT NULL COMMENT '패스키 수정 일자',
    CONSTRAINT fk_passkey_member FOREIGN KEY (member_id) REFERENCES member (member_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 3. 상품 (product) 테이블
CREATE TABLE product
(
    product_id       BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '상품 고유 식별자',
    seller_id        BIGINT       NOT NULL COMMENT 'FK: 판매자 회원 번호',
    category_id      BIGINT       NOT NULL COMMENT 'FK: 카테고리 식별자',
    name             VARCHAR(100) NOT NULL COMMENT '상품명',
    price            INT          NOT NULL COMMENT '판매가',
    content          TEXT         NOT NULL COMMENT '판매글 내용',
    size             VARCHAR(50)  NOT NULL COMMENT '의류 사이즈',
    grade            VARCHAR(20)  NOT NULL COMMENT '상품 상태',
    trading_area     VARCHAR(100) NOT NULL COMMENT '거래 가능 지역 (예: 천안, 서울 등)',
    recommended_type VARCHAR(50) COMMENT '상품을 추천하는 체형',
    post_status      VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE' COMMENT '판매 상태(ACTIVE, RESERVED, SOLD_OUT, HIDDEN, DELETED)',
    view_count       INT          NOT NULL DEFAULT 0 COMMENT '조회수',
    wishlist_count   INT          NOT NULL DEFAULT 0 COMMENT '찜/장바구니 담긴 수',
    created_at       DATETIME(6)  NOT NULL COMMENT '상품 등록 일시',
    updated_at       DATETIME(6)  NOT NULL COMMENT '상품 정보 수정 일시'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 4. 카테고리 (category) 테이블
CREATE TABLE category
(
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '카테고리 고유 식별자',
    parent_id   BIGINT COMMENT '상위 카테고리 식별자 (대분류는 NULL, 소분류는 대분류의 ID)',
    name        VARCHAR(50) NOT NULL COMMENT '카테고리명',
    depth_level INT         NOT NULL DEFAULT 1 COMMENT '카테고리 깊이 (1: 대분류, 2: 소분류)',
    sort_order  INT         NOT NULL DEFAULT 0 COMMENT '동일 계층 내 UI 노출 정렬 순서',
    created_at  DATETIME(6) NOT NULL COMMENT '생성 일시',
    updated_at  DATETIME(6) NOT NULL COMMENT '수정 일시',

    CONSTRAINT fk_category_parent FOREIGN KEY (parent_id) REFERENCES category (category_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 5. 커뮤니티 (community) 테이블
CREATE TABLE community
(
    community_id  BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '게시글 고유 식별자',
    board_type    VARCHAR(20)  NOT NULL COMMENT '게시글 분류',
    member_id     BIGINT       NOT NULL COMMENT 'FK: 작성자 회원 번호',
    title         VARCHAR(255) NOT NULL COMMENT '게시글 제목',
    content       TEXT         NOT NULL COMMENT '게시글 내용',
    view_count    INT          NOT NULL DEFAULT 0 COMMENT '조회수',
    comment_count INT          NOT NULL DEFAULT 0 COMMENT '댓글 수',
    post_status   VARCHAR(20) COMMENT '게시글 상태(ACTIVE, HIDDEN, DELETED)',
    created_at    DATETIME(6) COMMENT '작성일시',
    updated_at    DATETIME(6) COMMENT '수정일시'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 6. 채팅방 (chat_room) 테이블
CREATE TABLE chat_room
(
    chat_room_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '채팅방 고유 식별자',
    product_id   BIGINT      NOT NULL COMMENT 'FK: 채팅이 시작된 상품 번호',
    buyer_id     BIGINT      NOT NULL COMMENT 'FK: 구매자 회원 번호',
    seller_id    BIGINT      NOT NULL COMMENT 'FK: 판매자 회원 번호',
    created_at   DATETIME(6) NOT NULL COMMENT '채팅방 생성 일시',
    updated_at   DATETIME(6) NOT NULL COMMENT '마지막 채팅 일시'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 7. 채팅 메시지 (chat_message) 테이블
CREATE TABLE chat_message
(
    message_id   BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '채팅 메시지 고유 식별자',
    chat_room_id BIGINT      NOT NULL COMMENT 'FK: 어느 채팅방에 속한 메시지인지',
    sender_id    BIGINT      NOT NULL COMMENT 'FK: 메시지를 보낸 사용자의 회원 번호',
    content      TEXT        NOT NULL COMMENT '실제 채팅 내용',
    message_type VARCHAR(20) NOT NULL DEFAULT 'TEXT' COMMENT '메시지 종류(TEXT, IMAGE, FILE)',
    created_at   DATETIME(6) NOT NULL COMMENT '메시지 전송 일시'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 8. 장바구니/찜 (wishlist) 테이블
CREATE TABLE wishlist
(
    wishlist_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '찜 내역 고유 식별자',
    member_id   BIGINT      NOT NULL COMMENT 'FK: 찜을 누른 회원 번호',
    product_id  BIGINT      NOT NULL COMMENT 'FK: 찜한 상품 번호',
    created_at  DATETIME(6) NOT NULL COMMENT '찜 누른 일시',
    CONSTRAINT uk_wishlist_member_product UNIQUE (member_id, product_id) COMMENT '찜 중복 방지 제약조건'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 9. 체형 분석 (bodytype) 테이블
CREATE TABLE bodytype
(
    bodytype_id      BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '체형 분석 식별 번호',
    member_id        BIGINT       NOT NULL UNIQUE COMMENT 'FK: 검사한 회원 번호',
    height           INT          NOT NULL COMMENT '키',
    weight           INT          NOT NULL COMMENT '몸무게',
    obesity_type     VARCHAR(50)  NOT NULL COMMENT '체형 타입',
    face_shape       VARCHAR(50)  NOT NULL COMMENT '얼굴형',
    personal_color   VARCHAR(50)  NOT NULL COMMENT '펄스널 컬러 진단 결과',
    result_title     VARCHAR(255) NOT NULL COMMENT '최종 진단값 요약 타이틀',
    result_recommend TEXT         NOT NULL COMMENT '상세 추천 내용',
    created_at       DATETIME(6)  NOT NULL COMMENT '분석 결과 생성 일시',
    updated_at       DATETIME(6)  NOT NULL COMMENT '분석 결과 수정 일시'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 10. 댓글 (comment) 테이블
CREATE TABLE comment
(
    comment_id   BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '댓글 고유 식별자',
    community_id BIGINT      NOT NULL COMMENT 'FK: 어느 게시글에 달린 댓글인지',
    member_id    BIGINT      NOT NULL COMMENT 'FK: 댓글 작성자 회원 번호',
    content      TEXT        NOT NULL COMMENT '댓글 내용',
    parent_id    BIGINT COMMENT 'FK: 대댓글 기능 구현용(원 댓글의 ID 저장)',
    is_deleted   TINYINT     NOT NULL DEFAULT 0 COMMENT '삭제 여부(0: 정상, 1: 삭제됨)',
    created_at   DATETIME(6) NOT NULL COMMENT '댓글 작성 일시',
    updated_at   DATETIME(6) NOT NULL COMMENT '댓글 수정 일시'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 11. 신고 (report) 테이블
CREATE TABLE report
(
    report_id   BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '신고 기록 고유 식별자',
    reporter_id BIGINT      NOT NULL COMMENT 'FK: 신고자 회원 번호',
    reported_id BIGINT      NOT NULL COMMENT 'FK: 피신고자 회원 번호',
    target_type VARCHAR(20) NOT NULL COMMENT '신고 대상 종류(PRODUCT, COMMUNITY, CHAT_MESSAGE, MEMBER)',
    target_id   BIGINT      NOT NULL COMMENT 'FK: 신고 대상 고유 식별자(상품번호, 게시글번호 등)',
    report_type VARCHAR(50) NOT NULL COMMENT '신고 카테고리',
    content     TEXT        NOT NULL COMMENT '상세 신고 사유',
    status      VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '처리 상태(PENDING, RESOLVED, REJECTED)',
    admin_memo  TEXT COMMENT '관리자 처리 결과 및 메모',
    created_at  DATETIME(6) NOT NULL COMMENT '신고 접수 일시',
    updated_at  DATETIME(6) NOT NULL COMMENT '처리(수정) 일시'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 12. 해시태그 사전 (hashtag) 테이블
CREATE TABLE hashtag
(
    hashtag_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '해시태그 고유 식별자',
    tag_name   VARCHAR(50) NOT NULL UNIQUE COMMENT '태그 단어',
    created_at DATETIME(6) NOT NULL COMMENT '태그 최초 생성 일시'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 13. 상품-해시태그 연결 (product_hashtag) 테이블
CREATE TABLE product_hashtag
(
    product_hashtag_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '매핑 고유 식별자',
    product_id         BIGINT NOT NULL COMMENT 'FK: 어떤 상품인지',
    hashtag_id         BIGINT NOT NULL COMMENT 'FK: 어떤 해시태그가 달렸는지'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 14. 알림 (notification) 테이블
CREATE TABLE notification
(
    notification_id   BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '알림 고유 식별자',
    receiver_id       BIGINT       NOT NULL COMMENT 'FK: 알림을 받는 사람의 회원 번호',
    sender_id         BIGINT COMMENT 'FK: 알림을 유발한 사람',
    notification_type VARCHAR(50)  NOT NULL COMMENT '알림 종류(CHAT, COMMENT, DEAL)',
    content           VARCHAR(255) NOT NULL COMMENT '알림 메시지 내용',
    related_id        BIGINT COMMENT '알림 클릭 시 이동할 타겟 ID',
    is_read           TINYINT      NOT NULL DEFAULT 0 COMMENT '알림 읽음 여부(0: 안 읽음, 1: 읽음)',
    created_at        DATETIME(6)  NOT NULL COMMENT '알림 발생 일시'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 15. 거래 (deal) 테이블
CREATE TABLE deal
(
    deal_id     BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '거래 고유 번호',
    buyer_id    BIGINT      NOT NULL COMMENT 'FK: 구매자 회원 번호',
    seller_id   BIGINT      NOT NULL COMMENT 'FK: 판매자 회원 번호',
    product_id  BIGINT      NOT NULL COMMENT 'FK: 거래 대상 상품 번호',
    deal_status VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS' COMMENT '거래 상태(IN_PROGRESS, COMPLETED, CANCELED, ABORTED)',
    created_at  DATETIME(6) NOT NULL COMMENT '거래 시작 일시',
    updated_at  DATETIME(6) NOT NULL COMMENT '거래 상태 수정/완료 일시'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 16. 거래 후기 (deal_review) 테이블
CREATE TABLE deal_review
(
    review_id  BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '거래 후기 고유 번호',
    deal_id    BIGINT      NOT NULL UNIQUE COMMENT 'FK: 어느 거래에 대한 후기인지',
    author_id  BIGINT      NOT NULL COMMENT 'FK: 후기 작성자 회원 번호',
    target_id  BIGINT      NOT NULL COMMENT 'FK: 후기 대상자',
    rating     TINYINT     NOT NULL COMMENT '평점',
    content    TEXT COMMENT '상세 후기 내용',
    created_at DATETIME(6) NOT NULL COMMENT '후기 작성 일시',
    updated_at DATETIME(6) NOT NULL COMMENT '후기 수정 일시'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 17. 이미지 관리 (image) 테이블
CREATE TABLE image
(
    image_id       BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '이미지 고유 식별자',
    image_url      VARCHAR(500) NOT NULL COMMENT '클라우드 스토리지 이미지 URL 도메인',
    reference_type VARCHAR(20)  NOT NULL COMMENT '구분자(PROFILE, COMMUNITY, CHAT, PRODUCT)',
    reference_id   BIGINT       NOT NULL COMMENT '연결 대상 고유 번호',
    sort_order     INT          NOT NULL DEFAULT 0 COMMENT '사진 표시 순서',
    created_at     DATETIME(6)  NOT NULL COMMENT '이미지 등록 일시'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 1. 대분류 (Root Category) 추가: parent_id는 NULL, depth_level은 1
INSERT INTO category (category_id, parent_id, name, depth_level, sort_order, created_at, updated_at)
VALUES (1, NULL, '남성의류', 1, 1, NOW(), NOW()),
       (2, NULL, '여성의류', 1, 2, NOW(), NOW()),
       (3, NULL, '전자기기', 1, 3, NOW(), NOW());

-- 2. 소분류 (Sub Category) 추가: parent_id에 위에서 만든 대분류 ID 매핑, depth_level은 2
-- 남성의류(1)의 하위 카테고리
INSERT INTO category (parent_id, name, depth_level, sort_order, created_at, updated_at)
VALUES (1, '아우터', 2, 1, NOW(), NOW()),
       (1, '상의', 2, 2, NOW(), NOW()),
       (1, '하의', 2, 3, NOW(), NOW());

-- 여성의류(2)의 하위 카테고리
INSERT INTO category (parent_id, name, depth_level, sort_order, created_at, updated_at)
VALUES (2, '아우터', 2, 1, NOW(), NOW()),
       (2, '상의', 2, 2, NOW(), NOW()),
       (2, '원피스/스커트', 2, 3, NOW(), NOW());

-- 전자기기(3)의 하위 카테고리
INSERT INTO category (parent_id, name, depth_level, sort_order, created_at, updated_at)
VALUES (3, '스마트폰', 2, 1, NOW(), NOW()),
       (3, '태블릿/PC', 2, 2, NOW(), NOW()),
       (3, '웨어러블', 2, 3, NOW(), NOW());