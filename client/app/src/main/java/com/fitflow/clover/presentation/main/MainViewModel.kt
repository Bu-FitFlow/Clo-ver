package com.fitflow.clover.presentation.main

data class MainProductUiModel(
    val id: Int,
    val name: String,
    val brand: String,
    val price: String,
    val imageUri: String? = null
)

data class MainCommunityPostUiModel(
    val id: Int,
    val title: String,
    val nickname: String,
    val date: String,
    val viewCount: Int,
    val commentCount: Int,
    val likeCount: Int
)

class MainViewModel {

    val recentProducts: List<MainProductUiModel> = listOf(
        MainProductUiModel(
            id = 1,
            name = "최근 1번 자켓",
            brand = "CLO-VER",
            price = "39,800원"
        ),
        MainProductUiModel(
            id = 2,
            name = "최근 2번 셔츠",
            brand = "CLO-VER",
            price = "24,900원"
        ),
        MainProductUiModel(
            id = 3,
            name = "최근 3번 팬츠",
            brand = "CLO-VER",
            price = "42,000원"
        ),
        MainProductUiModel(
            id = 4,
            name = "최근 4번 니트",
            brand = "Second Wear",
            price = "18,500원"
        ),
        MainProductUiModel(
            id = 5,
            name = "최근 5번 후드",
            brand = "Second Wear",
            price = "29,000원"
        ),
        MainProductUiModel(
            id = 6,
            name = "최근 6번 가디건",
            brand = "Second Wear",
            price = "33,000원"
        ),
        MainProductUiModel(
            id = 7,
            name = "최근 7번 코트",
            brand = "Eco Closet",
            price = "58,000원"
        ),
        MainProductUiModel(
            id = 8,
            name = "최근 8번 맨투맨",
            brand = "Eco Closet",
            price = "21,000원"
        ),
        MainProductUiModel(
            id = 9,
            name = "최근 9번 데님",
            brand = "Eco Closet",
            price = "35,500원"
        )
    )

    val bodyRecommendProducts: List<MainProductUiModel> = listOf(
        MainProductUiModel(
            id = 10,
            name = "추천 1번 블레이저",
            brand = "Fit Pick",
            price = "49,800원"
        ),
        MainProductUiModel(
            id = 11,
            name = "추천 2번 슬랙스",
            brand = "Fit Pick",
            price = "31,900원"
        ),
        MainProductUiModel(
            id = 12,
            name = "추천 3번 셔츠",
            brand = "Fit Pick",
            price = "22,000원"
        ),
        MainProductUiModel(
            id = 13,
            name = "추천 4번 점퍼",
            brand = "Body Match",
            price = "44,000원"
        ),
        MainProductUiModel(
            id = 14,
            name = "추천 5번 와이드팬츠",
            brand = "Body Match",
            price = "27,500원"
        ),
        MainProductUiModel(
            id = 15,
            name = "추천 6번 반팔티",
            brand = "Body Match",
            price = "12,900원"
        ),
        MainProductUiModel(
            id = 16,
            name = "추천 7번 롱코트",
            brand = "Style Mate",
            price = "66,000원"
        ),
        MainProductUiModel(
            id = 17,
            name = "추천 8번 청자켓",
            brand = "Style Mate",
            price = "37,800원"
        ),
        MainProductUiModel(
            id = 18,
            name = "추천 9번 조거팬츠",
            brand = "Style Mate",
            price = "25,800원"
        )
    )

    val latestCommunityPosts: List<MainCommunityPostUiModel> = listOf(
        MainCommunityPostUiModel(
            id = 1,
            title = "한정판 콜라보 티셔츠 솔직한 후기",
            nickname = "닉네임",
            date = "2026-04-24",
            viewCount = 120,
            commentCount = 4,
            likeCount = 20
        ),
        MainCommunityPostUiModel(
            id = 2,
            title = "중고 거래할 때 확인해야 할 점",
            nickname = "클로버",
            date = "2026-04-25",
            viewCount = 98,
            commentCount = 3,
            likeCount = 15
        ),
        MainCommunityPostUiModel(
            id = 3,
            title = "데님 자켓 코디 추천 받아요",
            nickname = "스타일러",
            date = "2026-04-26",
            viewCount = 210,
            commentCount = 8,
            likeCount = 31
        )
    )

    val popularCommunityPosts: List<MainCommunityPostUiModel> = listOf(
        MainCommunityPostUiModel(
            id = 4,
            title = "겨울 쿨톤에게 잘 맞는 색상 정리",
            nickname = "톤잘알",
            date = "2026-04-24",
            viewCount = 340,
            commentCount = 12,
            likeCount = 45
        ),
        MainCommunityPostUiModel(
            id = 5,
            title = "체형별 아우터 고르는 방법",
            nickname = "핏마스터",
            date = "2026-04-25",
            viewCount = 286,
            commentCount = 9,
            likeCount = 38
        )
    )
}