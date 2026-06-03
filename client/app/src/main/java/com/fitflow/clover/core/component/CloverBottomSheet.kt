package com.fitflow.clover.presentation.report.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitflow.clover.R // 프로젝트 리소스 R
import com.fitflow.clover.core.theme.CloverGreen
import com.fitflow.clover.presentation.report.ReportViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportReasonBottomSheet(
    viewModel: ReportViewModel,
    showBottomSheet: Boolean
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    // 뷰모델의 상태 수집
    val reportReasons = viewModel.reportReasons
    val temporarySelectedReason by viewModel.temporarySelectedReason.collectAsState()

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.setShowBottomSheet(false) }, // 바깥 누르면 닫기
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                // 상단 타이틀 및 X 닫기 버튼
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("신고 유형을 선택해주세요.", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    IconButton(onClick = { viewModel.setShowBottomSheet(false) }) {
                        // 요청하신 x 아이콘
                        Icon(imageVector = ImageVector.vectorResource(id = R.drawable.x), contentDescription = "닫기")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 라디오 버튼 목록 나열
                reportReasons.forEach { reason ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setTemporaryReason(reason) } // UX 최적화
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (temporarySelectedReason == reason),
                            onClick = { viewModel.setTemporaryReason(reason) },
                            colors = RadioButtonDefaults.colors(selectedColor = CloverGreen)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = reason, fontSize = 15.sp, color = Color.Black)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 바텀시트 내부의 초록색 '다음' 확인 버튼
                Button(
                    onClick = {
                        // 애니메이션 숨김 처리 후 뷰모델의 확정 로직 실행
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                viewModel.confirmReason()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CloverGreen),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("다음", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}