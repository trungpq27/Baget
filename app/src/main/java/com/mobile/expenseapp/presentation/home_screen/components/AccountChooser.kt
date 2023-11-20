package com.mobile.expenseapp.presentation.home_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow
import com.mobile.expenseapp.R
import com.mobile.expenseapp.presentation.home_screen.Account
import com.mobile.expenseapp.presentation.home_screen.Category
import com.mobile.expenseapp.presentation.home_screen.HomeViewModel
import com.mobile.expenseapp.util.spacing

@ExperimentalUnitApi
@Composable
fun AccountChooser(
//    account: Account,
    navController: NavController,
    accountItems: Array<Account> = Account.values()
) {
    Column(
//        crossAxisSpacing = MaterialTheme.spacing.small,
        modifier = Modifier.padding(
            start = MaterialTheme.spacing.medium,
            top = MaterialTheme.spacing.medium,
            bottom = MaterialTheme.spacing.medium,
        ),
    ) {
        accountItems.forEach {
            AccountTag(account = it, navController = navController)
        }
    }
}