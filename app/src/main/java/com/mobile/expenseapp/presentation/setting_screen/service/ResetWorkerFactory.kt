package com.mobile.expenseapp.presentation.setting_screen.service

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.mobile.expenseapp.domain.usecase.write_datastore.EditExpenseLimitUseCase
import com.mobile.expenseapp.domain.usecase.write_database.InsertNewTransactionUseCase
import com.mobile.expenseapp.presentation.home_screen.service.MyAutoAddWorker
import com.mobile.expenseapp.presentation.setting_screen.service.LimitResetWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
class ResetWorkerFactory @Inject constructor(
    @ApplicationContext private val context: Context,
    private val editExpenseLimitUseCase: EditExpenseLimitUseCase,
    private val insertNewTransactionUseCase: InsertNewTransactionUseCase
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            LimitResetWorker::class.java.name ->
                LimitResetWorker(context, workerParameters, editExpenseLimitUseCase)
            MyAutoAddWorker::class.java.name ->
                MyAutoAddWorker(context, workerParameters, insertNewTransactionUseCase)
            else -> null
        }
    }
}
