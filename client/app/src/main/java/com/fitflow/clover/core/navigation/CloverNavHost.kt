package com.fitflow.clover.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fitflow.clover.presentation.splash.SplashScreen
import com.fitflow.clover.presentation.auth.*

@Composable
fun CloverNavHost(navController: NavHostController) {
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