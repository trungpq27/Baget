package com.mobile.expenseapp.domain.usecase.write_database

import com.mobile.expenseapp.data.local.entity.ScheduleDto
import com.mobile.expenseapp.domain.repository.TransactionRepository
import javax.inject.Inject

class InsertNewScheduleUseCase @Inject constructor(private val repo: TransactionRepository) {
    suspend operator fun invoke(scheduleDto: ScheduleDto) {
        repo.insertSchedule(scheduleDto)
    }
}