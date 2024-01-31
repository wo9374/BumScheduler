package com.ljb.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ljb.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

const val LIGHT_MODE = "라이트"
const val DARK_MODE = "다크"
const val DEVICE_MODE = "기기 설정"

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStoreRepository {

    private companion object {
        val DARK_MODE_KEY = stringPreferencesKey("DARK_MODE_KEY")
    }


    override suspend fun getDarkModePrefs(): Flow<String> = dataStore.data.map {
        it[DARK_MODE_KEY] ?: DEVICE_MODE
    }

    override suspend fun saveDarkModePrefs(mode: String) {
        dataStore.edit {
            it[DARK_MODE_KEY] = mode
        }
    }
}