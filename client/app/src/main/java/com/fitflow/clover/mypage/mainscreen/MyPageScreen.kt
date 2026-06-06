package com.fitflow.clover.mypage.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.fitflow.clover.R
import com.fitflow.clover.mypage.MyPageProductItem
import com.fitflow.clover.mypage.MyPageUiState

@Composable
fun MyPageScreen(
    uiState: MyPageUiState,
    onSettingsClick: () -> Unit,
    onMyWritingClick: () -> Unit,
    onGoodsClick: () -> Unit,
    onBackClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MyPageScreenTopBar()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(57.dp)
                    .background(Color(0x3399DE81))
            ) {
                Icon(
                    painter = painterResource(R.drawable.kakaotalk_20260514_111630855),
                    contentDescription = "뒤로가기 아이콘",
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(28.dp)
                        .align(Alignment.CenterStart)
                        .clickable { onBackClick() }
                )
                Text(
                    text = "마이페이지",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
                Icon(
                    painter = painterResource(R.drawable.settings),
                    contentDescription = "설정",
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(28.dp)
                        .align(Alignment.CenterEnd)
                        .clickable { onSettingsClick() },
                    tint = Color.Unspecified
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            ProfileImageBox(
                imageModel = uiState.profileImageModel,
                fallbackText = uiState.displayName.take(1)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = uiState.displayLoginId,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = uiState.displayName,
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(12.dp))

            DividerLine()

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 31.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "나의 클로버",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Icon(
                        painter = painterResource(R.drawable.seed_icon2),
                        contentDescription = "새싹",
                        modifier = Modifier
                            .size(70.dp)
                            .align(Alignment.Center),
                        tint = Color.Unspecified
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .background(Color.White, shape = CircleShape)
                        .border(1.dp, Color.Black, shape = CircleShape)
                        .padding(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(uiState.cloverProgress.coerceIn(0f, 1f))
                            .background(Color(0xFF99DE81), shape = CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            DividerLine()
            Spacer(modifier = Modifier.height(16.dp))

            MenuRow(
                text = "내 글 보기",
                onClick = onMyWritingClick
            )

            Spacer(modifier = Modifier.height(16.dp))
            DividerLine()
            Spacer(modifier = Modifier.height(16.dp))

            MenuRow(
                text = "내 판매 물품",
                onClick = onGoodsClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProductPreviewList(products = uiState.myProducts)
        }
    }
}

@Composable
private fun ProfileImageBox(
    imageModel: String?,
    fallbackText: String
) {
    Box(
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .background(Color(0xFFE0E0E0))
            .border(1.dp, Color.Gray, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (!imageModel.isNullOrBlank()) {
            Image(
                painter = rememberAsyncImagePainter(model = imageModel),
                contentDescription = "프로필 사진",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = fallbackText.ifBlank { "C" },
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun MenuRow(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 31.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Icon(
            painter = painterResource(R.drawable.kakaotalk_20260514_111630855),
            contentDescription = "더보기",
            modifier = Modifier
                .size(20.dp)
                .rotate(180f),
            tint = Color.Black
        )
    }
}

@Composable
private fun ProductPreviewList(products: List<MyPageProductItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 31.dp)
    ) {
        if (products.isEmpty()) {
            Text(
                text = "등록한 판매 물품이 없습니다.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            return@Column
        }

        products.take(3).forEachIndexed { index, product ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .border(1.dp, Color.Black)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    if (!product.thumbnailImageUrl.isNullOrBlank()) {
                        Image(
                            painter = rememberAsyncImagePainter(model = product.thumbnailImageUrl),
                            contentDescription = product.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name.ifBlank { "상품명 없음" },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${product.price}원 · ${product.postStatus}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (index < products.take(3).lastIndex) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.LightGray)
                )
            }
        }
    }
}

@Composable
private fun DividerLine() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 31.dp)
            .height(1.dp)
            .background(Color.Black)
    )
}

@Composable
fun MyPageScreenTopBar() {
    Box(
        modifier = Modifier
            .width(393.dp)
            .height(57.dp)
            .background(Color.White)
    )
}

@Preview(showBackground = true, device = "spec:width=393dp,height=852dp")
@Composable
fun MyPageScreenPreview() {
    MyPageScreen(
        uiState = MyPageUiState(
            loginId = "clover01",
            nickname = "clover",
            cloverProgress = 0.35f
        ),
        onSettingsClick = {},
        onMyWritingClick = {},
        onGoodsClick = {},
        onBackClick = {}
    )
}
