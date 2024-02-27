package com.eva.scannerapp.presentation.feature_capture

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.eva.scannerapp.domain.image.FileReadPermissionNotFoundException
import com.eva.scannerapp.domain.repository.ImageReaderRepo
import com.eva.scannerapp.presentation.feature_capture.states.ImagePreviewState
import com.eva.scannerapp.presentation.feature_capture.states.ImageScannerEvents
import com.eva.scannerapp.presentation.feature_capture.states.ImageScannerState
import com.eva.scannerapp.presentation.util.ScannerAppViewModel
import com.eva.scannerapp.presentation.util.UiEvents
import com.eva.scannerapp.util.Resource
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
class ImageCaptureViewModel @Inject constructor(
	private val repo: ImageReaderRepo,
) : ScannerAppViewModel() {

	private val _imageState = MutableStateFlow(ImagePreviewState())
	val previewImage = _imageState.asStateFlow()

	private val _state = MutableStateFlow(ImageScannerState())
	val screenState = _state.asStateFlow()

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()

	init {
		loadPreviewImage()
	}

	private fun loadPreviewImage() = viewModelScope.launch {
		repo.lastSavedImage
			.onEach { res ->
				when (res) {
					is Resource.Error -> {
						if (res.error is FileReadPermissionNotFoundException) return@onEach
						_uiEvents.emit(UiEvents.ShowToast(res.error?.message ?: ""))
					}

					is Resource.Loading -> _imageState.update { it.copy(isLoading = true) }
					is Resource.Success -> _imageState.update { state ->
						state.copy(isLoading = false, image = res.data)
					}
				}

			}.launchIn(this)
	}

	fun onEvent(event: ImageScannerEvents) {
		when (event) {
			ImageScannerEvents.ToggleFlashMode -> _state.update { state ->
				state.copy(isFlashOn = !state.isFlashOn)
			}

			is ImageScannerEvents.OnAnalysisModeChange -> _state.update { state ->
				state.copy(analysisOption = event.mode)
			}

			is ImageScannerEvents.OnImageCaptureSuccess -> {
				Log.d("IMAGE_BITMAP", event.image.toString())
			}

			is ImageScannerEvents.OnImageCaptureFailed -> {
				viewModelScope.launch {
					_uiEvents.emit(
						UiEvents.ShowSnackBar(
							message = event.exception.message ?: "Camera exception"
						)
					)
				}
			}
		}
	}

}