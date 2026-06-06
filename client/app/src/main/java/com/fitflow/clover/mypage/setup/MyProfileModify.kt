package com.fitflow.clover.mypage.setup

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.fitflow.clover.R
import com.fitflow.clover.mypage.MyPageUiState

@Composable
fun MyProfileModify(
    navController: NavHostController,
    myPageUiState: MyPageUiState = MyPageUiState(),
    onProfileSaved: (String, String, String, String, String?) -> Unit = { _, _, _, _, _ -> },
    viewModel: MyProfileModifyViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(myPageUiState) {
        viewModel.setInitialProfile(
            imageUri = myPageUiState.profileImageModel,
            height = myPageUiState.heightLabel,
            weight = myPageUiState.weightLabel,
            personalColor = myPageUiState.personalColorLabel,
            bodyType = myPageUiState.bodyTypeLabel
        )
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.onImageSelected(uri)
        }
    }

    val heightOptions = (140..200 step 5).map { "${it}cm" }
    val weightOptions = (40..120 step 2).map { "${it}kg" }
    val personalColorOptions = listOf("봄 [웜톤]", "여름 [쿨톤]", "가을 [웜톤]", "겨울 [쿨톤]")
    val bodyTypeOptions = listOf("마른 직선형", "사과형", "역삼각형", "배형", "모래시계형", "직사각형")

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MyProfileModifyTopBar()

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
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
                            .clickable { navController.popBackStack() }
                    )

                    Text(
                        text = "내 정보",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    Button(
                        onClick = {
                            val savedImageUri = viewModel.saveProfileData(context)
                            onProfileSaved(
                                uiState.height,
                                uiState.weight,
                                uiState.obesity,
                                uiState.faceShape,
                                savedImageUri
                            )
                            navController.popBackStack(
                                route = MyPageDestinations.MYPROFILE,
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

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .background(Color(0xFFE0E0E0))
                        .border(1.dp, Color.Gray)
                        .clickable {
                            galleryLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.imageUri == null) {
                        Text(
                            text = "사진 변경",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(model = uiState.imageUri),
                            contentDescription = "프로필 사진",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ProfileDropdownKeyBox(
                            label = "키",
                            selectedValue = uiState.height,
                            options = heightOptions,
                            onOptionSelected = { viewModel.onHeightSelected(it) },
                            modifier = Modifier.weight(1f)
                        )
                        ProfileDropdownKeyBox(
                            label = "몸무게",
                            selectedValue = uiState.weight,
                            options = weightOptions,
                            onOptionSelected = { viewModel.onWeightSelected(it) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    ProfileDropdownKeyBox(
                        label = "퍼스널 컬러",
                        selectedValue = uiState.obesity,
                        options = personalColorOptions,
                        onOptionSelected = { viewModel.onObesitySelected(it) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    ProfileDropdownKeyBox(
                        label = "체형",
                        selectedValue = uiState.faceShape,
                        options = bodyTypeOptions,
                        onOptionSelected = { viewModel.onFaceShapeSelected(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun MyProfileModifyTopBar() {
    Box(
        modifier = Modifier
            .width(393.dp)
            .height(57.dp)
            .background(Color.White)
    )
}

@Composable
fun ProfileDropdownKeyBox(
    label: String,
    selectedValue: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFCCCCCC), RoundedCornerShape(8.dp))
                .clickable { isExpanded = !isExpanded }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (selectedValue.contains("선택")) "$label 선택" else selectedValue,
                fontSize = 16.sp,
                color = if (selectedValue.contains("선택")) Color.Gray else Color.Black
            )
            Icon(
                painter = painterResource(id = android.R.drawable.arrow_down_float),
                contentDescription = "드롭다운 화살표",
                modifier = Modifier.size(12.dp),
                tint = Color.Gray
            )
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option, fontSize = 16.sp, color = Color.Black) },
                    onClick = {
                        onOptionSelected(option)
                        isExpanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=393dp,height=852dp")
@Composable
fun MyProfileModifyPreview() {
    MyProfileModify(navController = rememberNavController())
}
