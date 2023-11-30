package com.mobile.expenseapp.domain.usecase.write_database

import com.mobile.expenseapp.data.local.entity.LocalData
import com.mobile.expenseapp.domain.repository.TransactionRepository
import javax.inject.Inject

class SyncFromRemoteUseCase @Inject constructor(private val repo: TransactionRepository) {
    suspend operator fun invoke(data: LocalData) {
        repo.insertTransactions(data.transactions)
        repo.insertAccounts(data.accounts)
        repo.insertSchedules(data.schedules)
    }
}