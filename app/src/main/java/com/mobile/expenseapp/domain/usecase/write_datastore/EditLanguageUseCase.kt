package com.mobile.expenseapp.domain.usecase.write_datastore

import com.mobile.expenseapp.domain.repository.DatastoreRepository
import javax.inject.Inject

class EditLanguageUseCase @Inject constructor(private val datastoreRepository: DatastoreRepository) {
    suspend operator fun invoke(language: String) {
        datastoreRepository.writeLanguageToDataStore(language)
    }
}