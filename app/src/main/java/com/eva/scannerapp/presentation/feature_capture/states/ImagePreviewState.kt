package com.eva.scannerapp.presentation.feature_capture.states

import com.eva.scannerapp.domain.image.models.ImageDataModel

data class ImagePreviewState(
	val isLoading: Boolean = true,
	val image: ImageDataModel? = null,
)