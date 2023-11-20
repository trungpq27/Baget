package com.mobile.expenseapp.presentation.account_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobile.expenseapp.R
import com.mobile.expenseapp.domain.model.Account
import com.mobile.expenseapp.presentation.home_screen.amountFormat
import com.mobile.expenseapp.util.spacing
import com.mobile.expenseapp.presentation.home_screen.Account as AccountType

@ExperimentalMaterialApi
@Composable
fun AccountItem(account: Account, currency: String, navController: NavController, onItemClick: (String) -> Unit) {
    Card(
        onClick = {
            onItemClick(account.account)
        },
//        backgroundColor = Color.DarkGray.copy(alpha = 0.1f),
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = MaterialTheme.spacing.medium,
                start = MaterialTheme.spacing.medium,
                end = MaterialTheme.spacing.medium
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = MaterialTheme.spacing.medium
                )
        ) {
            // Account icon and name
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = "AccountName",
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 22.sp
                    ),
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.padding(
                        start = MaterialTheme.spacing.medium
                    )
                )

//                Spacer(modifier = Modifier.height(8.dp))

            }

            Spacer(modifier = Modifier.height(25.dp))

            val color = when (account.account) {
                AccountType.CASH.title -> AccountType.CASH.color
                AccountType.CARD.title -> AccountType.CARD.color
                else -> AccountType.BANK.color
            }

            // Type of account and Balance
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = MaterialTheme.spacing.medium
                    )
            ) {
                Text(
                    text = account.account
                )


                Text(text = buildAnnotatedString {
//                    withStyle(
//                        SpanStyle(
//                            fontWeight = FontWeight.SemiBold,
//                            fontSize = 12.sp,
//                            letterSpacing = 0.4.sp
//                        )
//                    ) {
//                        append(currency)
//                    }
                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.ExtraLight,
                            fontSize = 22.sp,
                            letterSpacing = 0.25.sp
                        )
                    ) {
                        append(account.amount.toString().amountFormat())
                    }
                }, modifier = Modifier.padding(
//                    start = MaterialTheme.spacing.medium,
//                    end = MaterialTheme.spacing.medium
                            MaterialTheme.spacing.medium
                    )
                )
            }

//            Surface(
////                color = color,
//                modifier = Modifier
//                    .fillMaxWidth()
//            ) {
//                Column(verticalArrangement = Arrangement.Center) {
//                    Row(
//                        horizontalArrangement = Arrangement.Center,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = MaterialTheme.spacing.small)
//                    ) {
//                        Text(
//                            text = account.account,
//                            style = MaterialTheme.typography.caption,
//                            color = contentColorFor(backgroundColor = MaterialTheme.colors.primary)
//                        )
//                    }
//                    Row(
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = MaterialTheme.spacing.medium)
//                    ) {
//                        Text(text = buildAnnotatedString {
//                            withStyle(
//                                SpanStyle(
//                                    fontWeight = FontWeight.Normal,
//                                    fontSize = 10.sp,
//                                    letterSpacing = 0.4.sp,
//                                    color = contentColorFor(backgroundColor = MaterialTheme.colors.primary)
//                                )
//                            ) {
//                                append(currency)
//                            }
//                            withStyle(
//                                SpanStyle(
//                                    fontWeight = FontWeight.Thin,
//                                    fontSize = 18.sp,
//                                    letterSpacing = 0.25.sp,
//                                            color = contentColorFor(backgroundColor = MaterialTheme.colors.primary)
//                                )
//                            ) {
//                                append(account.income.toString().amountFormat())
//                            }
//                        })
//
//                        Text(text = buildAnnotatedString {
//                            withStyle(
//                                SpanStyle(
//                                    fontWeight = FontWeight.Thin,
//                                    fontSize = 10.sp,
//                                    letterSpacing = 0.4.sp,
//                                    color = contentColorFor(backgroundColor = MaterialTheme.colors.primary)
//                                )
//                            ) {
//                                append(currency)
//                            }
//                            withStyle(
//                                SpanStyle(
//                                    fontWeight = FontWeight.Thin,
//                                    fontSize = 18.sp,
//                                    letterSpacing = 0.25.sp,
//                                    color = contentColorFor(backgroundColor = MaterialTheme.colors.primary)
//                                )
//                            ) {
//                                append(account.expense.toString().amountFormat())
//                            }
//                        })
//                    }
//
//                    Row(
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = MaterialTheme.spacing.medium)
//                    ) {
//                        Text(
//                            text = "INCOME",
//                            style = MaterialTheme.typography.overline,
//                            color = contentColorFor(backgroundColor = MaterialTheme.colors.primary)
//                        )
//
//                        Text(
//                            text = "EXPENSE",
//                            style = MaterialTheme.typography.overline,
//                            color = contentColorFor(backgroundColor = MaterialTheme.colors.primary)
//                        )
//                    }
//                }
//            }
        }
    }
}