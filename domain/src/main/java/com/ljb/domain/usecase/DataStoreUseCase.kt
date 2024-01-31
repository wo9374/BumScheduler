package com.ljb.domain.usecase

import com.ljb.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDarkModeUseCase @Inject constructor(private val repository: DataStoreRepository) {
    suspend operator fun invoke(): Flow<String> {
        return repository.getDarkModePrefs()
    }
}

@Singleton
class SaveDarkModeUseCase @Inject constructor(private val repository: DataStoreRepository) {
    suspend operator fun invoke(mode: String) {
        return repository.saveDarkModePrefs(mode)
    }
}