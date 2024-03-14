package com.eva.scannerapp.presentation.feature_capture.states

import android.graphics.Bitmap
import androidx.camera.core.ImageCaptureException
import com.eva.scannerapp.presentation.composables.options.AnalysisOption

interface ImageCaptureEvents {

	data object ToggleFlashMode : ImageCaptureEvents

	data class OnAnalysisModeChange(val mode: AnalysisOption) : ImageCaptureEvents

	data class OnImageCaptureFailed(val exception: ImageCaptureException) : ImageCaptureEvents

	data class OnImageCaptureSuccess(val bitmap: Bitmap? = null) : ImageCaptureEvents
}