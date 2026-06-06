package com.fitflow.clover.mypage.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable // 🎯 클릭 기능을 위해 필수 추가
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable // 🎯 composable 인식을 위해 필수 추가
import androidx.navigation.compose.rememberNavController
import com.fitflow.clover.R
import com.fitflow.clover.mypage.mainscreen.MyPageScreen
import com.fitflow.clover.mypage.mainscreen.MyWriting
import com.fitflow.clover.mypage.MyPageViewModel
import com.fitflow.clover.presentation.diagnosis.DiagnosisUiState


// 💡 화면들의 이동 주소 정의
object MyPageDestinations {
    const val MYPAGE_SCREEN = "mypage_screen"
    const val MY_WRITING = "my_writing"

    const val GOODS = "goods"
    const val SETUP = "setup"
    const val ACCOUNT_PROFILE = "account_profile"
    const val ACCOUNT_PROFILE_MODIFY = "account_profile_modify"
    const val ACCOUNT_PASSWORD = "account_password"
    const val MYPROFILE = "myprofile"
    const val MYPROFILE_MODIFY = "myprofile_modify"
    const val NOTIFICATION_PUSH = "notification_push"

    const val PERSONAL_INFORMATION= "personal_information"

}

// 💡 마이페이지 화면 이동을 총괄하는 네비게이션 호스트
@Composable
fun MyPageNavHost(
    onExitMyPage: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onGoodsClick: () -> Unit,
    diagnosisUiState: DiagnosisUiState = DiagnosisUiState(),
    viewModel: MyPageViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMyPage(context)
    }

    LaunchedEffect(
        diagnosisUiState.selectedHeightCm,
        diagnosisUiState.selectedWeightKg,
        diagnosisUiState.bodyResult,
        diagnosisUiState.personalColorResult
    ) {
        viewModel.syncDiagnosisState(diagnosisUiState)
    }

    NavHost(navController = navController, startDestination = MyPageDestinations.MYPAGE_SCREEN) {
        composable(MyPageDestinations.MYPAGE_SCREEN) {
            MyPageScreen(
                uiState = uiState,
                onSettingsClick = { navController.navigate(MyPageDestinations.SETUP) },
                onMyWritingClick = { navController.navigate(MyPageDestinations.MY_WRITING) },
                onGoodsClick = onGoodsClick,
                onBackClick = onExitMyPage
            )
        }

        composable(MyPageDestinations.MY_WRITING) {
            MyWriting(
                navController = navController,
                posts = uiState.myPosts
            )
        }
        composable(MyPageDestinations.SETUP) {
            MyPageSetup(
                navController = navController,
                onLogoutOrWithdraw = onNavigateToLogin
            )
        }
        composable(MyPageDestinations.ACCOUNT_PROFILE) {
            MyPageAccountProfile(
                navController = navController,
                uiState = uiState
            )
        }
        composable(MyPageDestinations.ACCOUNT_PROFILE_MODIFY) {
            MyPageAccountProfileModify(
                navController = navController,
                uiState = uiState,
                onSaveNickname = { nickname ->
                    viewModel.updateNickname(context, nickname)
                }
            )
        }
        composable(MyPageDestinations.ACCOUNT_PASSWORD) {
            MyPageAccountPassword(navController = navController)
        }
        composable(MyPageDestinations.MYPROFILE) {
            MyProfile(
                navController = navController,
                uiState = uiState
            )
        }
        composable(MyPageDestinations.MYPROFILE_MODIFY) {
            MyProfileModify(
                navController = navController,
                myPageUiState = uiState,
                onProfileSaved = { height, weight, personalColor, bodyType, profileImageUri ->
                    viewModel.updateProfileFromEdit(
                        heightLabel = height,
                        weightLabel = weight,
                        personalColorLabel = personalColor,
                        bodyTypeLabel = bodyType,
                        profileImageUri = profileImageUri
                    )
                }
            )
        }
        composable(MyPageDestinations.NOTIFICATION_PUSH) {
            NotificationPush(navController = navController)
        }

        composable(MyPageDestinations.PERSONAL_INFORMATION) {
            PersonalInformation(navController = navController)
        }
    }
}


@Composable
fun MyPageSetup(navController: NavController,
                onLogoutOrWithdraw: () -> Unit) { // 🎯 1. 괄호 안에 navController를 받도록 함


    @Suppress("AssignedValueIsNeverRead", "UnusedChangedValue") // 🔥 검사기 입 막기
    var showWithdrawDialog by remember { mutableStateOf(false) }

    var showLogoutDialog by remember { mutableStateOf(false) }

    /*
     // 팝업창을 띄울지 말지 결정하는 "상태" 정의 (처음엔 닫힘 상태)
     var showWithdrawDialog by remember { mutableStateOf(false) }
     var showLogoutDialog by remember { mutableStateOf(false) }
     */

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 상단바
            SetupTopBar()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp), // 일관된 양옆 패딩 부여
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.kakaotalk_20260514_111630855),
                        contentDescription = "설정 아이콘",
                        modifier = Modifier
                            .size(28.dp)
                            .align(Alignment.CenterStart)
                            .clickable { navController.popBackStack()}
                    )

                    Text(
                        text = "설정",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // 1. 내 계정
                SettingSectionHeader(text = "내 계정")
                SettingTextItem(
                    text = "내 정보",
                    onClick = {navController.navigate(MyPageDestinations.MYPROFILE)}
                )
                SettingTextItem(
                    text = "계정 정보",
                    onClick = { navController.navigate(MyPageDestinations.ACCOUNT_PROFILE) }
                )
                SettingTextItem(
                    text = "비밀번호 재설정",
                    onClick = { navController.navigate(MyPageDestinations.ACCOUNT_PASSWORD) }
                )

                // 2. 알림 설정
                SettingSectionHeader(text = "알림 설정")
                SettingTextItem(
                    text = "푸쉬 알림 설정",
                    onClick = { navController.navigate(MyPageDestinations.NOTIFICATION_PUSH) }
                )

                // 3. 기타 진짜 버튼들
                SettingMenuButton(
                    text = "개인 정보 처리 방침",
                    onClick = { navController.navigate(MyPageDestinations.PERSONAL_INFORMATION)  }
                )
                SettingMenuButton(
                    text = "탈퇴하기",
                    onClick = { showWithdrawDialog = true }
                )
                SettingMenuButton(
                    text = "로그아웃",
                    onClick = { showLogoutDialog = true }
                )
            }
        }
    }

    // 상태가 true가 되면 화면 중앙에 팝업창을 그립니다.
    // 탈퇴 확인 팝업
    if (showWithdrawDialog) {
        AlertDialog(
            onDismissRequest = { showWithdrawDialog = false }, // 🌟 true -> false로 올바르게 수정!
            title = {
                Text(
                    text = "회원 탈퇴",
                    style = MaterialTheme.typography.headlineSmall // M3 규격 스타일 추가로 경고 방지
                )
            },
            text = {
                Text(
                    text = "정말 탈퇴하시겠습니까? 데이터가 모두 삭제됩니다.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showWithdrawDialog = false
                        // 💡 컴파일러에게 이 변수가 확실히 사용됨을 인지시키기 위해 로그 한 줄 추가
                        android.util.Log.d("UserPage", "회원 탈퇴 완료 상태: $showWithdrawDialog")
                        /* 실제 탈퇴 서버 통신 로직 수행 */
                        onLogoutOrWithdraw()
                    }
                ) {
                    Text("탈퇴", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWithdrawDialog = false }) {
                    Text("취소", color = Color.Black)
                }
            }
        )
    }

// 로그아웃 확인 팝업
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "로그아웃",
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Text(
                    text = "로그아웃 하시겠습니까?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        // 💡 마찬가지로 변수 사용 인식을 위한 임시 로그 추가
                        android.util.Log.d("UserPage", "로그아웃 완료 상태: $showLogoutDialog")
                        /* 로그아웃 처리 후 로그인 화면 등으로 이동 */
                        onLogoutOrWithdraw()
                    }
                ) {
                    Text("로그아웃")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("취소", color = Color.Black)
                }
            }
        )
    }


    /*
    // 탈퇴 확인 팝업
    if (showWithdrawDialog) {
        AlertDialog(
            onDismissRequest = { showWithdrawDialog = false }, // 바깥을 누르거나 취소하면 닫기
            title = {
                Text(text = "회원 탈퇴") },
            text = { Text(text = "정말 탈퇴하시겠습니까? 데이터가 모두 삭제됩니다.") },
            confirmButton = {
                TextButton(onClick = {
                    showWithdrawDialog = false
                    /* 실제 탈퇴 서버 통신 로직 수행 */
                }) {
                    Text("탈퇴", color = Color.Red) // 탈퇴는 경고의 의미로 빨간색
                }
            },
            dismissButton = {
                TextButton(onClick = { showWithdrawDialog = false }) {
                    Text("취소", color = Color.Black)
                }
            }
        )
    } // if절 (showWithdrawDialog)

    // 로그아웃 확인 팝업
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text(text = "로그아웃") },
            text = { Text(text = "로그아웃 하시겠습니까?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    /* 로그아웃 처리 후 로그인 화면 등으로 이동 */
                }) {
                    Text("로그아웃")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("취소", color = Color.Black)
                }
            }
        )
    } // if절 (showLogoutDialog)

     */


} // fun MyPageSetup {}

@Composable
fun SetupTopBar() {
    Box(
        modifier = Modifier
            .width(393.dp)  // 가로 사이즈
            .height(57.dp) // 세로 사이즈
            .background(Color.White) // 배경을 흰색으로 채움
    ) {
    }
}



// 누를 수 없는 대분류 타이틀 전용 컴포넌트
@Composable
fun SettingSectionHeader(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(vertical = 4.dp) // 1. [바깥 여백] 먼저 이 박스 위아래로 8dp만큼 공간을 비워둡니다. (다른 컴포넌트와 안 겹치게)
            // 2. [그리기] 비워둔 상태에서 흰색 배경을 칠하고 검은색 테두리를 그립니다.
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .padding(vertical = 10.dp, horizontal = 16.dp), // 3. [안쪽 여백] 테두리가 다 그려진 '내부'에서 글자가 벽에 바짝 붙지 않도록 여백을 줍니다.
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

// 테두리가 있는 버튼 모양을 만드는 커스텀 컴포넌트
@Composable
fun SettingMenuButton(
    text: String,
    onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(48.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        // 💡 핵심: 버튼 안을 꽉 채우는 투명한 정렬 상자(Box)를 만듭니다.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // 글자가 왼쪽 테두리 벽면에 바짝 붙지 않도록 안쪽 여백 주기
            contentAlignment = Alignment.CenterStart // 👈 내부 요소를 왼쪽 가운데로 정렬하는 치트키!
        ) {
            Text(
                text = text,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        //Text(text = text, color = Color.Black)
    }
}

// 테두리가 없는 일반 텍스트 메뉴 (🎯 3. onClick을 받아 클릭 가능하게 구조 확장)
@Composable
fun SettingTextItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 16.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp) // 누르기 편하게 여백 확보
            .clickable { onClick() } // 터치하면 지정한 액션 수행
    )
}

@Preview(showBackground = true, device = "spec:width=393dp,height=852dp")
@Composable
fun MyPageSetupPreview() {
    // 미리보기에서도 전체 화면 흐름을 안전하게 볼 수 있도록 Host를 띄워줍니다.
    MyPageNavHost(onExitMyPage = {},
        onNavigateToLogin = {},
        onGoodsClick = {})
}
