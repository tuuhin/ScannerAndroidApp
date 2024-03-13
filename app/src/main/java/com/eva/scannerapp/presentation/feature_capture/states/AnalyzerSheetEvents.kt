package com.eva.scannerapp.presentation.feature_capture.states

import com.eva.scannerapp.domain.ml.models.RecognizedModel

interface AnalyzerSheetEvents {

	data object CloseBottomSheet : AnalyzerSheetEvents

	data object OpenBottomSheet : AnalyzerSheetEvents

	data class OnSelectBoundingRect(val model: RecognizedModel) : AnalyzerSheetEvents
}