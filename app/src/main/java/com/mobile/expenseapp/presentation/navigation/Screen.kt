package com.mobile.expenseapp.presentation.navigation

sealed class Screen(val route: String) {
    object WelcomeScreen: Screen("welcome")
    object SignInScreen: Screen("login")
    object SignUpScreen: Screen("register")

    object CurrencyScreen: Screen("currency")
    object HomeScreen: Screen("home")
    object TransactionScreen: Screen("transaction")
    object CategoryChooserScreen: Screen("category_chooser")
    object AccountChooserScreen: Screen("account_chooser")
    object InsightScreen: Screen("insight")
    object AccountScreen: Screen("account")
    object AccountDetailScreen: Screen("detail")
    object SettingScreen: Screen("setting")
}