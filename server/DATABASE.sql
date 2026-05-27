DROP DATABASE IF EXISTS clover;

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
    wishlist_count INT NOT NULL DEFAULT 0 COMMENT '좋아요 수',
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
    is_read      TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '읽음 여부 (0: 안읽음, 1: 읽음)',
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
    review_id        BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '거래 후기 고유 번호',
    deal_id          BIGINT      NOT NULL UNIQUE COMMENT 'FK: 어느 거래에 대한 후기인지',
    author_id        BIGINT      NOT NULL COMMENT 'FK: 후기 작성자 회원 번호',
    target_id        BIGINT      NOT NULL COMMENT 'FK: 후기 대상자',
    rating           TINYINT     NOT NULL COMMENT '현재 평점',
    content          TEXT COMMENT '현재 상세 후기 내용',
    original_rating  TINYINT COMMENT '수정 전 원본 평점',
    original_content TEXT COMMENT '수정 전 원본 후기 내용',
    is_edited        TINYINT(1)  NOT NULL DEFAULT 0 COMMENT '수정 여부 (0: 미수정, 1: 수정됨)',
    created_at       DATETIME(6) NOT NULL COMMENT '후기 작성 일시',
    updated_at       DATETIME(6) NOT NULL COMMENT '후기 수정 일시'
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

-- 상품 정보 샘플 데이터
INSERT INTO product (seller_id, category_id, name, price, content,
                     size, grade, trading_area, recommended_type,
                     post_status, view_count, wishlist_count, created_at, updated_at)
VALUES
-- [남성의류] 4: 아우터, 5: 상의, 6: 하의
(1, 5, '나이키 스우시 반팔티 블랙', 35000, '여름에 입기 좋은 나이키 반팔티입니다.', 'L', 'S급', '천안시 서북구', '보통 체형', 'ACTIVE', 15, 2, NOW(), NOW()),
(2, 6, '리바이스 501 오리지널 핏 청바지', 45000, '사이즈 미스로 팝니다. 핏 엄청 예쁘게 떨어져요.', '32', 'A급', '천안시 동남구', '하체 발달형', 'ACTIVE', 45, 3,
 NOW(), NOW()),
(3, 4, '아디다스 트랙탑 져지', 20000, '마실용으로 막 입기 좋습니다. 사용감이 좀 있어서 싸게 올려요.', 'XL', 'B급', '아산시 배방읍', '통통 체형', 'ACTIVE', 120, 5,
 NOW(), NOW()),
(1, 5, '무신사 스탠다드 릴렉스 핏 크루 넥 반팔티', 10000, '색상별로 샀다가 안 입는 색상 팝니다. 미개봉 새상품.', 'L', 'S급', '천안시 서북구', '마른 체형', 'ACTIVE', 5,
 0, NOW(), NOW()),
(2, 6, '와이드 슬랙스 블랙', 15000, '무탠다드 와이드 슬랙스입니다. 기장 수선 안 했어요.', '30', 'A급', '평택시 비전동', '보통 체형', 'SOLD_OUT', 88, 1, NOW(),
 NOW()),
(1, 4, '노스페이스 눕시 패딩 블랙', 150000, '겨울 준비 미리 하세요. 상태 아주 좋습니다.', 'L', 'A급', '천안시 서북구', '보통 체형', 'ACTIVE', 200, 15, NOW(),
 NOW()),

-- [여성의류] 7: 아우터, 8: 상의, 9: 원피스/스커트
(3, 8, '자라(ZARA) 크롭 블라우스', 25000, '한 번 입고 보관 중입니다. 봄에 입기 딱 좋아요.', 'M', 'S급', '천안시 동남구', '마른 체형', 'ACTIVE', 22, 1, NOW(),
 NOW()),
(1, 9, '플라워 롱 원피스', 30000, '휴양지 룩으로 샀는데 여행 취소돼서 팝니다ㅠㅠ', 'FREE', 'S급', '천안시 서북구', '하체 커버형', 'ACTIVE', 55, 6, NOW(),
 NOW()),
(2, 7, '핸드메이드 롱코트 오트밀', 80000, '원가 20만원 넘게 준 코트입니다. 드라이클리닝 완료.', 'FREE', 'A급', '아산시 탕정면', '보통 체형', 'RESERVED', 150, 12,
 NOW(), NOW()),
(3, 9, 'A라인 미니스커트', 12000, '기본템으로 입기 좋아요. 스판 짱짱합니다.', 'S', 'B급', '평택시 고덕동', '마른 체형', 'ACTIVE', 10, 0, NOW(), NOW()),
(1, 8, '폴로 랄프로렌 꽈배기 니트', 65000, '정품 맞습니다. 보풀 약간 있어서 저렴하게 넘겨요.', 'S', 'B급', '천안시 서북구', '보통 체형', 'ACTIVE', 300, 20, NOW(),
 NOW()),

-- [전자기기 - 스마트폰] 10: 스마트폰
(2, 10, '아이폰 13 프로 256GB 실버', 750000, '기스 하나 없는 S급입니다. 배터리 88%.', '단일사이즈', 'S급', '천안시 서북구', NULL, 'ACTIVE', 340, 15,
 NOW(), NOW()),
(3, 10, '갤럭시 S22 울트라 버건디', 600000, '화면 모서리에 미세한 찍힘 있습니다. 케이스 끼우면 안 보여요.', '단일사이즈', 'B급', '아산시 배방읍', NULL, 'ACTIVE', 150,
 8, NOW(), NOW()),
(1, 10, '아이폰 12 미니 128GB 화이트', 300000, '서브폰으로 쓰다가 팝니다. 가벼워서 좋아요.', '단일사이즈', 'A급', '천안시 동남구', NULL, 'SOLD_OUT', 400, 25,
 NOW(), NOW()),
(2, 10, '갤럭시 Z플립4 크림', 550000, '힌지 보호 케이스 계속 씌워서 썼습니다. 잔기스 X', '단일사이즈', 'S급', '천안시 서북구', NULL, 'ACTIVE', 280, 18, NOW(),
 NOW()),

-- [전자기기 - 태블릿/웨어러블 등] 11: 태블릿/PC, 12: 웨어러블
(3, 11, '아이패드 프로 11인치 3세대 M1', 850000, '영상 머신으로만 써서 상태 최고입니다. 애플펜슬 포함.', '단일사이즈', 'S급', '평택시 비전동', NULL, 'ACTIVE', 500,
 42, NOW(), NOW()),
(1, 12, '애플워치 SE 40mm 스페이스그레이', 150000, '생활기스 약간 있습니다. 충전기 같이 드려요.', '단일사이즈', 'B급', '천안시 서북구', NULL, 'RESERVED', 180,
 10, NOW(), NOW()),
(2, 11, '맥북 에어 M1 깡통 스그', 700000, '배터리 사이클 120. 박스 풀셋입니다.', '단일사이즈', 'A급', '아산시 탕정면', NULL, 'ACTIVE', 600, 55, NOW(),
 NOW()),
(1, 12, '갤럭시워치4 클래식 46mm', 120000, '베젤링 돌리는 맛이 좋습니다. 스트랩 3개 추가 증정.', '단일사이즈', 'A급', '천안시 서북구', NULL, 'ACTIVE', 90, 5,
 NOW(), NOW()),
(3, 11, '갤럭시탭 S7 FE Wi-Fi', 350000, '인강용으로 최고입니다. S펜 포함.', '단일사이즈', 'A급', '천안시 동남구', NULL, 'HIDDEN', 40, 2, NOW(),
 NOW()),

-- [기타 페이징 테스트용 데이터]
(1, 5, '스파오 베이직 셔츠 화이트', 15000, '면접 때 한 번 입었습니다.', 'L', 'S급', '천안시 서북구', '보통 체형', 'ACTIVE', 8, 0, NOW(), NOW()),
(2, 10, '아이폰 14 프로 맥스 512GB', 1200000, '자급제입니다. 영수증 있어요.', '단일사이즈', 'S급', '천안시 동남구', NULL, 'ACTIVE', 800, 60, NOW(),
 NOW()),
(3, 4, '지오지아 정장 마이', 40000, '결혼식 갈 때 한 번 입음.', '100', 'S급', '평택시 고덕동', '보통 체형', 'ACTIVE', 15, 1, NOW(), NOW()),
(1, 8, '자라 튜브탑', 10000, '이너로 입기 좋아요.', 'S', 'A급', '천안시 서북구', '마른 체형', 'ACTIVE', 20, 2, NOW(), NOW()),
(2, 12, '에어팟 프로 1세대', 100000, '본체에 철가루 방지 스티커 붙여서 썼습니다. 양쪽 소리 잘 나와요.', '단일사이즈', 'B급', '아산시 배방읍', NULL, 'ACTIVE', 220,
 14, NOW(), NOW());