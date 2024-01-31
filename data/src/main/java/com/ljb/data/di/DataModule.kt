package com.ljb.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ljb.data.database.HolidayDao
import com.ljb.data.database.HolidayDatabase
import com.ljb.data.datasource.LocalHolidaySource
import com.ljb.data.datasource.LocalHolidaySourceImpl
import com.ljb.data.datasource.RemoteHolidaySource
import com.ljb.data.datasource.RemoteHolidaySourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindLocalHolidaySource(impl: LocalHolidaySourceImpl): LocalHolidaySource

    @Binds
    abstract fun bindRemoteHolidaySource(impl: RemoteHolidaySourceImpl): RemoteHolidaySource

}

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideHolidayDatabase(@ApplicationContext appContext: Context) =
        HolidayDatabase.getInstance(appContext)

    @Singleton
    @Provides
    fun provideHolidayDao(database: HolidayDatabase): HolidayDao = database.dao()


    private val Context.darkModeDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "bum_scheduler_datastore"
    )

    @Singleton
    @Provides
    fun provideDarkModePrefsDataStore(@ApplicationContext appContext: Context) : DataStore<Preferences>{
        return appContext.darkModeDataStore
    }
}