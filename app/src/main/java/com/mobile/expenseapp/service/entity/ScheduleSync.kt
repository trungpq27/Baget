package com.mobile.expenseapp.service.entity

import com.mobile.expenseapp.data.local.entity.ScheduleDto
import java.util.Date

data class ScheduleSync(
    val transactionDto: Long,
    val timeSchedule: Long,
    val timeUnit: String,
    val lastTimeAdded: Long,
) {
    fun toDto(): ScheduleDto {
        return ScheduleDto(
            Date(transactionDto),
            timeSchedule,
            timeUnit,
            Date(lastTimeAdded)
        )
    }
}

fun ScheduleDto.toSync(): ScheduleSync {
    return ScheduleSync(
        transactionDto.time,
        timeSchedule,
        timeUnit,
        lastTimeAdded.time
    )
}