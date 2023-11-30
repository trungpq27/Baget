package com.mobile.expenseapp.domain.usecase.read_database

import com.mobile.expenseapp.data.local.entity.LocalData
import com.mobile.expenseapp.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetLocalDataUseCase @Inject constructor(private val repo: TransactionRepository) {
    operator fun invoke(): Flow<LocalData> {
        val accounts = repo.getAccounts()
        val transactions = repo.getAllTransaction()
        val schedules = repo.getAllSchedules()
        return combine(accounts, transactions, schedules) { account, transaction, schedule ->
            LocalData(account, transaction, schedule)
        }
    }
}
