package com.eva.scannerapp.presentation.feature_settings.state

interface SettingsScreenEvents {

	data object ToggleSaveToExternalStorage : SettingsScreenEvents

	data object OnClearImageCache : SettingsScreenEvents
}