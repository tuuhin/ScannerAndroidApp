package com.eva.scannerapp.di

import android.content.Context
import com.eva.scannerapp.data.preferences.SettingsPreferencesImpl
import com.eva.scannerapp.domain.preferences.SettingsPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

	@Provides
	@Singleton
	fun providesPreferences(@ApplicationContext context: Context): SettingsPreferences =
		SettingsPreferencesImpl(context)
}