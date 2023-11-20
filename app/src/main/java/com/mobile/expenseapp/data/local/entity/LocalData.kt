package com.mobile.expenseapp.data.local.entity

data class LocalData(
    val accounts: List<AccountDto>,
    val transactions: List<TransactionDto>
)