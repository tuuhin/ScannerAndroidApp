package com.eva.scannerapp.presentation.feature_capture.states

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.ui.graphics.vector.ImageVector
import com.eva.scannerapp.R

enum class AnalysisOption(@StringRes val text: Int, val imageVector: ImageVector) {
	BAR_CODE(R.string.bar_code_analyser, Icons.Outlined.QrCode),
	LABELS(R.string.labels_analyser, Icons.AutoMirrored.Outlined.Label)
}