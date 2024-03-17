package com.eva.scannerapp.domain.preferences

import kotlinx.coroutines.flow.Flow

interface SettingsPreferences {


	val isSaveToExternalAllowed: Flow<Boolean>

	suspend fun onSaveToExternalChanged(newValue: Boolean)
}