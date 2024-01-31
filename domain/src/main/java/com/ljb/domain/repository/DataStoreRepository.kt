package com.ljb.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun getDarkModePrefs(): Flow<String>
    suspend fun saveDarkModePrefs(mode: String)
}