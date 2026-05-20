package com.fitflow.clover.domain.modal

// ─────────────────────────────────────────────────────────
// 1. 대분류 카테고리
// ─────────────────────────────────────────────────────────
enum class ProductMainCategory(val displayName: String) {
    ALL("전체"),
    TOP("상의"),
    PANTS("바지"),
    OUTER("아우터"),
    DRESS_SKIRT("원피스/스커트")
}

// ─────────────────────────────────────────────────────────
// 2. 세부 카테고리 (대분류에 연동)
// ─────────────────────────────────────────────────────────
enum class ProductSubCategory(
    val displayName: String,
    val mainCategory: ProductMainCategory
) {
    // 상의
    LONG_SLEEVE("긴팔티셔츠", ProductMainCategory.TOP),
    SHORT_SLEEVE("반소매티셔츠", ProductMainCategory.TOP),
    SHIRT_BLOUSE("서츠/블라우스", ProductMainCategory.TOP),
    HOODIE("후드티셔츠", ProductMainCategory.TOP),
    SWEATSHIRT("맨투맨", ProductMainCategory.TOP),
    KNIT_SWEATER("니트/스웨터", ProductMainCategory.TOP),
    SLEEVELESS("민소매티셔츠", ProductMainCategory.TOP),
    ETC_TOP("기타 상의", ProductMainCategory.TOP),

    // 바지
    DENIM("데님팬츠", ProductMainCategory.PANTS),
    TRAINING_PANTS("트레이닝 팬츠", ProductMainCategory.PANTS),
    SHORT_PANTS("숏 팬츠", ProductMainCategory.PANTS),
    COTTON_PANTS("코튼 팬츠", ProductMainCategory.PANTS),
    SLACKS("슬랙스", ProductMainCategory.PANTS),
    ETC_PANTS("기타 하의", ProductMainCategory.PANTS),

    // 아우터
    CARDIGAN("가디건", ProductMainCategory.OUTER),
    JACKET("자켓", ProductMainCategory.OUTER),
    ZIP_UP("집업/점퍼", ProductMainCategory.OUTER),
    WINDBREAKER("바람막이", ProductMainCategory.OUTER),
    COAT("코트", ProductMainCategory.OUTER),
    FLEECE("플리스", ProductMainCategory.OUTER),
    FIELD_JACKET("야상", ProductMainCategory.OUTER),
    PADDING("패딩", ProductMainCategory.OUTER),
    ETC_OUTER("기타 아우터", ProductMainCategory.OUTER),

    // 원피스/스커트
    MINI_DRESS("미니원피스", ProductMainCategory.DRESS_SKIRT),
    MIDI_DRESS("미디원피스", ProductMainCategory.DRESS_SKIRT),
    MAXI_DRESS("맥시원피스", ProductMainCategory.DRESS_SKIRT),
    MINI_SKIRT("미니스커트", ProductMainCategory.DRESS_SKIRT),
    MIDI_SKIRT("미디스커트", ProductMainCategory.DRESS_SKIRT),
    LONG_SKIRT("롱스커트", ProductMainCategory.DRESS_SKIRT);

    companion object {
        fun getByMainCategory(main: ProductMainCategory): List<ProductSubCategory> {
            return entries.filter { it.mainCategory == main }
        }
    }
}

// ─────────────────────────────────────────────────────────
// 3. 상품 목록 아이템 (목록 화면용)
// ─────────────────────────────────────────────────────────
data class ProductSummary(
    val productId: Long,
    val title: String,
    val price: Int,
    val thumbnailImageUrl: String?,
    val mainCategory: ProductMainCategory,
    val subCategory: ProductSubCategory,
    val likeCount: Int,
    val createdAt: String,
    val isSold: Boolean
)