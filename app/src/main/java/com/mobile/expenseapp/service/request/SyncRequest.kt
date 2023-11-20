package com.mobile.expenseapp.service.request

import com.mobile.expenseapp.data.local.entity.LocalData
import com.mobile.expenseapp.service.entity.AccountSync
import com.mobile.expenseapp.service.entity.TransactionSync
import com.mobile.expenseapp.service.entity.toSync

data class SyncRequest(
    val accounts: List<AccountSync>,
    val transactions: List<TransactionSync>,
) {
    fun toDto(): LocalData {
        return LocalData(
            accounts.map { it.toDto() },
            transactions.map { it.toDto() }
        )
    }
}

fun LocalData.toSync(): SyncRequest{
    return SyncRequest(
        accounts.map { it.toSync() },
        transactions.map { it.toSync() }
    )
}
