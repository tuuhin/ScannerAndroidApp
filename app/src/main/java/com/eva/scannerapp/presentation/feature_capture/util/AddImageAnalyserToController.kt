package com.eva.scannerapp.presentation.feature_capture.util

import android.content.Context
import androidx.camera.view.CameraController
import androidx.core.content.ContextCompat
import com.eva.scannerapp.data.ml.feed.CameraFeedImageAnalyzer
import com.eva.scannerapp.domain.ml.models.RecognizedModel

fun CameraController.addImageAnalyzer(
	context: Context,
	analyser: CameraFeedImageAnalyzer<out RecognizedModel>,
) {
	if (!isImageAnalysisEnabled) return
	val executor = ContextCompat.getMainExecutor(context)
	// set the new analyzer
	setImageAnalysisAnalyzer(executor, analyser)
}