package com.mobile.expenseapp.presentation.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mobile.expenseapp.R
import com.mobile.expenseapp.presentation.account_screen.AccountDetailScreen
import com.mobile.expenseapp.presentation.account_screen.AccountScreen
import com.mobile.expenseapp.presentation.account_screen.components.SignInScreen
import com.mobile.expenseapp.presentation.auth_screen.SignUpScreen
import com.mobile.expenseapp.presentation.home_screen.AccountChooserScreen
import com.mobile.expenseapp.presentation.home_screen.HomeScreen
import com.mobile.expenseapp.presentation.home_screen.TransactionScreen
import com.mobile.expenseapp.presentation.home_screen.CategoryChooserScreen
import com.mobile.expenseapp.presentation.insight_screen.InsightScreen
import com.mobile.expenseapp.presentation.setting_screen.SettingScreen
import com.mobile.expenseapp.presentation.welcome_screen.CurrencyScreen
import com.mobile.expenseapp.presentation.welcome_screen.WelcomeScreen

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalUnitApi
@ExperimentalPagerApi
@Composable
fun MainNavigation(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = Screen.SignInScreen.route){
            SignInScreen(navController = navController)
        }
        composable(route = Screen.SignUpScreen.route){
            SignUpScreen(navController = navController)
        }
        composable(route = Screen.WelcomeScreen.route) {
            WelcomeScreen(navController = navController)
        }

        composable(route = "${Screen.CurrencyScreen.route}/{setting}",
            arguments = listOf(
                navArgument("setting") {
                    type = NavType.BoolType
                    defaultValue = true
                }
            )
        ) { entry ->
            CurrencyScreen(
                navController = navController,
                setting = entry.arguments?.getBoolean("setting")
            )
        }
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(route = "${Screen.TransactionScreen.route}/{tag}?trxKey={trxKey}&trxPos={trxPos}&trxStatus={trxStatus}",
            arguments = listOf(
                navArgument("tag") {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument("trxKey") {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                },
                navArgument("trxPos") {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument("trxStatus") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { entry ->
            TransactionScreen(
                navController = navController,
                transactionTag = entry.arguments?.getInt("tag"),
                transactionDate = entry.arguments?.getString("trxKey"),
                transactionPos = entry.arguments?.getInt("trxPos"),
                transactionStatus = entry.arguments?.getInt("trxStatus")
            )
        }
        composable(route = Screen.CategoryChooserScreen.route) {
            CategoryChooserScreen(navController)
        }
        composable(route = Screen.AccountChooserScreen.route) {
            AccountChooserScreen(navController)
        }
        composable(route = Screen.InsightScreen.route) {
            InsightScreen(navController = navController)
        }
        composable(route = Screen.AccountScreen.route) {
            AccountScreen(navController = navController)
        }
        composable(
            route = "${Screen.AccountDetailScreen.route}/{accountType}",
            arguments = listOf(
                navArgument("accountType") {
                    type = NavType.StringType
                    defaultValue = navController.context.getString(R.string.pay_with_cash)
                    nullable = true
                })
        ) { entry ->
            AccountDetailScreen(entry.arguments?.getString("accountType"),navController)
        }
        composable(route = Screen.SettingScreen.route) {
            SettingScreen(navController = navController)
        }
    }
}