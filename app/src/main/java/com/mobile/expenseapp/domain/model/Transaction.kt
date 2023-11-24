package com.mobile.expenseapp.domain.model

import com.mobile.expenseapp.data.local.entity.TransactionDto
import java.util.Date

data class Transaction(
    val date: Date,
    val dateOfEntry: String,
    val amount: Double,
    val account: String,
    val category: String,
    val transactionType: String,
    val title: String,
) {
    companion object {
        fun toTransactionDto(transaction: Transaction): TransactionDto {
            return TransactionDto(
                date = transaction.date,
                dateOfEntry = transaction.dateOfEntry,
                amount = transaction.amount,
                account = transaction.account,
                category = transaction.category,
                transactionType = transaction.transactionType,
                title = transaction.title
            )
        }
    }
}