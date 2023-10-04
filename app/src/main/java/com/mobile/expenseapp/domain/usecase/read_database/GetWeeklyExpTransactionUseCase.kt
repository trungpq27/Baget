package com.mobile.expenseapp.domain.usecase.read_database

import com.mobile.expenseapp.data.local.entity.TransactionDto
import com.mobile.expenseapp.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeeklyExpTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(): Flow<List<TransactionDto>> {
        return transactionRepository.getWeeklyExpTransaction()
    }
}