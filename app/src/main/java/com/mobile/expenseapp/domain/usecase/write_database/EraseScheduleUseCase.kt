package com.mobile.expenseapp.domain.usecase.write_database

import com.mobile.expenseapp.domain.repository.TransactionRepository
import javax.inject.Inject

class EraseScheduleUseCase @Inject constructor(private val repository: TransactionRepository) {
    operator fun invoke() {
        repository.eraseSchedules()
    }
}