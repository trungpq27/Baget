package com.mobile.expenseapp.domain.usecase.write_datastore

import android.content.SharedPreferences
import com.mobile.expenseapp.domain.repository.DatastoreRepository
import javax.inject.Inject

class EditDarkModeUseCase @Inject constructor(
    private val datastoreRepository: DatastoreRepository
) {
    suspend operator fun invoke(enabled: Boolean) {
        datastoreRepository.writeDarkModeToDataStore(enabled)
    }
}
