package com.eva.scannerapp.presentation.feature_result

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.eva.scannerapp.domain.repository.StaticImageAnalyzerRepo
import com.eva.scannerapp.presentation.composables.options.AnalysisOption
import com.eva.scannerapp.presentation.feature_result.util.AnalysisScreenEvents
import com.eva.scannerapp.presentation.feature_result.util.AnalysisScreenState
import com.eva.scannerapp.presentation.navigation.navArgs.ResultsScreenArgs
import com.eva.scannerapp.presentation.navigation.routes.navArgs
import com.eva.scannerapp.presentation.util.viewmodel.ScannerAppViewModel
import com.eva.scannerapp.presentation.util.viewmodel.UiEvents
import com.eva.scannerapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalysisResultsViewModel @Inject constructor(
	private val savedStateHandle: SavedStateHandle,
	private val analyzer: StaticImageAnalyzerRepo,
) : ScannerAppViewModel() {

	private val _resultsState = MutableStateFlow(AnalysisScreenState())
	val resultState = _resultsState.asStateFlow()

	private val _analysisOption = MutableStateFlow(AnalysisOption.BAR_CODE)
	val analysisOption = _analysisOption.asStateFlow()

	private val _uiEvents = MutableSharedFlow<UiEvents>()

	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()

	private val screenArgs: ResultsScreenArgs
		get() = savedStateHandle.navArgs()

	init {
		_resultsState.update { state -> state.copy(fileUri = screenArgs.fileUri) }
		// if screen-args file uri is valid, then show results
		screenArgs.fileUri?.toString()?.let(::analyzeImage)
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	private fun analyzeImage(fileUri: String) = viewModelScope.launch {
		_analysisOption.flatMapLatest { option ->
			when (option) {
				AnalysisOption.BAR_CODE -> analyzer.prepareBarCodeResults(fileUri)
				AnalysisOption.LABELS -> analyzer.prepareLabelsResults(fileUri)
			}
		}.onEach { res ->
			_resultsState.update { state -> state.copy(isAnalysing = res is Resource.Loading) }

			when (res) {
				is Resource.Error -> _uiEvents.emit(UiEvents.ShowSnackBar(res.message ?: ""))
				is Resource.Success -> _resultsState.update { state -> state.copy(analysisResult = res.data) }
				else -> {}
			}
		}.launchIn(this)
	}

	fun onEvents(event: AnalysisScreenEvents) {
		when (event) {
			is AnalysisScreenEvents.OnAnalysisOptionSwitched -> _analysisOption.update { event.option }
		}
	}

	override fun onCleared() {
		Log.d("VIEWMODEL", "CLEARED ${screenArgs.fromCamera}")
		super.onCleared()
	}

}