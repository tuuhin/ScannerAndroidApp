package com.eva.scannerapp.presentation.feature_result.util

import com.eva.scannerapp.presentation.composables.options.AnalysisOption

sealed interface AnalysisScreenEvents {

	data class OnAnalysisOptionSwitched(val option: AnalysisOption) : AnalysisScreenEvents

}