package com.mobile.expenseapp.presentation.navigation.components

import com.mobile.expenseapp.R
import com.mobile.expenseapp.presentation.navigation.Screen

data class NavBarItemHolder(
    val title: String,
    val icon: Int,
    val route: String
)

fun provideBottomNavItems() = listOf(
    NavBarItemHolder(
        "",
        R.drawable.main_home,
        Screen.HomeScreen.route
    ),
    NavBarItemHolder(
        "",
        R.drawable.donut,
        Screen.InsightScreen.route
    ),
    NavBarItemHolder(
        "",
        R.drawable.account,
        Screen.AccountScreen.route
    ),
    NavBarItemHolder(
        "",
        R.drawable.setting,
        Screen.SettingScreen.route
    )
)