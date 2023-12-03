package com.mobile.expenseapp.service.entity

import com.mobile.expenseapp.data.local.entity.TransactionDto
import java.util.Date

data class TransactionSync(
    val date: Long,
    val entry_date: String,
    val amount: Double,
    val account: String,
    val category: String,
    val transaction_type: String,
    val transaction_title: String,
){
    fun toDto(): TransactionDto {
        return TransactionDto(
            Date(date),
            entry_date,
            amount,
            account,
            category,
            transaction_type,
            transaction_title,
        )
    }
}

fun TransactionDto.toSync(): TransactionSync{
    return TransactionSync(
        date.time,
        dateOfEntry,
        amount,
        account,
        category,
        transactionType,
        title,
    )
}
