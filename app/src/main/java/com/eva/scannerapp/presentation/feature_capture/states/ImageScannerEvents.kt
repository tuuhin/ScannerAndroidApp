package com.eva.scannerapp.presentation.feature_capture.states

import android.graphics.Bitmap
import androidx.camera.core.ImageCaptureException
import com.eva.scannerapp.presentation.composables.options.AnalysisOption

interface ImageScannerEvents {

	data object ToggleFlashMode : ImageScannerEvents

	data class OnAnalysisModeChange(val mode: AnalysisOption) : ImageScannerEvents

	data class OnImageCaptureSuccess(val image: Bitmap) : ImageScannerEvents

	data class OnImageCaptureFailed(val exception: ImageCaptureException) : ImageScannerEvents
}