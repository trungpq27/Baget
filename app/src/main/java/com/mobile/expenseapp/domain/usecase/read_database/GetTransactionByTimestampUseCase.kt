package com.mobile.expenseapp.domain.usecase.read_database

import com.mobile.expenseapp.data.local.entity.TransactionDto
import com.mobile.expenseapp.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class GetTransactionByTimestampUseCase @Inject constructor(
    private val repository: TransactionRepository
) {

    suspend operator fun invoke(timestamp: Date): Flow<TransactionDto> {
        return repository.getTransactionByTimestamp(timestamp)
    }
}
