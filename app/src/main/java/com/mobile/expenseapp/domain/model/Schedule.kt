package com.mobile.expenseapp.domain.model

import java.sql.Date
import java.sql.Timestamp
import java.util.concurrent.TimeUnit

data class Schedule(
    val transaction: Transaction, // Reference to the associated Transaction
    val timeSchedule: Long,
    val timeUnit: TimeUnit,
    val lastTimeAdded: Date // Assuming you want to store the timestamp
)