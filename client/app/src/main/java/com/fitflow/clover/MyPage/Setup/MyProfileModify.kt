package com.fitflow.clover.mypage.setup


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.fitflow.clover.R


@Composable
fun MyProfileModify(navController: NavHostController) {

    // 💡 각 드롭다운박스에서 "선택된 값"을 기억할 상태 장치들입니다.
    var selectedHeight by remember { mutableStateOf("키 선택") }
    var selectedWeight by remember { mutableStateOf("몸무게 선택") }
    var selectedObesity by remember { mutableStateOf("상하체 비만 상태 선택") }
    var selectedFaceShape by remember { mutableStateOf("얼굴형 선택") }

    // 💡 드롭다운 메뉴에 보여줄 리스트 데이터
    val heightOptions = (140..190 step 3).map { "${it}cm" }
    val weightOptions = (40..100 step 3).map { "${it}kg" }
    val obesityOptions = listOf("상체 비만", "하체 비만", "평균")
    val faceShapeOptions = listOf("계란형", "둥근형", "각진형", "역삼각형")

    // 전체 화면을 감싸는 도화지
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 상단바
            MyProfileModifyTopBar()

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
                            .align(Alignment.CenterStart)
                            .clickable{
                                navController.popBackStack()
                            }
                    )

                    Text(
                        text = "내 정보",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    // 🎯 [여기치환] 기존 완료 버튼이 있던 자리를 이 코드로 쏙 교체합니다!
                    Button(
                        onClick = {
                            // 완료 버튼을 누르면 "setup" 주소를 가진 MyPageSetup 화면으로 다이렉트 복귀!
                            navController.popBackStack(
                                route = MyPageDestinations.SETUP,
                                inclusive = false
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF99DE81)),
                        shape = RoundedCornerShape(5.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Text(
                            text = "완료",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // 150 150 프로필 사진
                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .background(Color(0xFFE0E0E0)) // 회색 배경
                        .border(1.dp, Color.Gray),     // 테두리
                    contentAlignment = Alignment.Center
                ){

                }

                // 프로필 사진 아래 ID 표기
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "ID",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )

                // 키, 몸무게 가로배치
                // 그 아래 상하체 비만 여부, 얼굴형 아래로 배치
                Spacer(modifier = Modifier.height(30.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp) // 요소 간격 고정
                ) {
                    // [키] / [몸무게] 가로 배치
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ProfileDropdownKeyBox(
                            label = "키",
                            selectedValue = selectedHeight,
                            options = heightOptions,
                            onOptionSelected = { selectedHeight = it },
                            modifier = Modifier.weight(1f)
                        )
                        ProfileDropdownKeyBox(
                            label = "몸무게",
                            selectedValue = selectedWeight,
                            options = weightOptions,
                            onOptionSelected = { selectedWeight = it },
                            modifier = Modifier.weight(1f)
                        )
                       /*
                        // 키 입력 박스 (Row 안에서 반씩 나눠 갖도록 weight 1f 부여)
                        ProfileModifyInfoBox(text = "키", modifier = Modifier.weight(1f))
                        // 몸무게 입력 박스
                        ProfileModifyInfoBox(text = "몸무게", modifier = Modifier.weight(1f))
                        */
                    }
                    // 상하체 비만 박스
                    ProfileDropdownKeyBox(
                        label = "상하체 비만",
                        selectedValue = selectedObesity,
                        options = obesityOptions,
                        onOptionSelected = { selectedObesity = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // 얼굴형 박스
                    ProfileDropdownKeyBox(
                        label = "얼굴형",
                        selectedValue = selectedFaceShape,
                        options = faceShapeOptions,
                        onOptionSelected = { selectedFaceShape = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                   /*
                    // [상하체 비만] 박스
                    ProfileModifyInfoBox(text = "상하체 비만", modifier = Modifier.fillMaxWidth())

                    // [얼굴형] 박스
                    ProfileModifyInfoBox(text = "얼굴형", modifier = Modifier.fillMaxWidth())
                    */
                }
            }
        }
    }
}

@Composable
fun MyProfileModifyTopBar() {
    Box(
        modifier = Modifier
            .width(393.dp)  // 가로 사이즈
            .height(57.dp) // 세로 사이즈
            .background(Color.White) // 배경을 흰색으로 채움
    ) {
    }
}

@Composable
fun ProfileDropdownKeyBox(
    label: String,                 // "키", "몸무게" 같은 분류 라벨
    selectedValue: String,         // 현재 선택되어 상자에 띄워줄 값
    options: List<String>,         // 눌렀을 때 아래로 뜰 리스트 목록
    onOptionSelected: (String) -> Unit, // 사용자가 항목을 클릭했을 때 작동할 치트키
    modifier: Modifier = Modifier
) {
    // 이 박스의 드롭다운 메뉴가 열려있는지 닫혀있는지 제어하는 상태 변수
    var isExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        // 클릭 가능한 메인 상자
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(8.dp))
                .clickable { isExpanded = !isExpanded } // 누르면 열림/닫힘 토글 🔄
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 선택된 값이 없으면(기본문구면) 원래 라벨을 띄우고, 선택됐으면 선택된 값을 보여줍니다.
            Text(
                text = if (selectedValue.contains("선택")) "$label: $selectedValue" else selectedValue,
                fontSize = 16.sp,
                color = if (selectedValue.contains("선택")) Color.Gray else Color.Black
            )
            // 아래 방향 화살표 아이콘 🔽
            Icon(
                painter = painterResource(id = android.R.drawable.arrow_down_float),
                contentDescription = "드롭다운 화살표",
                modifier = Modifier.size(12.dp),
                tint = Color.Gray
            )
        }

        // 실제로 아래로 펼쳐지는 리스트 박스
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }, // 바깥쪽 누르면 닫히게 설정
            modifier = Modifier.background(Color.White)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option, fontSize = 16.sp, color = Color.Black) },
                    onClick = {
                        onOptionSelected(option) // 내가 고른 녀석으로 글자 교체!
                        isExpanded = false       // 고르고 나면 메뉴 닫기
                    }
                )
            }
        }
    }
}

/*

@Composable
fun ProfileModifyInfoBox(text: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ){
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}
 */


/*
@Composable
fun MyProfileModifyTopBar() {
    Box(
        modifier = Modifier
            .width(393.dp)  // 가로 사이즈
            .height(63.dp) // 세로 사이즈
            .background(Color.White)
            .drawBehind {
                // 하단에 1dp 굵기의 회색 선을 그어 경계면을 확실히 함
                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            },
        //contentAlignment = Alignment.Center // 내부 글자를 중앙 정렬
    ) {
    }
}
 */



//미리보기 도화지 설정창
//showBackground = true 미리보기 창 배경 흰색으로 설정
//device 미리보기 화면 크기 고정
@Preview(showBackground = true, device = "spec:width=393dp,height=852dp")
//컴포즈 화면을 만드는 함수
@Composable
//미리보기 상자의 이름
fun MyProfileModifyPreview() {
    // 진짜 화면 불러오기
    MyProfileModify(navController = rememberNavController())
}
