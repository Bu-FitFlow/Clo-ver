package com.fitflow.clover.mypage.setup


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fitflow.clover.R

@Composable
fun MyPageAccountPassword(navController: NavHostController) {

    // 💡 1. 알림(Toast)을 띄우기 위한 context 객체는 여기 함수 맨 위에 둡니다.
    val context = androidx.compose.ui.platform.LocalContext.current

    // 💡 사용자가 입력한 값을 기억하는 변수 3개 (프론트엔드 자체 UI 상태)
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // 💡 비밀번호 변경 완료 팝업을 제어하는 상태 변수
    var showSuccessDialog by remember { mutableStateOf(false) }

    // 1. 전체 화면을 감싸는 도화지
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // 2. 위에서 아래로 요소를 배치합니다. (Groovy의 LinearLayout vertical 느낌)
        Column(
            modifier = Modifier
                .fillMaxSize(),
            //.padding(16.dp), // iPhone 16 피그마 수치대로 여백 주기
            horizontalAlignment = Alignment.CenterHorizontally // 가운데 정렬

        ) {
            //PasswordTopBar()

            // 위에서 아래로 요소를 배치 (Groovy의 LinearLayout vertical 느낌)
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally // 가운데 정렬
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {

                    Icon(
                        painter = painterResource(R.drawable.kakaotalk_20260514_111630855),
                        contentDescription = "뒤로가기 아이콘",
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.CenterStart) // 🔥 Box 내부 정렬 규칙 적용
                            .clickable {
                                navController.popBackStack()
                            }
                    )

                    // 3. 이 텍스트도 Box의 자식이므로 Center (완전 중앙) 정렬이 가능합니다!
                    Text(
                        text = "비밀번호 재설정",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center) // 🔥 Box 내부 정렬 규칙 적용
                    )

                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp), // 이미지 비율에 맞춘 여백
                    verticalArrangement = Arrangement.spacedBy(16.dp)   // 입력창 간의 간격
                ) {
                    PasswordInputField(
                        value = currentPassword,
                        onValueChange = { currentPassword = it },
                        hint = "현재 비밀번호")
                    PasswordInputField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        hint = "새 비밀번호")
                    PasswordInputField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        hint = "새 비밀번호 재입력")

                    // 🔥 오른쪽 아래에 배치된 완료 버튼
                    Button(
                        onClick = {
                            // [검증 1] 빈 칸 확인
                            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                                android.widget.Toast.makeText(context, "모든 항목을 입력해주세요.", android.widget.Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            // [검증 2] 비밀번호 일치 확인
                            if (newPassword != confirmPassword) {
                                android.widget.Toast.makeText(context, "새 비밀번호가 일치하지 않습니다.", android.widget.Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            // 💡 모든 검증을 통과하면 Toast 대신 팝업을 띄웁니다!
                            showSuccessDialog = true

                           /*
                            // [검증 통과] 임시 완료 처리
                            android.widget.Toast.makeText(context, "비밀번호 변경이 완료되었습니다.", android.widget.Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                            */
                        },

                        modifier = Modifier
                            .align(Alignment.End) // 🔥 오른쪽 정렬
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF99DE81), // 버튼 배경색
                            contentColor = Color.Black    // 글자색
                        )
                    ) {
                        Text(
                            text = "완료",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }


// 🔥 비밀번호 변경 완료 알림 팝업창
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = {
                    // 팝업 바깥을 눌렀을 때의 처리 (보통은 아무것도 안 하거나 닫음)
                    showSuccessDialog = false
                },
                title = {
                    Text(
                        text = "비밀번호 변경 완료",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                text = {
                    Text(
                        text = "비밀번호가 성공적으로 변경되었습니다.\n로그인 화면으로 돌아갑니다.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showSuccessDialog = false // 팝업 닫기
                            navController.popBackStack() // 💡 [핵심] 확인을 누르면 마이페이지 밖으로 나감!
                        }
                    ) {
                        Text("확인", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}

// 🔥 입력창을 그리기 위한 재사용 컴포넌트 (파일 하단이나 외부에 추가해 주세요)
@Composable
fun PasswordInputField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String) {


    // 👁️ 비밀번호 시각화 여부를 상태로 관리 (기본값: true = 숨김 상태)
    var isPasswordHidden by remember { mutableStateOf(true) }

    OutlinedTextField(

        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = hint,
                color = Color.Black,
                fontSize = 16.sp) // 이미지처럼 검은색 텍스트 힌트
        },
        modifier = Modifier.fillMaxWidth(),
    visualTransformation = if (isPasswordHidden) PasswordVisualTransformation() else VisualTransformation.None,
    trailingIcon = {
        // 프로젝트에 기본 내장된 아이콘 혹은 갖고 계신 눈 모양 리소스를 사용하세요.
        // 여기서는 임시로 가시성 토글에 맞는 안드로이드 시스템 기본 드로어블을 지정하거나
        // 커스텀 아이콘을 쓸 수 있도록 처리했습니다.
        val iconRes = if (isPasswordHidden) {
            android.R.drawable.ic_menu_view // 눈 감은 모양 또는 숨김 아이콘 역할
        } else {
            android.R.drawable.ic_secure // 눈 뜬 모양 또는 노출 아이콘 역할
        }

        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = if (isPasswordHidden) "비밀번호 보이기" else "비밀번호 숨기기",
            modifier = Modifier
                .size(24.dp)
                .clickable { isPasswordHidden = !isPasswordHidden }, // 클릭 시 상태 반전
            tint = Color.Gray
        )
    },
        shape = RoundedCornerShape(8.dp), // 이미지처럼 살짝 둥근 테두리 모양
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,      // 포커스 시 테두리 검은색
            unfocusedBorderColor = Color.Black,    // 평상시 테두리 검은색
            focusedContainerColor = Color.White,   // 내부 배경 흰색
            unfocusedContainerColor = Color.White,
            cursorColor = Color.Black              // 커서 색상 검은색
        )
    )
}

@Preview(showBackground = true, device = "spec:width=393dp,height=852dp")
@Composable
fun MyPageAccountPasswordPreview() {
    MyPageAccountPassword(navController = rememberNavController())
}
