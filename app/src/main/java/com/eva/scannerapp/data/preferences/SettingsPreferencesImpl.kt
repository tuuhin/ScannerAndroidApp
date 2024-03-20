package com.eva.scannerapp.data.preferences

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.eva.scannerapp.domain.preferences.SettingsPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.datastore by preferencesDataStore(name = PreferenceKeys.DATASTORE_NAME)

class SettingsPreferencesImpl(
	private val context: Context
) : SettingsPreferences {

	private val isSaveToExternalKey =
		booleanPreferencesKey(name = PreferenceKeys.IS_SAVE_TO_EXTERNAL_STORE)

	private val checkWritePerms: Boolean
		get() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
			ContextCompat.checkSelfPermission(
				context,
				Manifest.permission.WRITE_EXTERNAL_STORAGE
			) == PermissionChecker.PERMISSION_GRANTED else true

	override val isSaveToExternalAllowed: Flow<Boolean>
		get() = context.datastore.data
			.map { prefs -> prefs[isSaveToExternalKey] ?: false }

	override suspend fun onSaveToExternalChanged(newValue: Boolean) {
		context.datastore.edit { prefs ->
			prefs[isSaveToExternalKey] = checkWritePerms && newValue
		}
	}

}