package com.eva.scannerapp.presentation.feature_gallery

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eva.scannerapp.domain.image.ImageFileReader
import com.eva.scannerapp.domain.image.models.ImageDataModel
import com.eva.scannerapp.presentation.feature_gallery.state.GalleryPermissionState
import com.eva.scannerapp.presentation.util.viewmodel.ScannerAppViewModel
import com.eva.scannerapp.presentation.util.viewmodel.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ImageGalleryViewModel @Inject constructor(
	private val imageReader: ImageFileReader,
	private val imagePager: Pager<Int, ImageDataModel>,
) : ScannerAppViewModel() {

	// is a device having permissions to read image it will be used
	// only in the viewmodel not by the screen inti-tally its unknown
	private val _canReadImage = MutableStateFlow(GalleryPermissionState.UNKNOWN)
	val canReadImage = _canReadImage.asStateFlow()

	private val fullReadOk: Boolean
		get() = imageReader.isFullReadPermissionGranted

	private val partialReadOk: Boolean
		get() = imageReader.isPartialReadAllowed

	// if permissions are not provided, use an empty flow of paging data
	private val _emptyPagedFlow: Flow<PagingData<ImageDataModel>>
		get() = flowOf(PagingData.empty())

	private val _uiEvents = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()


	@OptIn(ExperimentalCoroutinesApi::class)
	val pagedImages = _canReadImage
		.flatMapLatest { permsState ->
			when (permsState) {
				GalleryPermissionState.NOT_GRANTED -> _emptyPagedFlow
				else -> imagePager.flow
			}
		}.catch { err -> err.printStackTrace() }
		.cachedIn(viewModelScope)

	fun onRecheckPermissions(state: GalleryPermissionState? = null) {
		// if the current read value is equal to the state to change are same then skip
		if (_canReadImage.value == state) return
		// if read allowed then oke
		_canReadImage.update {
			if (fullReadOk) GalleryPermissionState.GRANTED
			else if (partialReadOk) GalleryPermissionState.PARTIALLY_GRANTED
			else GalleryPermissionState.NOT_GRANTED
		}
	}
}

