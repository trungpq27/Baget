package com.mobile.expenseapp.domain.usecase.read_database

import com.mobile.expenseapp.data.local.entity.AccountDto
import com.mobile.expenseapp.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAccountsUseCase @Inject constructor(private val repo: TransactionRepository) {
    operator fun invoke(): Flow<List<AccountDto>> {
        return repo.getAccounts()
    }
}