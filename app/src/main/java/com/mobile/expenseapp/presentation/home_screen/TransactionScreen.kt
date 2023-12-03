package com.mobile.expenseapp.presentation.home_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mobile.expenseapp.R
import com.mobile.expenseapp.common.Constants
import com.mobile.expenseapp.presentation.home_screen.components.InfoBanner
import com.mobile.expenseapp.presentation.home_screen.components.KeypadComponent
import com.mobile.expenseapp.presentation.ui.theme.Red200
import com.mobile.expenseapp.util.spacing
import kotlinx.coroutines.launch
import java.util.Calendar

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalUnitApi
@ExperimentalMaterialApi
@Composable
fun TransactionScreen(
    navController: NavController,
    transactionTag: Int?,
    transactionDate: String?,
    transactionPos: Int?,
    transactionStatus: Int?,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val transactionType = TransactionType.values()[transactionTag!!]
    val scope = rememberCoroutineScope()
    val keypadBottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )
    val keyboardController = LocalSoftwareKeyboardController.current
    val title by remember { mutableStateOf(homeViewModel.transactionTitle) }
    val titleFieldValue = TextFieldValue(title.collectAsState().value)
    val showInfoBanner by homeViewModel.showInfoBanner.collectAsState()
    val expenseAmount by homeViewModel.transactionAmount.collectAsState()
    val currencyCode by homeViewModel.selectedCurrencyCode.collectAsState()
    val limitKey by homeViewModel.limitKey.collectAsState()
    val limitInfoWarning by homeViewModel.limitAlert.collectAsState(initial = HomeViewModel.UIEvent.NoAlert())

    BottomSheetScaffold(
        sheetContent = {
            KeypadComponent(
                bottomSheetScaffoldState = keypadBottomSheetState
            ) {
                homeViewModel.setTransaction(it)
            }
        },
        scaffoldState = keypadBottomSheetState,
        sheetPeekHeight = 0.dp,
        sheetContentColor = MaterialTheme.colors.background
    ) {
        LaunchedEffect(key1 = transactionPos) {
            if (transactionPos != -1) {
                homeViewModel.displayTransaction(transactionDate, transactionPos, transactionStatus)
            }
            homeViewModel.displayExpenseLimitWarning()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(bottom = it.calculateBottomPadding())
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = MaterialTheme.spacing.small,
                            end = MaterialTheme.spacing.medium,
                            top = MaterialTheme.spacing.small
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .scale(0.75f)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.remove),
                            contentDescription = "close",
                            tint = MaterialTheme.colors.onSurface,
                            modifier = Modifier
                                .scale(0.8f)
                        )
                    }

                    Text(
                        text = navController.context.getString(R.string.transaction_add_transaction),
                        modifier = Modifier.weight(2f),
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.onSurface
                    )

                    IconButton(
                        onClick = {
                            homeViewModel.apply {
                                if (transactionPos == -1) {
                                    setCurrentTime(Calendar.getInstance().time)
                                    if (transactionType == TransactionType.INCOME) {
                                        insertDailyTransaction(
                                            date.value,
                                            transactionAmount.value.toDouble(),
                                            category.value.title,
                                            Constants.INCOME, transactionTitle.value
                                        ) {
                                            navController.navigateUp()
                                        }
                                    } else {
                                        insertDailyTransaction(
                                            date.value,
                                            transactionAmount.value.toDouble(),
                                            category.value.title,
                                            Constants.EXPENSE, transactionTitle.value
                                        ) {
                                            navController.navigateUp()
                                        }
                                    }
                                } else {
                                    updateTransaction(
                                        transactionDate,
                                        transactionPos,
                                        transactionStatus
                                    ) {
                                        navController.navigateUp()
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .scale(0.8f)
                            .background(MaterialTheme.colors.surface, CircleShape)

                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.enter),
                            contentDescription = "enter",
                            tint = MaterialTheme.colors.onSurface,
                            modifier = Modifier
                                .scale(0.8f)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    // Make this the way to pick the transaction type, not the scaffold
//                    EntryTypePicker(transactionType = transactionType)

                    InfoBanner(shown = showInfoBanner, transactionType)

                    // Amount title
                    Text(
                        text = "Amount",
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier
                            .padding(
                                horizontal = MaterialTheme.spacing.medium,
                                vertical = MaterialTheme.spacing.small
                            )
                            .align(Alignment.Start)
                    )

                    // Amount number
                    TextButton(
                        onClick = {
                            scope.launch {
                                keyboardController?.hide()
                                if (keypadBottomSheetState.bottomSheetState.isCollapsed)
                                    keypadBottomSheetState.bottomSheetState.expand()
                                else keypadBottomSheetState.bottomSheetState.collapse()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Start)
                            .padding(
                                start = MaterialTheme.spacing.medium,
                                end = MaterialTheme.spacing.medium
                            ),
                        colors = ButtonDefaults.textButtonColors(MaterialTheme.colors.primary),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(
                            horizontal = MaterialTheme.spacing.medium,
                            vertical = MaterialTheme.spacing.small
                        ),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 12.dp
                        )
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontWeight = FontWeight.W300,
                                        fontSize = 24.sp
                                    )
                                ) {
                                    append(currencyCode)
                                    append(expenseAmount.amountFormat())
                                }
                            },
                            color = MaterialTheme.colors.onSurface,
                            modifier = Modifier
                                .padding(
                                    horizontal = MaterialTheme.spacing.medium,
                                    vertical = MaterialTheme.spacing.small
                                )
                        )
                    }

                    // Account type
                    Text(
//                        text = if (transactionType == TransactionType.INCOME) {
//                            "Fund"
//                        } else "Pay with",
                        text = "Account",
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier
                            .padding(
                                horizontal = MaterialTheme.spacing.medium,
                                vertical = MaterialTheme.spacing.small
                            )
                            .align(Alignment.Start),
                    )

                    // Dropdown account picker
                    var expandedAccountState by remember { mutableStateOf(false) }
                    var accountItems: Array<Account> = Account.values()
                    var selectedAccount by remember { mutableStateOf(Account.CARD) }


                    // Account picker
                    TextButton(
                        onClick = {
//                            navController.navigate(Screen.AccountChooserScreen.route)
                        },
                        modifier = Modifier
                            .align(Alignment.Start)
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .padding(
                                horizontal = MaterialTheme.spacing.medium,
                                vertical = MaterialTheme.spacing.small
                            ),
                        border = BorderStroke(1.dp, MaterialTheme.colors.primary),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(
                            horizontal = MaterialTheme.spacing.medium,
                            vertical = MaterialTheme.spacing.medium
                        ),
                    ) {
//                        Text(
//                            text = "Choose an account",
//                            textAlign = TextAlign.Start,
//                            color = MaterialTheme.colors.onSurface
//                        )
                        Box(
                            modifier = Modifier
                                .clickable {
                                    expandedAccountState = !expandedAccountState
                                }
                                .fillMaxSize()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = selectedAccount.iconRes
                                    ),
                                    contentDescription = selectedAccount.title,
                                )

                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                                Text(
                                    text = navController.context.getString(selectedAccount.content),
                                    style = MaterialTheme.typography.subtitle1
                                )
                            }

                            DropdownMenu(
                                expanded = expandedAccountState,
                                onDismissRequest = { expandedAccountState = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                accountItems.forEachIndexed { index, account ->
                                    DropdownMenuItem(
                                        onClick = {
                                            homeViewModel.selectAccount(account)
                                            expandedAccountState = false
                                            selectedAccount = account
                                        },
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            Icon(
                                                painter = painterResource(
                                                    id = account.iconRes
                                                ),
                                                contentDescription = account.title,
                                            )
                                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                                            Text(
                                                text = navController.context.getString(account.content),
                                                style = MaterialTheme.typography.subtitle2,
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = TextUnit(1.1f, TextUnitType.Sp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Category type
                    Text(
//                        text = if (transactionType == TransactionType.INCOME) {
//                            "Fund"
//                        } else "Pay with",
                        text = "Category",
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier
                            .padding(
                                horizontal = MaterialTheme.spacing.medium,
                                vertical = MaterialTheme.spacing.small
                            )
                            .align(Alignment.Start),
                    )

                    var expandedCategoryState by remember { mutableStateOf(false) }
                    var expenseItems: Array<Category> = Category.values()
                    var selectedCategory by remember { mutableStateOf(Category.FOOD_DRINK) }

                    // Category type picker
                    TextButton(
                        onClick = {
//                            navController.navigate(Screen.CategoryChooserScreen.route)
                        },
                        modifier = Modifier
                            .align(Alignment.Start)
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .padding(
                                horizontal = MaterialTheme.spacing.medium,
                                vertical = MaterialTheme.spacing.small
                            ),
                        border = BorderStroke(1.dp, MaterialTheme.colors.primary),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(
                            horizontal = MaterialTheme.spacing.medium,
                            vertical = MaterialTheme.spacing.medium
                        ),
                    ) {
                        Box(
                            modifier = Modifier
                                .clickable {
                                    expandedCategoryState = !expandedCategoryState
                                }
                                .fillMaxSize()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    painter = painterResource(
                                        id = selectedCategory.iconRes
                                    ),
                                    contentDescription = selectedCategory.title,
                                )

                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

                                Text(
                                    text = navController.context.getString(selectedCategory.content),
                                    style = MaterialTheme.typography.subtitle1
                                )
                            }

                            DropdownMenu(
                                expanded = expandedCategoryState,
                                onDismissRequest = { expandedCategoryState = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .requiredHeight(300.dp)
                            ) {
                                expenseItems.forEachIndexed { index, category ->
                                    DropdownMenuItem(
                                        onClick = {
                                            homeViewModel.selectCategory(category)
                                            expandedCategoryState = false
                                            selectedCategory = category
                                        },
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            Icon(
                                                painter = painterResource(
                                                    id = category.iconRes
                                                ),
                                                contentDescription = category.title,
                                            )
                                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                                            Text(
                                                text = navController.context.getString(category.content),
                                                style = MaterialTheme.typography.subtitle2,
                                                fontWeight = FontWeight.Bold,
                                                letterSpacing = TextUnit(1.1f, TextUnitType.Sp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Note field
                    NoteTextField(titleFieldValue, homeViewModel)

                    // Set time interval
                    SetRepeatable()

                    if (limitKey) {
                        if (limitInfoWarning is HomeViewModel.UIEvent.Alert) {
                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier
                                    .padding(
                                        horizontal = MaterialTheme.spacing.medium
                                    )
                                    .align(Alignment.Start)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.info_warning),
                                    contentDescription = null,
                                    tint = Red200
                                )
                                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                                    Text(
                                        text = (limitInfoWarning as HomeViewModel.UIEvent.Alert).info,
                                        style = MaterialTheme.typography.caption
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AccountPicker() {

}

@Composable
fun NoteTextField(
    noteTextField: TextFieldValue,
    homeViewModel: HomeViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Note
        Text(
            text = "Note",
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .padding(
                    horizontal = MaterialTheme.spacing.medium,
                    vertical = MaterialTheme.spacing.small
                )
                .align(Alignment.Start)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.spacing.medium
                ),
//                .border(BorderStroke(1.dp, MaterialTheme.colors.primary)),
            shape = RoundedCornerShape(6.dp),
            value = noteTextField.text,
            onValueChange = { field -> homeViewModel.setTransactionTitle(field) },
            label = { Text("Add note") },
        )
    }
}

@Composable
fun SetRepeatable(homeViewModel: HomeViewModel = hiltViewModel()) {
    var checkedState by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = MaterialTheme.spacing.medium
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Checkbox(
            checked = checkedState,
            onCheckedChange = { checkedState = !checkedState },
            colors = CheckboxDefaults.colors(MaterialTheme.colors.primary)
        )

        Text(text = "Set as auto-transaction")
    }

    // checkbox (not finished)
    if (checkedState) {
        Column {
            var expandedAutoState by remember { mutableStateOf(false) }
            val autoItems by remember {
                mutableStateOf(
                    listOf(
                        "Daily",
                        "Weekly",
                        "Monthly",
                        "Yearly "
                    )
                )
            }
            var selectedAuto by remember { mutableStateOf("Repeat timer") }

            TextButton(
                onClick = {  },
                modifier = Modifier
                    //                .align(Alignment.Start)
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .padding(
                        horizontal = MaterialTheme.spacing.medium,
                        vertical = MaterialTheme.spacing.small
                    ),
                border = BorderStroke(1.dp, MaterialTheme.colors.primary),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(
                    horizontal = MaterialTheme.spacing.medium,
                    vertical = MaterialTheme.spacing.medium
                ),
            ) {
                Box(
                    modifier = Modifier
                        .clickable {
                            expandedAutoState = !expandedAutoState
                        }
                        .fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Text(
                            text = selectedAuto,
                            style = MaterialTheme.typography.subtitle1
                        )
                    }

                    DropdownMenu(
                        expanded = expandedAutoState,
                        onDismissRequest = { expandedAutoState = false },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        autoItems.forEachIndexed { index, auto ->
                            DropdownMenuItem(
                                onClick = {
                                    expandedAutoState = false
                                    var timeSchedule = 1
                                    when (auto) {
                                        "Daily" -> timeSchedule = 1
                                        "Weekly" ->   timeSchedule = 7// 7 days in a week
                                        "Monthly" ->  timeSchedule = 30// Assuming 30 days in a month
                                        "Yearly" ->  timeSchedule = 365// Assuming 365 days in a year
                                    }
                                    homeViewModel.setTimeSchedule(timeSchedule)
                                },
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = auto,
                                        style = MaterialTheme.typography.subtitle2,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    homeViewModel.setAutoAdd(checkedState)
}