package com.mobile.expenseapp.presentation.navigation.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@ExperimentalAnimationApi
@Composable
fun BottomNavBar(
    items: List<NavBarItemHolder>,
    navController: NavController,
    itemClick: (NavBarItemHolder) -> Unit
) {
}