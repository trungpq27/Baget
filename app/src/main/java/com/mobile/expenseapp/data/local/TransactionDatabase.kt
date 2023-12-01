package com.mobile.expenseapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobile.expenseapp.data.local.converter.DateConverter
import com.mobile.expenseapp.data.local.entity.AccountDto
import com.mobile.expenseapp.data.local.entity.ScheduleDto
import com.mobile.expenseapp.data.local.entity.TransactionDto

@TypeConverters(value = [DateConverter::class])
@Database(entities = [TransactionDto::class, AccountDto::class, ScheduleDto::class], exportSchema = true, version = 2)
abstract class TransactionDatabase: RoomDatabase() {
    abstract val transactionDao: TransactionDao
}