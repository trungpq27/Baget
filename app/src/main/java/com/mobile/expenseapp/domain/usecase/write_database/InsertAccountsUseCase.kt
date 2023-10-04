package com.mobile.expenseapp.domain.usecase.write_database

import com.mobile.expenseapp.data.local.entity.AccountDto
import com.mobile.expenseapp.domain.repository.TransactionRepository
import javax.inject.Inject

class InsertAccountsUseCase @Inject constructor(private val repo: TransactionRepository) {

    suspend operator fun invoke(account: List<AccountDto>) {
        repo.insertAccount(account)
    }
}