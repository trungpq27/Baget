package com.mobile.expenseapp.domain.usecase.read_database

import com.mobile.expenseapp.data.local.entity.TransactionDto
import com.mobile.expenseapp.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDailyTransactionUseCase @Inject constructor(private val repo: TransactionRepository) {

    operator fun invoke(entryDate: String): Flow<List<TransactionDto>?> {
        return repo.getDailyTransaction(entryDate)
    }
}