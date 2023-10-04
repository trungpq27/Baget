package com.mobile.expenseapp.presentation.welcome_screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.javalon.xpensewhiz.presentation.welcome_screen.WelcomeViewModel

@Composable
fun WelcomeScreen(
    navController: NavController,
    welcomeViewModel: WelcomeViewModel = hiltViewModel()
) {
}