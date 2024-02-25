package com.eva.scannerapp.presentation.feature_capture

import androidx.lifecycle.viewModelScope
import com.eva.scannerapp.domain.repository.ImageReaderRepo
import com.eva.scannerapp.presentation.feature_capture.states.ImagePreviewState
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
	private val imageReaderRepo: ImageReaderRepo,
) : ScannerAppViewModel() {

	private val _imageState = MutableStateFlow(ImagePreviewState())
	val previewImageState = _imageState.asStateFlow()

	private val _isFlashEnabled = MutableStateFlow(false)
	val isFlashEnabled = _isFlashEnabled.asStateFlow()

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()
	
	init {
		loadPreviewImage()
	}

	private fun loadPreviewImage() = viewModelScope.launch {
		imageReaderRepo.lastSavedImage
			.onEach { res ->
				when (res) {
					is Resource.Error -> {}

					is Resource.Loading -> _imageState.update { it.copy(isLoading = true) }
					is Resource.Success -> _imageState.update { state ->
						state.copy(isLoading = false, image = res.data)
					}
				}

			}.launchIn(this)
	}

}