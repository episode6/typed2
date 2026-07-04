package com.episode6.typed2.sampleapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@HiltAndroidApp class SampleApplication : Application()

private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = "Typed2SampleAppDataStore")

@Module @InstallIn(SingletonComponent::class) object AppModule {
  @Provides fun sharedPrefs(@ApplicationContext appContext: Context): SharedPreferences =
    appContext.getSharedPreferences("Typed2SampleAppPrefs", Context.MODE_PRIVATE)

  @Provides fun dataStore(@ApplicationContext appContext: Context): DataStore<Preferences> = appContext.appDataStore
}
