package com.mobile.expenseapp.presentation.navigation

sealed class Screen(val route: String) {
    object WelcomeScreen: Screen("welcome")
    object HomeScreen: Screen("home")
    object InsightScreen: Screen("insight")
    object AccountScreen: Screen("account")
    object SettingScreen: Screen("setting")
}