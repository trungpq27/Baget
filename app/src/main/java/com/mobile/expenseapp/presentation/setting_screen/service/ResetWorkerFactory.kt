package com.mobile.expenseapp.presentation.setting_screen.service

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.mobile.expenseapp.domain.usecase.read_database.GetAccountUseCase
import com.mobile.expenseapp.domain.usecase.read_database.GetAllScheduleUseCase
import com.mobile.expenseapp.domain.usecase.read_database.GetLocalDataUseCase
import com.mobile.expenseapp.domain.usecase.read_database.GetTransactionByTimestampUseCase
import com.mobile.expenseapp.domain.usecase.read_datastore.GetLoginTokenUseCase
import com.mobile.expenseapp.domain.usecase.write_database.InsertAccountsUseCase
import com.mobile.expenseapp.domain.usecase.write_datastore.EditExpenseLimitUseCase
import com.mobile.expenseapp.domain.usecase.write_database.InsertNewTransactionUseCase
import com.mobile.expenseapp.domain.usecase.write_database.UpdateScheduleUseCase
import com.mobile.expenseapp.presentation.home_screen.service.MyAutoAddWorker
import com.mobile.expenseapp.presentation.home_screen.service.SyncWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@InternalCoroutinesApi
class ResetWorkerFactory @Inject constructor(
    @ApplicationContext private val context: Context,
    private val editExpenseLimitUseCase: EditExpenseLimitUseCase,
    private val insertNewTransactionUseCase: InsertNewTransactionUseCase,
    private val getTransactionByTimestampUseCase: GetTransactionByTimestampUseCase,
    private val getAllScheduleUseCase: GetAllScheduleUseCase,
    private val updateScheduleUseCase: UpdateScheduleUseCase,
    private val getAccountUseCase: GetAccountUseCase,
    private val insertAccountsUseCase: InsertAccountsUseCase,
    private val getLocalDataUseCase: GetLocalDataUseCase,
    private val getLoginTokenUseCase: GetLoginTokenUseCase,
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
                MyAutoAddWorker(
                    context,
                    workerParameters,
                    insertNewTransactionUseCase,
                    getTransactionByTimestampUseCase,
                    getAllScheduleUseCase,
                    updateScheduleUseCase,
                    getAccountUseCase,
                    insertAccountsUseCase,
                )
            SyncWorker::class.java.name ->
                SyncWorker(
                    context,
                    workerParameters,
                    getLocalDataUseCase,
                    getLoginTokenUseCase,
                )
            else -> null
        }
    }
}
