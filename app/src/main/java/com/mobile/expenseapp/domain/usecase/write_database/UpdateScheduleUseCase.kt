package com.mobile.expenseapp.domain.usecase.write_database

import com.mobile.expenseapp.data.local.entity.ScheduleDto
import com.mobile.expenseapp.domain.repository.TransactionRepository
import javax.inject.Inject

class UpdateScheduleUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    operator fun invoke(schedule: ScheduleDto) {
        transactionRepository.updateSchedule(schedule)
    }
}
