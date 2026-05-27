package com.fitflow.clover.presentation.product

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fitflow.clover.domain.modal.ProductDetailModel
import com.fitflow.clover.domain.modal.ProductImageModel
import com.fitflow.clover.domain.modal.ProductSummaryModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductSummaryCard(
    product: ProductSummaryModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(103.dp)
            .clickable(onClick = onClick)
    ) {
        ProductImagePlaceholder(
            modifier = Modifier
                .width(103.dp)
                .height(150.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${product.name}\n${product.grade}\n${formatPrice(product.price)}",
            color = Color.Black,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
            fontWeight = FontWeight.Normal,
            maxLines = 3
        )
    }
}

@Composable
fun ProductDetailImagePager(
    images: List<ProductImageModel>,
    modifier: Modifier = Modifier
) {
    var currentIndex by remember(images) {
        mutableIntStateOf(0)
    }

    val safeIndex = currentIndex.coerceIn(
        minimumValue = 0,
        maximumValue = (images.size - 1).coerceAtLeast(0)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.9f)
                .background(Color(0xFFEDEFF2)),
            contentAlignment = Alignment.Center
        ) {
            if (images.isEmpty()) {
                Text(
                    text = "등록된 이미지가 없습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF777777),
                    textAlign = TextAlign.Center
                )
            } else {
                ProductImagePlaceholder(
                    modifier = Modifier.fillMaxSize()
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        enabled = safeIndex > 0,
                        onClick = {
                            if (currentIndex > 0) {
                                currentIndex--
                            }
                        },
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Text(text = "‹")
                    }

                    Text(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .background(
                                color = Color.Black.copy(alpha = 0.55f),
                                shape = RoundedCornerShape(999.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        text = "${safeIndex + 1} / ${images.size}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )

                    OutlinedButton(
                        enabled = safeIndex < images.size - 1,
                        onClick = {
                            if (currentIndex < images.size - 1) {
                                currentIndex++
                            }
                        },
                        shape = RoundedCornerShape(999.dp)
                    ) {
                        Text(text = "›")
                    }
                }
            }
        }
    }
}

@Composable
fun ProductInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            modifier = Modifier.width(92.dp),
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF777777)
        )

        Text(
            modifier = Modifier.weight(1f),
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF222222)
        )
    }
}

@Composable
fun ProductDetailBottomBar(
    product: ProductDetailModel,
    isWishlistProcessing: Boolean,
    onClickWishlist: () -> Unit,
    onClickChat: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .navigationBarsPadding()
    ) {
        Divider(
            color = Color(0xFFEDEDED)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        if (product.isWishlisted) {
                            Color(0xFFFFE8EE)
                        } else {
                            Color(0xFFF1F3F5)
                        }
                    )
                    .clickable(
                        enabled = !isWishlistProcessing
                    ) {
                        onClickWishlist()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (product.isWishlisted) "♥" else "♡",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (product.isWishlisted) {
                        Color(0xFFE83E65)
                    } else {
                        Color(0xFF555555)
                    }
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                onClick = onClickChat,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF202C59),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "채팅하기",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ProductImagePlaceholder(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .background(Color(0xFFD9D9D9))
            .border(1.dp, Color(0xFFBFC7CC))
    ) {
        drawRect(
            color = Color(0xFFBFC7CC),
            size = size
        )

        val iconPath = Path().apply {
            moveTo(size.width * 0.28f, size.height * 0.70f)
            lineTo(size.width * 0.42f, size.height * 0.52f)
            lineTo(size.width * 0.55f, size.height * 0.64f)
            lineTo(size.width * 0.70f, size.height * 0.42f)
            lineTo(size.width * 0.86f, size.height * 0.70f)
        }

        drawPath(
            path = iconPath,
            color = Color.White,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        )

        drawCircle(
            color = Color.White,
            radius = 8.dp.toPx(),
            center = Offset(size.width * 0.34f, size.height * 0.30f)
        )
    }
}

fun formatPrice(
    price: Int
): String {
    return NumberFormat
        .getNumberInstance(Locale.KOREA)
        .format(price) + "원"
}