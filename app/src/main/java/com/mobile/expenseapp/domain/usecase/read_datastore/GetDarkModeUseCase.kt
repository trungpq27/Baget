package com.mobile.expenseapp.domain.usecase.read_datastore

import android.content.SharedPreferences
import com.mobile.expenseapp.domain.repository.DatastoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDarkModeUseCase @Inject constructor(
    private val datastoreRepository: DatastoreRepository
) {
    suspend operator fun invoke(): Flow<Boolean> {
        return datastoreRepository.readDarkModeFromDataStore()
    }
}
