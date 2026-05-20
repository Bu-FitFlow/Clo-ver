package com.fitflow.clover.presentation.product

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ProductDetailUiModel(
    val id: Int = 1,
    val productName: String = "어 뭐 상의라고 해두지",
    val price: String = "25,800원",
    val size: String = "L",
    val fit: String = "레귤러",
    val category: String = "아우터 > 데님",
    val tradeInfo: String = "서울시 어딘가 / 직거래",
    val description: String = "음 잘 어울리겠죠 옷인데\n이쁘지 않을까요\n이런 식으로 나열 해서 적으면\n어떤 식으로 보일까요오",
    val imageCount: Int = 5
)

@Composable
fun ProductDetailsScreen(
    product: ProductDetailUiModel = ProductDetailUiModel(),
    onBack: () -> Unit,
    onClickLogo: () -> Unit,
    onReport: () -> Unit,
    onOpenChat: () -> Unit,
    onOpenSellerProfile: () -> Unit,
    onHome: () -> Unit,
    onBoard: () -> Unit,
    onNotification: () -> Unit,
    onMyPage: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var selectedImageIndex by remember { mutableIntStateOf(0) }
    var moreMenuVisible by remember { mutableStateOf(false) }
    var isWishListed by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            ProductDetailTopBar(
                onBack = onBack,
                onClickLogo = onClickLogo,
                onClickMore = {
                    moreMenuVisible = !moreMenuVisible
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(bottom = 92.dp)
            ) {
                Spacer(modifier = Modifier.height(36.dp))

                ProductImageArea(
                    selectedImageIndex = selectedImageIndex,
                    imageCount = product.imageCount,
                    onClick = {
                        selectedImageIndex = if (selectedImageIndex >= product.imageCount - 1) {
                            0
                        } else {
                            selectedImageIndex + 1
                        }
                    }
                )

                Spacer(modifier = Modifier.height(43.dp))

                ProductMainInfo(product = product)

                ProductDivider()

                ProductMetaInfo(product = product)

                ProductDivider()

                ProductDescription(product = product)

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        if (moreMenuVisible) {
            ProductMoreMenu(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(top = 57.dp, end = 9.dp),
                onReport = {
                    moreMenuVisible = false
                    onReport()
                },
                onShare = {
                    moreMenuVisible = false
                    shareProduct(context, product)
                },
                onProfile = {
                    moreMenuVisible = false
                    onOpenSellerProfile()
                }
            )
        }

        ProductBottomActionBar(
            isWishListed = isWishListed,
            onClickWish = {
                isWishListed = !isWishListed
            },
            onClickChat = onOpenChat,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        )
    }
}

@Composable
private fun ProductDetailTopBar(
    onBack: () -> Unit,
    onClickLogo: () -> Unit,
    onClickMore: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(57.dp)
            .background(Color(0x3399DE81))
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 11.dp)
                .size(40.dp)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            BackIcon()
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(94.dp)
                .height(57.dp)
                .clickable(onClick = onClickLogo),
            contentAlignment = Alignment.Center
        ) {
            ProductCloverLogo()
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 24.dp)
                .size(40.dp)
                .clickable(onClick = onClickMore),
            contentAlignment = Alignment.Center
        ) {
            MoreVerticalIcon()
        }
    }
}

@Composable
private fun ProductImageArea(
    selectedImageIndex: Int,
    imageCount: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .height(196.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(Color(0xFFD9D9D9))
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD9D9D9))
        )

        Text(
            text = "${selectedImageIndex + 1}/$imageCount",
            color = Color.Black,
            fontSize = 10.sp,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 6.dp, bottom = 6.dp)
        )
    }
}

@Composable
private fun ProductMainInfo(
    product: ProductDetailUiModel
) {
    Text(
        text = "상품명: ${product.productName}\n가격: ${product.price}",
        color = Color.Black,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 35.dp)
    )
}

@Composable
private fun ProductMetaInfo(
    product: ProductDetailUiModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 33.dp)
    ) {
        Column(
            modifier = Modifier.width(160.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(text = "사이즈", fontSize = 16.sp, color = Color.Black)
            Text(text = "핏", fontSize = 16.sp, color = Color.Black)
            Text(text = "카테고리", fontSize = 16.sp, color = Color.Black)
            Text(text = "거래 지역/ 거래 방식", fontSize = 16.sp, color = Color.Black)
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(text = product.size, fontSize = 16.sp, color = Color.Black)
            Text(text = product.fit, fontSize = 16.sp, color = Color.Black)
            Text(text = product.category, fontSize = 16.sp, color = Color.Black)
            Text(text = product.tradeInfo, fontSize = 16.sp, color = Color.Black)
        }
    }
}

@Composable
private fun ProductDescription(
    product: ProductDetailUiModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 31.dp)
    ) {
        Text(
            text = "상품 설명",
            color = Color.Black,
            fontSize = 20.sp,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = product.description,
            color = Color.Black,
            fontSize = 16.sp,
            lineHeight = 19.sp
        )
    }
}

@Composable
private fun ProductDivider() {
    Spacer(modifier = Modifier.height(34.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .height(1.dp)
            .background(Color.Black)
    )

    Spacer(modifier = Modifier.height(34.dp))
}

@Composable
private fun ProductBottomActionBar(
    isWishListed: Boolean,
    onClickWish: () -> Unit,
    onClickChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(69.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
            )
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
            )
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(92.dp)
                .fillMaxHeight()
                .clickable(onClick = onClickWish),
            contentAlignment = Alignment.Center
        ) {
            HeartActionIcon(isSelected = isWishListed)
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .height(42.dp)
                .background(
                    color = Color(0xFF99DE81),
                    shape = RoundedCornerShape(5.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(5.dp)
                )
                .clickable(onClick = onClickChat),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "채팅",
                color = Color.Black,
                fontSize = 20.sp,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
private fun ProductMoreMenu(
    modifier: Modifier = Modifier,
    onReport: () -> Unit,
    onShare: () -> Unit,
    onProfile: () -> Unit
) {
    Column(
        modifier = modifier
            .width(150.dp)
            .height(147.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        ProductMoreMenuItem(
            text = "신고 하기",
            iconType = ProductMoreIconType.REPORT,
            onClick = onReport
        )

        ProductMoreMenuItem(
            text = "공유 하기",
            iconType = ProductMoreIconType.SHARE,
            onClick = onShare
        )

        ProductMoreMenuItem(
            text = "프로필 보기",
            iconType = ProductMoreIconType.PROFILE,
            onClick = onProfile
        )
    }
}

private enum class ProductMoreIconType {
    REPORT,
    SHARE,
    PROFILE
}

@Composable
private fun ProductMoreMenuItem(
    text: String,
    iconType: ProductMoreIconType,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .clickable(onClick = onClick)
            .padding(start = 10.dp, end = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (iconType) {
            ProductMoreIconType.REPORT -> ReportIcon()
            ProductMoreIconType.SHARE -> ShareIcon()
            ProductMoreIconType.PROFILE -> ProfileIcon()
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = text,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 22.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ProductCloverLogo() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Canvas(
            modifier = Modifier.size(31.dp)
        ) {
            val leafColor = Color(0xFF8DD36F)
            val strokeColor = Color(0xFF5FAA43)
            val center = Offset(size.width / 2f, size.height / 2f)

            drawCircle(leafColor, 7.dp.toPx(), Offset(center.x, center.y - 7.dp.toPx()))
            drawCircle(leafColor, 7.dp.toPx(), Offset(center.x - 7.dp.toPx(), center.y))
            drawCircle(leafColor, 7.dp.toPx(), Offset(center.x + 7.dp.toPx(), center.y))
            drawCircle(leafColor, 7.dp.toPx(), Offset(center.x, center.y + 7.dp.toPx()))

            drawLine(
                color = strokeColor,
                start = center,
                end = Offset(center.x + 9.dp.toPx(), center.y + 13.dp.toPx()),
                strokeWidth = 1.6.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        Text(
            text = "Clo-ver",
            color = Color(0xFF67B74C),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 14.sp
        )
    }
}

@Composable
private fun BackIcon() {
    Canvas(
        modifier = Modifier.size(32.dp)
    ) {
        drawLine(
            color = Color.Black,
            start = Offset(size.width * 0.68f, size.height * 0.18f),
            end = Offset(size.width * 0.30f, size.height * 0.50f),
            strokeWidth = 2.4.dp.toPx(),
            cap = StrokeCap.Round
        )

        drawLine(
            color = Color.Black,
            start = Offset(size.width * 0.30f, size.height * 0.50f),
            end = Offset(size.width * 0.68f, size.height * 0.82f),
            strokeWidth = 2.4.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun MoreVerticalIcon() {
    Canvas(
        modifier = Modifier.size(28.dp)
    ) {
        drawCircle(Color.Black, 2.dp.toPx(), Offset(size.width / 2f, size.height * 0.26f))
        drawCircle(Color.Black, 2.dp.toPx(), Offset(size.width / 2f, size.height * 0.50f))
        drawCircle(Color.Black, 2.dp.toPx(), Offset(size.width / 2f, size.height * 0.74f))
    }
}

@Composable
private fun HeartActionIcon(
    isSelected: Boolean
) {
    Canvas(
        modifier = Modifier.size(31.dp)
    ) {
        val path = Path().apply {
            moveTo(size.width * 0.50f, size.height * 0.85f)
            cubicTo(
                size.width * 0.10f,
                size.height * 0.58f,
                size.width * 0.05f,
                size.height * 0.25f,
                size.width * 0.30f,
                size.height * 0.18f
            )
            cubicTo(
                size.width * 0.42f,
                size.height * 0.14f,
                size.width * 0.50f,
                size.height * 0.27f,
                size.width * 0.50f,
                size.height * 0.27f
            )
            cubicTo(
                size.width * 0.50f,
                size.height * 0.27f,
                size.width * 0.58f,
                size.height * 0.14f,
                size.width * 0.70f,
                size.height * 0.18f
            )
            cubicTo(
                size.width * 0.95f,
                size.height * 0.25f,
                size.width * 0.90f,
                size.height * 0.58f,
                size.width * 0.50f,
                size.height * 0.85f
            )
            close()
        }

        if (isSelected) {
            drawPath(
                path = path,
                color = Color(0xFFE53935)
            )
        }

        drawPath(
            path = path,
            color = if (isSelected) Color(0xFFE53935) else Color.Black,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

@Composable
private fun ReportIcon() {
    Canvas(modifier = Modifier.size(24.dp)) {
        drawCircle(
            color = Color.Black,
            radius = size.minDimension * 0.36f,
            center = Offset(size.width / 2f, size.height / 2f),
            style = Stroke(width = 2.dp.toPx())
        )

        drawLine(
            color = Color.Black,
            start = Offset(size.width * 0.28f, size.height * 0.28f),
            end = Offset(size.width * 0.72f, size.height * 0.72f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun ShareIcon() {
    Canvas(modifier = Modifier.size(24.dp)) {
        val left = Offset(size.width * 0.25f, size.height * 0.50f)
        val rightTop = Offset(size.width * 0.72f, size.height * 0.25f)
        val rightBottom = Offset(size.width * 0.72f, size.height * 0.75f)

        drawLine(Color.Black, left, rightTop, 2.dp.toPx(), StrokeCap.Round)
        drawLine(Color.Black, left, rightBottom, 2.dp.toPx(), StrokeCap.Round)

        drawCircle(Color.White, 4.dp.toPx(), left)
        drawCircle(Color.White, 4.dp.toPx(), rightTop)
        drawCircle(Color.White, 4.dp.toPx(), rightBottom)

        drawCircle(Color.Black, 4.dp.toPx(), left, style = Stroke(width = 2.dp.toPx()))
        drawCircle(Color.Black, 4.dp.toPx(), rightTop, style = Stroke(width = 2.dp.toPx()))
        drawCircle(Color.Black, 4.dp.toPx(), rightBottom, style = Stroke(width = 2.dp.toPx()))
    }
}

@Composable
private fun ProfileIcon() {
    Canvas(modifier = Modifier.size(24.dp)) {
        drawCircle(
            color = Color.Black,
            radius = size.minDimension * 0.38f,
            center = Offset(size.width / 2f, size.height / 2f),
            style = Stroke(width = 2.dp.toPx())
        )

        drawCircle(
            color = Color.Black,
            radius = size.minDimension * 0.12f,
            center = Offset(size.width / 2f, size.height * 0.40f),
            style = Stroke(width = 2.dp.toPx())
        )

        drawArc(
            color = Color.Black,
            startAngle = 205f,
            sweepAngle = 130f,
            useCenter = false,
            topLeft = Offset(size.width * 0.30f, size.height * 0.52f),
            size = Size(size.width * 0.40f, size.height * 0.28f),
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

private fun shareProduct(
    context: Context,
    product: ProductDetailUiModel
) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_TEXT,
            "CLO-VER 상품 공유\n상품명: ${product.productName}\n가격: ${product.price}"
        )
    }

    runCatching {
        context.startActivity(
            Intent.createChooser(
                shareIntent,
                "상품 공유하기"
            )
        )
    }
}