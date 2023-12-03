package com.mobile.expenseapp.presentation.insight_screen.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mobile.expenseapp.presentation.home_screen.TransactionType
import com.mobile.expenseapp.presentation.insight_screen.InsightViewModel
import com.mobile.expenseapp.presentation.ui.theme.GreenAlpha700
import com.mobile.expenseapp.presentation.ui.theme.Red500
import com.mobile.expenseapp.util.spacing

@Composable
fun InsightTabBar(
    cornerRadius: Dp = 24.dp,
    insightViewModel: InsightViewModel = hiltViewModel()
) {
    val selectedTab by insightViewModel.tabButton.collectAsState()
    Surface(
        color = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(cornerRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            InsightBar(
                modifier = Modifier.weight(1f),
                cornerRadius = cornerRadius,
                onTabClick = {
                    insightViewModel.selectTabButton(TransactionType.INCOME)
                },
                title = "Income",
                backgroundColor = animateColorAsState(
                    targetValue = if (selectedTab == TransactionType.INCOME)
                        MaterialTheme.colors.primary else Color.Transparent
                ).value,
                textColor = if (selectedTab == TransactionType.INCOME)
                    Color.Black else Color.White
            )

            InsightBar(
                modifier = Modifier.weight(1f),
                cornerRadius = cornerRadius,
                onTabClick = {
                    insightViewModel.selectTabButton(TransactionType.EXPENSE)
                },
                title = "Expense",
                backgroundColor = animateColorAsState(
                    targetValue = if (selectedTab == TransactionType.EXPENSE)
                        MaterialTheme.colors.primary else Color.Transparent
                ).value,
                textColor = if (selectedTab == TransactionType.EXPENSE)
                    Color.Black else Color.White
            )
        }
    }
}

@Composable
fun InsightBar(
    modifier: Modifier,
    cornerRadius: Dp,
    onTabClick: () -> Unit,
    title: String,
    backgroundColor: Color,
    textColor: Color
) {
    TextButton(
        onClick = onTabClick,
        modifier = modifier
            .padding(vertical = MaterialTheme.spacing.extraSmall),
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = backgroundColor,
            contentColor = textColor
        )
    ) {
        Text(
            text = title,
            color = textColor,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier
                .padding(
                    horizontal = MaterialTheme.spacing.small,
                    vertical = MaterialTheme.spacing.extraSmall
                )
                .align(Alignment.CenterVertically)
        )
    }
}