package com.mobile.expenseapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.expenseapp.domain.model.Schedule
import java.sql.Date
import java.util.concurrent.TimeUnit

@Entity(tableName = "schedule_table")
data class ScheduleDto(
    @ColumnInfo(name = "transaction")
    val transactionDto: TransactionDto,
    @ColumnInfo(name = "time_schedule")
    val timeSchedule: Long,
    @ColumnInfo(name = "time_unit")
    val timeUnit: String,
    @ColumnInfo(name = "last_time_added")
    val lastTimeAdded: Date,
) {
    fun toSchedule() = Schedule(
        transaction = transactionDto.toTransaction(),
        timeSchedule = timeSchedule,
        timeUnit = TimeUnit.valueOf(timeUnit),
        lastTimeAdded = lastTimeAdded
    )
}
