package com.mobile.expenseapp.domain.usecase.read_database

import com.mobile.expenseapp.data.local.entity.ScheduleDto
import com.mobile.expenseapp.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllScheduleUseCase @Inject constructor(private val repository: TransactionRepository){
    operator fun invoke(): Flow<List<ScheduleDto>?> {
        return repository.getAllSchedules()
    }
}