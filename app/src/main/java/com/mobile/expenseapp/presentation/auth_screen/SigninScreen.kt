package com.mobile.expenseapp.presentation.account_screen.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mobile.expenseapp.R
import com.mobile.expenseapp.presentation.navigation.Screen
import com.mobile.expenseapp.presentation.welcome_screen.WelcomeViewModel

@ExperimentalMaterialApi
@Composable
fun SignInScreen(
    navController: NavController,
    welcomeViewModel: WelcomeViewModel = hiltViewModel()
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(20.dp)
        ) {
            val username = remember { mutableStateOf(TextFieldValue()) }
            val password = remember { mutableStateOf(TextFieldValue()) }

            Text(
                text = "Login",
                style = TextStyle(
                    fontSize = 36.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                label = { Text(text = "Username or email") },
                value = username.value,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { username.value = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                label = { Text(text = "Password") },
                value = password.value,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { password.value = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        navController.popBackStack()
                        welcomeViewModel.saveCurrencyLocale()
                        welcomeViewModel.saveOnBoardingState(completed = true)
                        welcomeViewModel.createAccounts()
                        navController.navigate(Screen.HomeScreen.route)
                    },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Text(
                        text = "Sign in",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily.Default
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 8.dp),
                    color = Color.Gray,
                    thickness = 1.dp
                )

                Text(
                    modifier = Modifier.padding(
                        start = 8.dp,
                        end = 8.dp
                    ),
                    text = "OR SIGN IN WITH",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Default
                    ),
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 8.dp),
                    color = Color.Gray,
                    thickness = 1.dp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { },
                    shape = RoundedCornerShape(40.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_google_logo),
                            contentDescription = "Google Button",
                            tint = Color.Unspecified
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Sign in with Google",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontFamily = FontFamily.Default
                            )
                        )
                    }
                }
            }
        }
    }
}