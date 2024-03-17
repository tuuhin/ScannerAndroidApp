package com.eva.scannerapp.presentation.feature_capture.util

import android.content.Context
import android.graphics.ImageFormat
import androidx.camera.core.ImageCapture
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController

object AppCameraController {
	private const val USE_CASES = CameraController.IMAGE_CAPTURE or CameraController.IMAGE_ANALYSIS

	private val analysisResolutionSelector = ResolutionSelector.Builder()
		.setAspectRatioStrategy(AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY)
		.setResolutionStrategy(ResolutionStrategy.HIGHEST_AVAILABLE_STRATEGY)
		.build()

	private val captureResolutionSelector = ResolutionSelector.Builder()
		.setAspectRatioStrategy(AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY)
		.build()

	fun getController(context: Context): LifecycleCameraController {
		return LifecycleCameraController(context).apply {
			setEnabledUseCases(USE_CASES)
			isTapToFocusEnabled = true
			imageAnalysisOutputImageFormat = ImageFormat.YUV_420_888
			imageCaptureMode = ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
			imageCaptureResolutionSelector = captureResolutionSelector
			// Image analysis config
			imageAnalysisResolutionSelector = analysisResolutionSelector
		}
	}
}