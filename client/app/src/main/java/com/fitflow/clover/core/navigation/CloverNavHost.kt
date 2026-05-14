package com.fitflow.clover.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.fitflow.clover.presentation.diagnosis.personalcolor.PersonalColorMoveResult
import com.fitflow.clover.presentation.diagnosis.personalcolor.PersonalColorQuestionScreen
import com.fitflow.clover.presentation.diagnosis.personalcolor.PersonalColorResultScreen
import com.fitflow.clover.presentation.diagnosis.personalcolor.PersonalColorRetryScreen
import com.fitflow.clover.presentation.diagnosis.personalcolor.PersonalColorViewModel
import com.fitflow.clover.presentation.main.MainScreen

@Composable
fun CloverNavHost() {
    val currentScreen = remember {
        mutableStateOf<ScreenRoute>(ScreenRoute.PersonalColorQuestion)
    }

    val personalColorViewModel = remember {
        PersonalColorViewModel()
    }

    when (currentScreen.value) {
        ScreenRoute.PersonalColorQuestion -> {
            PersonalColorQuestionScreen(
                viewModel = personalColorViewModel,
                onMoveToResult = {
                    currentScreen.value = ScreenRoute.PersonalColorResult
                },
                onMoveToRetry = {
                    currentScreen.value = ScreenRoute.PersonalColorRetry
                },
                onMoveToMain = {
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        ScreenRoute.PersonalColorResult -> {
            PersonalColorResultScreen(
                result = personalColorViewModel.uiState.value.result,
                onMoveToMain = {
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        ScreenRoute.PersonalColorRetry -> {
            PersonalColorRetryScreen(
                onRetry = {
                    personalColorViewModel.reset()
                    currentScreen.value = ScreenRoute.PersonalColorQuestion
                },
                onMoveToMain = {
                    currentScreen.value = ScreenRoute.Main
                }
            )
        }

        ScreenRoute.Main -> {
            MainScreen()
        }
    }
}