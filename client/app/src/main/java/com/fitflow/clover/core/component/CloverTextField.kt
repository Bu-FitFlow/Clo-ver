package com.fitflow.clover.core.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CloverTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String, // 'hint' 대신 'label'로 통일하는 것이 머티리얼 디자인 표준입니다.
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}


@Composable
fun CustomOutlinedInput(value: String, onValueChange: (String) -> Unit, placeholder: String, modifier: Modifier = Modifier) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth().height(50.dp).border(1.dp, Color.Gray, RoundedCornerShape(4.dp)).padding(horizontal = 12.dp, vertical = 13.dp),
        decorationBox = { inner ->
            if (value.isEmpty()) Text(placeholder, color = Color.LightGray, fontSize = 14.sp)
            inner()
        }
    )
}

