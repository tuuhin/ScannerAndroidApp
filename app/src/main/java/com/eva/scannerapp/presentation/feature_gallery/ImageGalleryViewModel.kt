package com.eva.scannerapp.presentation.feature_gallery

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eva.scannerapp.domain.image.ImageFileReader
import com.eva.scannerapp.domain.image.models.ImageDataModel
import com.eva.scannerapp.presentation.util.ScannerAppViewModel
import com.eva.scannerapp.presentation.util.UiEvents
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageGalleryViewModel @Inject constructor(
	private val imageReader: ImageFileReader,
	imagePager: Pager<Int, ImageDataModel>,
) : ScannerAppViewModel() {

	// is a device having permissions to read image it will be used
	// only in the viewmodel not by the screen
	private val _canReadImage = MutableStateFlow(imageReader.isPermissionProvided)
	val canReadImage = _canReadImage.asStateFlow()

	// if permissions are not provided, use an empty flow of paging data
	private val _emptyPagedFlow: Flow<PagingData<ImageDataModel>>
		get() = flowOf(PagingData.empty())


	private val _uiEvents = MutableSharedFlow<UiEvents>()
	override val uiEvents: SharedFlow<UiEvents>
		get() = _uiEvents.asSharedFlow()


	@OptIn(ExperimentalCoroutinesApi::class)
	val pagedImages = _canReadImage
		.flatMapLatest { isProvided ->
			if (isProvided) imagePager.flow else _emptyPagedFlow
		}.catch { err -> err.printStackTrace() }
		.cachedIn(viewModelScope)

	fun onReadPermissionChange(isAllowed: Boolean) {
		val actualPerms = imageReader.isPermissionProvided
		if (isAllowed != actualPerms) viewModelScope.launch {
			_uiEvents.emit(UiEvents.ShowSnackBar("Some problem occurred with permission handling"))
		}
		// recheck permissions if the app has been granted the correct permission
		_canReadImage.update { imageReader.isPermissionProvided }
	}
}

