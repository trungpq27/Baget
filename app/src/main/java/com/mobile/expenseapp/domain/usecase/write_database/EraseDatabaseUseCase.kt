package com.mobile.expenseapp.domain.usecase.write_database

import com.mobile.expenseapp.domain.repository.TransactionRepository
import javax.inject.Inject

class EraseDatabaseUseCase @Inject constructor(private val repository: TransactionRepository) {
    suspend operator fun invoke() {
        repository.eraseDatabase()
    }
}
