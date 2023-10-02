package com.mobile.expenseapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface DatastoreRepository {
    suspend fun readOnboardingKeyFromDataStore(): Flow<Boolean>
}