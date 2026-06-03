package com.fitflow.clover.core.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitflow.clover.R


// --- 공통 컴포넌트 ---

@Composable
fun CustomCheckBoxRow(text: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit, onDetailClick: (() -> Unit)? = null) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable { onCheckedChange(!checked) }, verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(24.dp).border(1.dp, Color.Gray, RoundedCornerShape(4.dp)), contentAlignment = Alignment.Center) {
            if (checked) Image(painter = painterResource(id = R.drawable.check), contentDescription = null, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, fontSize = 14.sp, modifier = Modifier.weight(1f))
        if (onDetailClick != null) Text("보기", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.clickable { onDetailClick() }.padding(8.dp))
    }
}