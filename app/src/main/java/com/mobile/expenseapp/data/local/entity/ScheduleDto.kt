package com.mobile.expenseapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.mobile.expenseapp.domain.model.Schedule
import com.mobile.expenseapp.domain.model.Transaction
import java.util.Date
import java.util.concurrent.TimeUnit

@Entity(
    tableName = "schedule_table",
    foreignKeys = [
        ForeignKey(
            entity = TransactionDto::class,
            parentColumns = ["timestamp"],
            childColumns = ["transactionTimestamp"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ScheduleDto(
    @PrimaryKey
    @ColumnInfo(name = "transactionTimestamp")
    val transactionDto: Date,
    @ColumnInfo(name = "time_schedule")
    val timeSchedule: Long,
    @ColumnInfo(name = "time_unit")
    val timeUnit: String,
    @ColumnInfo(name = "last_time_added")
    val lastTimeAdded: Date,
) {

}
