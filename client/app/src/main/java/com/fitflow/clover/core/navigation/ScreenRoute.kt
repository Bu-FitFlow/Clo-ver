package com.fitflow.clover.core.navigation

sealed class ScreenRoute {
    data object PersonalColorQuestion : ScreenRoute()
    data object PersonalColorResult : ScreenRoute()
    data object PersonalColorRetry : ScreenRoute()
    data object Main : ScreenRoute()
}