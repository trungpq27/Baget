package com.mobile.expenseapp.domain.usecase.write_database

import com.mobile.expenseapp.data.local.entity.ScheduleDto
import com.mobile.expenseapp.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteScheduleUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(scheduleDto: ScheduleDto) {
        transactionRepository.deleteSchedule(scheduleDto)
    }
}