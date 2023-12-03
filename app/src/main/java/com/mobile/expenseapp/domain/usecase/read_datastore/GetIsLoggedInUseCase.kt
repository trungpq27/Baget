package com.mobile.expenseapp.domain.usecase.read_datastore

import com.mobile.expenseapp.domain.repository.DatastoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetIsLoggedInUseCase @Inject constructor(
    private val datastoreRepository: DatastoreRepository
) {
    suspend operator fun invoke(): Flow<Boolean> {
        return datastoreRepository.readIsLoggedIn()
    }
}
