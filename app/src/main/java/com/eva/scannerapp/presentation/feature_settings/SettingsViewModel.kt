package com.eva.scannerapp.presentation.feature_settings

import androidx.lifecycle.viewModelScope
import com.eva.scannerapp.domain.preferences.SettingsPreferences
import com.eva.scannerapp.domain.repository.ImageRepository
import com.eva.scannerapp.presentation.feature_settings.state.SettingsScreenEvents
import com.eva.scannerapp.presentation.feature_settings.state.SettingsScreenState
import com.eva.scannerapp.presentation.util.viewmodel.ScannerAppViewModel
import com.eva.scannerapp.presentation.util.viewmodel.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
	private val repo: ImageRepository,
	private val settings: SettingsPreferences,
) : ScannerAppViewModel() {

	private val _state = MutableStateFlow(SettingsScreenState())
	val settingsState = _state.asStateFlow()

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()


	init {
		settings.isSaveToExternalAllowed
			.onEach { current ->
				_state.update { state -> state.copy(isExternalSaveAllowed = current) }
			}.launchIn(viewModelScope)
	}


	fun onEvents(event: SettingsScreenEvents) {
		when (event) {
			SettingsScreenEvents.OnClearImageCache -> viewModelScope.launch {
				repo.deleteAllLocalCache()
			}

			SettingsScreenEvents.ToggleSaveToExternalStorage -> viewModelScope.launch {
				settings.onSaveToExternalChanged(newValue = !_state.value.isExternalSaveAllowed)
			}
		}
	}

}