package com.eva.scannerapp.presentation.feature_capture.states

import com.eva.scannerapp.presentation.composables.options.AnalysisOption

data class CameraScreenState(
	val isFlashOn: Boolean = false,
	val analysisOption: AnalysisOption = AnalysisOption.BAR_CODE,
)
