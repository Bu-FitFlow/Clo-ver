package com.fitflow.clover.presentation.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun ProductEditScreen(
    onBack: () -> Unit,
    onSubmit: (ProductEditFormState) -> Unit
) {
    var formState by remember {
        mutableStateOf(ProductEditFormState())
    }

    var validationMessage by remember {
        mutableStateOf<String?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "상품 등록",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        ProductTextField(
            label = "상품명",
            value = formState.name,
            onValueChange = {
                formState = formState.copy(name = it)
            },
            singleLine = true
        )

        ProductTextField(
            label = "가격",
            value = formState.price,
            onValueChange = {
                formState = formState.copy(price = it.filter { char -> char.isDigit() })
            },
            singleLine = true,
            keyboardType = KeyboardType.Number
        )

        ProductTextField(
            label = "상품 설명",
            value = formState.content,
            onValueChange = {
                formState = formState.copy(content = it)
            }
        )

        ProductTextField(
            label = "사이즈",
            value = formState.size,
            onValueChange = {
                formState = formState.copy(size = it)
            },
            singleLine = true
        )

        ProductTextField(
            label = "상품 상태",
            value = formState.grade,
            onValueChange = {
                formState = formState.copy(grade = it)
            },
            singleLine = true
        )

        ProductTextField(
            label = "거래 지역",
            value = formState.tradingArea,
            onValueChange = {
                formState = formState.copy(tradingArea = it)
            },
            singleLine = true
        )

        ProductTextField(
            label = "추천 체형",
            value = formState.recommendedType,
            onValueChange = {
                formState = formState.copy(recommendedType = it)
            },
            singleLine = true
        )

        ProductTextField(
            label = "판매 상태",
            value = formState.postStatus,
            onValueChange = {
                formState = formState.copy(postStatus = it.uppercase())
            },
            singleLine = true
        )

        ProductTextField(
            label = "카테고리 ID",
            value = formState.categoryId,
            onValueChange = {
                formState = formState.copy(categoryId = it.filter { char -> char.isDigit() })
            },
            singleLine = true,
            keyboardType = KeyboardType.Number
        )

        ProductTextField(
            label = "색상 ID",
            value = formState.colorId,
            onValueChange = {
                formState = formState.copy(colorId = it.filter { char -> char.isDigit() })
            },
            singleLine = true,
            keyboardType = KeyboardType.Number
        )

        validationMessage?.let { message ->
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFB3261E)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val message = formState.validateProductForm()

                if (message != null) {
                    validationMessage = message
                    return@Button
                }

                validationMessage = null
                onSubmit(formState.normalized())
            }
        ) {
            Text(text = "등록")
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onBack
        ) {
            Text(text = "이전")
        }
    }
}

@Composable
private fun ProductTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = label)
        },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        )
    )
}

private fun ProductEditFormState.validateProductForm(): String? {
    if (name.isBlank()) {
        return "상품명을 입력해 주세요."
    }

    if (price.isBlank()) {
        return "가격을 입력해 주세요."
    }

    val priceValue = price.trim().toIntOrNull()
    if (priceValue == null || priceValue <= 0) {
        return "가격은 1원 이상 숫자로 입력해 주세요."
    }

    if (content.isBlank()) {
        return "상품 설명을 입력해 주세요."
    }

    if (size.isBlank()) {
        return "사이즈를 입력해 주세요."
    }

    if (grade.isBlank()) {
        return "상품 상태를 입력해 주세요."
    }

    if (tradingArea.isBlank()) {
        return "거래 지역을 입력해 주세요."
    }

    if (categoryId.isBlank()) {
        return "카테고리 ID를 입력해 주세요."
    }

    if (categoryId.trim().toLongOrNull() == null) {
        return "카테고리 ID는 숫자로 입력해 주세요."
    }

    if (colorId.isNotBlank() && colorId.trim().toLongOrNull() == null) {
        return "색상 ID는 숫자로 입력해 주세요."
    }

    val safePostStatus = postStatus.trim().ifBlank {
        "ACTIVE"
    }

    val allowedStatus = setOf(
        "ACTIVE",
        "RESERVED",
        "SOLD_OUT",
        "HIDDEN"
    )

    if (safePostStatus !in allowedStatus) {
        return "판매 상태는 ACTIVE, RESERVED, SOLD_OUT, HIDDEN 중 하나로 입력해 주세요."
    }

    return null
}

private fun ProductEditFormState.normalized(): ProductEditFormState {
    return copy(
        name = name.trim(),
        price = price.trim(),
        content = content.trim(),
        size = size.trim(),
        grade = grade.trim(),
        tradingArea = tradingArea.trim(),
        recommendedType = recommendedType.trim(),
        postStatus = postStatus.trim().ifBlank {
            "ACTIVE"
        },
        categoryId = categoryId.trim(),
        colorId = colorId.trim()
    )
}