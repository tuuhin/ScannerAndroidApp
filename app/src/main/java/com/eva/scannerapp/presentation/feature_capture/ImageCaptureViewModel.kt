package com.eva.scannerapp.presentation.feature_capture

import android.graphics.Bitmap
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.eva.scannerapp.data.ml.feed.CameraFeedImageAnalyzer
import com.eva.scannerapp.domain.ml.MLResource
import com.eva.scannerapp.domain.ml.models.RecognizedBarcode
import com.eva.scannerapp.domain.ml.models.RecognizedLabel
import com.eva.scannerapp.domain.repository.ImageRepository
import com.eva.scannerapp.presentation.composables.options.AnalysisOption
import com.eva.scannerapp.presentation.feature_capture.states.AnalyzerSheetEvents
import com.eva.scannerapp.presentation.feature_capture.states.CameraScreenState
import com.eva.scannerapp.presentation.feature_capture.states.ImageCaptureEvents
import com.eva.scannerapp.presentation.feature_capture.states.ImagePreviewState
import com.eva.scannerapp.presentation.feature_capture.states.RecognizedItemState
import com.eva.scannerapp.presentation.util.viewmodel.ScannerAppViewModel
import com.eva.scannerapp.presentation.util.viewmodel.UiEvents
import com.eva.scannerapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageCaptureViewModel @Inject constructor(
	private val repository: ImageRepository,
	private val barCodeAnalyzer: CameraFeedImageAnalyzer<RecognizedBarcode>,
	private val labelAnalyzer: CameraFeedImageAnalyzer<RecognizedLabel>,
) : ScannerAppViewModel() {

	private val _previewImageState = MutableStateFlow(ImagePreviewState())
	val previewImageState = _previewImageState.asStateFlow()

	private val _cameraScreenState = MutableStateFlow(CameraScreenState())
	val cameraScreenState = _cameraScreenState.asStateFlow()

	private val _isImageCapturing = MutableStateFlow(false)
	val isCapturing = _isImageCapturing.asStateFlow()

	private val _recognizedItemState = MutableStateFlow(RecognizedItemState())
	val recognizedItemState = _recognizedItemState.asStateFlow()

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()


	val imageAnalyzer = _cameraScreenState.map { state ->
		when (state.analysisOption) {
			AnalysisOption.BAR_CODE -> barCodeAnalyzer
			AnalysisOption.LABELS -> labelAnalyzer
		}
	}

	init {
		startCameraFeedAnalyzer()
		loadPreviewImage()
	}

	private fun startCameraFeedAnalyzer() = viewModelScope.launch {
		imageAnalyzer.onEach { feedImageAnalyzer ->
			//hide the results when done
			_uiEvents.emit(UiEvents.ShowToast(message = "Image Analyzer Changed"))

			feedImageAnalyzer.setImageListener { res ->
				when (res) {
					is MLResource.Empty -> _recognizedItemState
						.update { state -> state.copy(models = null) }

					is MLResource.Error -> viewModelScope.launch {
						_uiEvents.emit(UiEvents.ShowToast(message = res.error?.message ?: ""))
					}

					is MLResource.Success -> _recognizedItemState
						.update { state -> state.copy(models = res.data) }
				}
			}

		}.launchIn(this)
	}

	private fun loadPreviewImage() = viewModelScope.launch {
		repository.lastSavedImage.onEach { res ->
			//update the loading state
			_previewImageState.update { state ->
				state.copy(isLoading = res is Resource.Loading)
			}
			// update the error or success state
			when (res) {
				is Resource.Error -> _uiEvents.emit(
					UiEvents.ShowSnackBar(message = res.error?.message ?: "")
				)

				is Resource.Success -> _previewImageState.update { state ->
					state.copy(isLoading = false, image = res.data)
				}

				else -> {}
			}

		}.launchIn(this)
	}


	fun onImageEvents(event: ImageCaptureEvents) {
		when (event) {
			ImageCaptureEvents.ToggleFlashMode -> _cameraScreenState.update { state ->
				state.copy(isFlashOn = !state.isFlashOn)
			}

			is ImageCaptureEvents.OnAnalysisModeChange -> _cameraScreenState.update { state ->
				state.copy(analysisOption = event.mode)
			}

			is ImageCaptureEvents.OnImageCaptureFailed -> viewModelScope.launch {
				_uiEvents.emit(
					UiEvents.ShowSnackBar(message = event.exception.message ?: "Exception")
				)
			}


			is ImageCaptureEvents.OnImageCaptureSuccess -> onImageCapture(event.bitmap)
		}
	}

	fun onSheetEvents(event: AnalyzerSheetEvents) {
		when (event) {
			AnalyzerSheetEvents.CloseBottomSheet -> _recognizedItemState
				.update { state -> state.copy(showResults = false, savedModel = null) }

			AnalyzerSheetEvents.OpenBottomSheet -> _recognizedItemState
				.update { state -> state.copy(showResults = true) }

			is AnalyzerSheetEvents.OnSelectBoundingRect -> _recognizedItemState
				.update { state -> state.copy(showResults = true, savedModel = event.model) }
		}
	}

	private fun onImageCapture(bitmap: Bitmap?) = viewModelScope.launch {
		//skip if bitmap is null
		val bitmapImage = bitmap ?: return@launch
		// skip if image is already taken and its processing
		if (_isImageCapturing.value) return@launch

		repository.saveCaptureContent(image = bitmapImage)
			.onEach { res ->
				// update the capturing state according to state
				_isImageCapturing.update { res is Resource.Loading }

				when (res) {
					is Resource.Error -> _uiEvents.emit(
						UiEvents.ShowSnackBar(message = res.message ?: "")
					)

					is Resource.Success -> {
						val uri = res.data?.toUri()
						_uiEvents.emit(UiEvents.NavigateToResults(uri))
					}

					else -> {}
				}
			}.launchIn(this)
	}
}

