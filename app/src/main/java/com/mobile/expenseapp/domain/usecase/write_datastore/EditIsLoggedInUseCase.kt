package com.mobile.expenseapp.domain.usecase.write_datastore

import com.mobile.expenseapp.domain.repository.DatastoreRepository
import javax.inject.Inject

class EditIsLoggedInUseCase @Inject constructor(private val datastoreRepository: DatastoreRepository) {
    suspend operator fun invoke(loggedIn: Boolean) {
        datastoreRepository.writeIsLoggedIn(loggedIn)
    }
}
