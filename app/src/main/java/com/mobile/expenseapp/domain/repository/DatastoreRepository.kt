package com.mobile.expenseapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface DatastoreRepository {
    suspend fun writeIsLoggedIn(loggedIn: Boolean)
    suspend fun readIsLoggedIn(): Flow<Boolean>
    suspend fun writeLoginToken(token: String)
    suspend fun readLoginToken(): Flow<String?>
    suspend fun writeOnboardingKeyToDataStore(completed: Boolean)

    suspend fun readOnboardingKeyFromDataStore(): Flow<Boolean>

    suspend fun writeCurrencyToDataStore(currency: String)

    suspend fun readCurrencyFromDataStore(): Flow<String>

    suspend fun writeExpenseLimitToDataStore(amount: Double)

    suspend fun readExpenseLimitFromDataStore(): Flow<Double>

    suspend fun writeLimitKeyToDataStore(enabled: Boolean)

    suspend fun readLimitKeyFromDataStore(): Flow<Boolean>

    suspend fun writeDarkModeToDataStore(enabled: Boolean)

    suspend fun readDarkModeFromDataStore(): Flow<Boolean>

    suspend fun writeLimitDurationToDataStore(duration: Int)

    suspend fun readLimitDurationFromDataStore(): Flow<Int>

    suspend fun writeLanguageToDataStore(language: String)

    suspend fun readLanguageFromDataStore(): Flow<String>

    suspend fun eraseDataStore()
}