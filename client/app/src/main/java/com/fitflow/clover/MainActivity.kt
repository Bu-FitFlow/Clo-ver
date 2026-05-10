package com.fitflow.clover

import com.fitflow.clover.ResetPasswordScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

val CloverGreen = Color(0xFF98E084)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") { SplashScreen(navController) }
                    composable("login") { LoginMain(navController) }
                    composable("join_terms") { JoinTerms(navController) }
                    composable("term_detail_1") { TermDetailScreen(navController, "이용약관 동의(필수)") }
                    composable("term_detail_2") { TermDetailScreen(navController, "개인정보 수집 및 이용동의(필수)") }
                    composable("join_detail") { JoinDetail(navController) }
                    composable("find_id_pw") { FindIdPw(navController) }
                    composable("reset_password") { ResetPasswordScreen(navController) }
                }
            }
        }
    }
}

// 1. 스플래시 화면 (기존 유지)
@Composable
fun SplashScreen(navController: NavController) {
    var progressValue by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        for (i in 1..100) {
            delay(20)
            progressValue = i / 100f
        }
        navController.navigate("login") { popUpTo("splash") { inclusive = true } }
    }
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Image(painter = painterResource(id = R.drawable.frame_31), contentDescription = null, modifier = Modifier.size(200.dp))
            Spacer(modifier = Modifier.height(24.dp))
            LinearProgressIndicator(progress = { progressValue }, modifier = Modifier.width(200.dp), color = CloverGreen)
            Spacer(modifier = Modifier.height(16.dp))
            Text("당신의 옷장에 행운을 배달 중이에요", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

// 2. 로그인 화면 (기존 유지)
@Composable
fun LoginMain(navController: NavController) {
    var id by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(modifier = Modifier.padding(horizontal = 30.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Image(painter = painterResource(id = R.drawable.frame_31), contentDescription = null, modifier = Modifier.size(250.dp))
            Spacer(modifier = Modifier.height(30.dp))
            OutlinedTextField(value = id, onValueChange = { id = it }, label = { Text("ID") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = pw, onValueChange = { pw = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { }, modifier = Modifier.fillMaxWidth().height(62.dp), colors = ButtonDefaults.buttonColors(containerColor = CloverGreen), shape = RoundedCornerShape(5.dp)) { Text("로그인", color = Color.Black) }
            Row(modifier = Modifier.padding(top = 16.dp)) {
                Text("아이디/비밀번호 찾기", modifier = Modifier.clickable { navController.navigate("find_id_pw") }, color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(100.dp))
                Text("아직 회원이 아니신가요?", modifier = Modifier.clickable { navController.navigate("join_terms") }, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

// 3. 아이디/비밀번호 찾기 (수정 완료)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindIdPw(navController: NavController) {
    var tabIndex by remember { mutableIntStateOf(0) }
    var idInput by remember { mutableStateOf("") }
    var nameInput by remember { mutableStateOf("") }
    var emailInput by remember { mutableStateOf("") }
    var authCodeInput by remember { mutableStateOf("") }

    // 로직 상태 변수
    var isAuthSent by remember { mutableStateOf(false) } // 인증 버튼 클릭 여부
    var showIdResult by remember { mutableStateOf(false) } // 아이디 결과창 표시 여부

    Scaffold(
        containerColor = Color.White, // 배경색 흰색 고정
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
                title = { Text(if (tabIndex == 0) "아이디 찾기" else "비밀번호 찾기", fontSize = 28.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Image(painter = painterResource(id = R.drawable.back), contentDescription = null, modifier = Modifier.size(24.dp))
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp)) {

            if (!showIdResult) {
                // 탭 버튼
                Spacer(modifier = Modifier.height(30.dp))
                Row(modifier = Modifier.fillMaxWidth().padding(top = 30.dp, bottom = 60.dp)) {
                    TabButton("아이디 찾기", tabIndex == 0, Modifier.weight(1f)) {
                        tabIndex = 0; isAuthSent = false
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    TabButton("비밀번호 찾기", tabIndex == 1, Modifier.weight(1f)) {
                        tabIndex = 1; isAuthSent = false
                    }
                }

                Spacer(modifier = Modifier.height(60.dp))

                if (tabIndex == 1) {
                    CustomOutlinedInput(value = idInput, onValueChange = { idInput = it }, placeholder = "ID")
                    Spacer(modifier = Modifier.height(12.dp))
                }
                CustomOutlinedInput(value = nameInput, onValueChange = { nameInput = it }, placeholder = "이름 입력")
                Spacer(modifier = Modifier.height(12.dp))
                CustomOutlinedInput(value = emailInput, onValueChange = { emailInput = it }, placeholder = "이메일 입력")
                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically)  {
                    CustomOutlinedInput(value = authCodeInput, onValueChange = { authCodeInput = it }, placeholder = "인증번호 6자리", modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = { isAuthSent = true },
                        modifier = Modifier.height(55.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CloverGreen),
                        shape = RoundedCornerShape(5.dp),
                        border = BorderStroke(1.dp, Color.Black)
                    ) {
                        Text(if (isAuthSent) "재인증" else "인증받기", color = Color.Black, fontSize = 12.sp)
                    }
                }

                // 인증 버튼을 누른 후에만 메시지 표시
                if (isAuthSent) {
                    Text("인증번호가 오지 않았습니까?", color = Color.Red, fontSize = 11.sp, modifier = Modifier.padding(top = 8.dp))
                }

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        if (tabIndex == 0) showIdResult = true
                        else navController.navigate("reset_password")
                    },
                    modifier = Modifier.fillMaxWidth().height(62.dp).padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CloverGreen),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color.Black)
                ) { Text("확인", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold) }
                Spacer(Modifier.height(200.dp))

            } else {
                // 아이디 알려주는 창 (결과 화면)
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text("ID", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(50.dp).border(1.dp, Color.Gray).padding(vertical = 13.dp), contentAlignment = Alignment.Center) {
                        Text("user_id_example", fontSize = 16.sp) // 실제 아이디 데이터 바인딩 지점
                    }
                    Text("비밀번호를 잊으셨나요?", color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 10.dp).clickable {
                        showIdResult = false; tabIndex = 1
                    })
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { navController.navigate("login") },
                        modifier = Modifier.fillMaxWidth().height(62.dp).padding(bottom = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CloverGreen),
                        shape = RoundedCornerShape(8.dp)
                    ) { Text("로그인으로", color = Color.Black, fontSize = 28.sp, fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}

// 4. 새 비밀번호 입력 화면 (수정 완료)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(navController: NavController) {
    var newPw by remember { mutableStateOf("") }
    var confirmPw by remember { mutableStateOf("") }
    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
                title = { Text("새 비밀번호 입력", fontSize = 28.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Image(painter = painterResource(id = R.drawable.back), contentDescription = null, modifier = Modifier.size(24.dp))
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp)) {
            Spacer(modifier = Modifier.height(60.dp))
            CustomOutlinedInput(value = newPw, onValueChange = { newPw = it }, placeholder = "새 password")
            Spacer(modifier = Modifier.height(15.dp))
            CustomOutlinedInput(value = confirmPw, onValueChange = { confirmPw = it }, placeholder = "새 password 재입력")

            Box(modifier = Modifier.fillMaxWidth().padding(top = 20.dp), contentAlignment = Alignment.Center) {
                Text("아이디를 잊으셨나요?", color = Color.Red, fontSize = 12.sp, modifier = Modifier.clickable {
                    navController.navigate("find_id_pw")
                }, textAlign = TextAlign.Center, textDecoration = TextDecoration.Underline)
            }

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { navController.navigate("login") { popUpTo("login") { inclusive = true } } },
                modifier = Modifier.fillMaxWidth().height(60.dp).padding(bottom = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CloverGreen),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(1.dp, Color.Black)
            ) { Text("로그인으로", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold) }
        }
    }
}

// 5. 회원가입 약관 (배경색 수정)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinTerms(navController: NavController) {
    var term1 by remember { mutableStateOf(false) }
    var term2 by remember { mutableStateOf(false) }
    var term3 by remember { mutableStateOf(false) }
    val isAllChecked = term1 && term2 && term3

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
                title = { Text("약관 동의", fontSize = 28.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Image(painter = painterResource(id = R.drawable.back), contentDescription = null, modifier = Modifier.size(24.dp))
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(20.dp))
            Column(modifier = Modifier.padding(vertical = 0.dp, horizontal = 40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.frame_31), contentDescription = null, modifier = Modifier.size(200.dp))
                Text("회원님 환영합니다!", fontWeight = FontWeight.Bold, fontSize = 28.sp)
            }
            Spacer(modifier = Modifier.height(40.dp))
            CustomCheckBoxRow("약관 전체 동의", isAllChecked, { val t = !isAllChecked; term1 = t; term2 = t; term3 = t })
            HorizontalDivider(modifier = Modifier.padding(vertical = 15.dp), thickness = 1.dp)
            CustomCheckBoxRow("이용약관 동의(필수)", term1, { term1 = it }, { navController.navigate("term_detail_1") })
            CustomCheckBoxRow("개인정보 수집 및 이용동의(필수)", term2, { term2 = it }, { navController.navigate("term_detail_2") })
            CustomCheckBoxRow("가입 시 알림 동의(선택)", term3, { term3 = it })
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { navController.navigate("join_detail") },
                modifier = Modifier.fillMaxWidth().height(55.dp).padding(bottom = 10.dp),
                enabled = term1 && term2,
                colors = ButtonDefaults.buttonColors(containerColor = CloverGreen, disabledContainerColor = Color(0xFFC8E6C9)),
                shape = RoundedCornerShape(8.dp)
            ) { Text("다음", color = Color.Black, fontWeight = FontWeight.Bold) }
        }
    }
}

// 회원가입 상세 (JoinDetail) 부분

@Composable
fun JoinDetail(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var id by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }
    var pwConfirm by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }

    // 중복 확인 상태: 0(기본), 1(성공/체크), 2(실패/빨간버튼)
    var idCheckStatus by remember { mutableIntStateOf(0) }
    // 이메일 중복 경고 표시 여부
    var isEmailDuplicate by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .padding(horizontal = 35.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))
            // 로고 이미지
            Image(
                painter = painterResource(id = R.drawable.frame_31),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))

            // 이름 입력
            CustomOutlinedInput(value = name, onValueChange = { name = it }, placeholder = "이름")
            Spacer(modifier = Modifier.height(10.dp))

            // ID 입력 및 중복 확인 버튼 섹션
            Row(verticalAlignment = Alignment.CenterVertically) {
                CustomOutlinedInput(
                    value = id,
                    onValueChange = { id = it; idCheckStatus = 0 },
                    placeholder = "ID",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))

                // 상태에 따른 버튼/아이콘 변화 로직
                when (idCheckStatus) {
                    1 -> { // 성공: 초록색 체크 표시
                        Image(
                            painter = painterResource(id = R.drawable.check),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    2 -> { // 실패: 빨간색 중복 버튼
                        Button(
                            onClick = { idCheckStatus = 1 }, // 테스트용: 클릭 시 성공으로 변경
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B6B)),
                            contentPadding = PaddingValues(horizontal = 12.dp),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.height(40.dp)
                        ) { Text("중복", color = Color.White, fontSize = 12.sp) }
                    }
                    else -> { // 기본: 중복 확인 버튼
                        Button(
                            onClick = { idCheckStatus = 2 }, // 테스트용: 클릭 시 실패로 변경
                            colors = ButtonDefaults.buttonColors(containerColor = CloverGreen),
                            contentPadding = PaddingValues(horizontal = 10.dp),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.height(40.dp)
                        ) { Text("중복 확인", color = Color.Black, fontSize = 11.sp) }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            CustomOutlinedInput(value = pw, onValueChange = { pw = it }, placeholder = "password")
            Spacer(modifier = Modifier.height(10.dp))
            CustomOutlinedInput(value = pwConfirm, onValueChange = { pwConfirm = it }, placeholder = "password 재입력")
            Spacer(modifier = Modifier.height(10.dp))
            // 이메일 입력 (테스트를 위해 "test" 입력 시 중복 메시지 출력)
            CustomOutlinedInput(
                value = email,
                onValueChange = { email = it; isEmailDuplicate = (it == "test") },
                placeholder = "이메일"
            )
            Spacer(modifier = Modifier.height(10.dp))
            CustomOutlinedInput(value = nickname, onValueChange = { nickname = it }, placeholder = "닉네임")

            Spacer(modifier = Modifier.height(60.dp))

            Spacer(modifier = Modifier.height(50.dp))

// "이미 계정이 있으신가요?" 텍스트 버튼으로 변경
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "이미 계정이 있으신가요?",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = "로그인",
                    fontSize = 14.sp,
                    color = Color.Blue, // 강조를 위해 파란색 적용
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable {
                            // 로그인 화면으로 이동하며 백스택 정리
                            navController.navigate("login") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                )
            }

            // 이메일 중복 시 나타나는 메시지
            if (isEmailDuplicate) {
                Text(
                    text = "중복 된 이메일 입니다.",
                    color = Color.Red,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            // 완료 버튼
            Button(
                onClick = { navController.navigate("login") },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CloverGreen),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Text("완료", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

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

@Composable
fun TabButton(text: String, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(45.dp),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = if (isSelected) Color(0xFFE8F5E9) else Color.White),
        border = BorderStroke(1.dp, if (isSelected) CloverGreen else Color.Gray)
    ) { Text(text, color = Color.Black, fontSize = 13.sp) }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermDetailScreen(navController: NavController, title: String) {
    Scaffold(
        containerColor = Color.White,
        topBar = { CenterAlignedTopAppBar(colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White), title = { Text("약관 동의") }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Image(painter = painterResource(id = R.drawable.back), contentDescription = null, modifier = Modifier.size(28.dp)) } }) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            Box(modifier = Modifier.fillMaxWidth().weight(1f).border(1.dp, Color.LightGray).padding(16.dp)) { Text("상세 내용...") }
            Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth().height(55.dp), colors = ButtonDefaults.buttonColors(containerColor = CloverGreen)) { Text("확인", color = Color.Black) }
        }
    }
}