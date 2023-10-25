package com.mobile.expenseapp.presentation.welcome_screen.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mobile.expenseapp.R

sealed class OnBoardingPage(
    @DrawableRes
    val icon: Int,
    @StringRes
    val title: Int,
    @StringRes
    val description: Int
) {

    object FirstPage : OnBoardingPage(
        R.drawable.entry,
        R.string.welcome_title_first_page,
        R.string.welcome_content_first_page
    )

    object SecondPage : OnBoardingPage(
        R.drawable.insight,
        R.string.welcome_title_second_page,
        R.string.welcome_content_second_page
    )

    object ThirdPage : OnBoardingPage(
        R.drawable.decision,
        R.string.welcome_title_third_page,
        R.string.welcome_content_third_page
    )
}