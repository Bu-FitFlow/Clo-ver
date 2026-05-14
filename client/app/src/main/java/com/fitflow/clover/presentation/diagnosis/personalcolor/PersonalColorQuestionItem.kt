package com.fitflow.clover.presentation.diagnosis.personalcolor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PersonalColorQuestionItem(
    question: PersonalColorQuestion,
    selectedTone: PersonalColorTone?,
    onSelect: (PersonalColorTone) -> Unit
) {
    Column {
        Text(
            text = question.title,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 28.dp)
        )

        question.options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 14.dp)
            ) {
                RadioButton(
                    selected = selectedTone == option.tone,
                    onClick = {
                        onSelect(option.tone)
                    }
                )

                Text(
                    text = option.label,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}