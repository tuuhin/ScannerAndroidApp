package com.eva.scannerapp.presentation.composables.options

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.eva.scannerapp.R

enum class AnalysisOption(
	@StringRes val text: Int,
	@DrawableRes val painter: Int
) {
	BAR_CODE(
		text = R.string.bar_code_analyser,
		painter = R.drawable.ic_barcode
	),

	LABELS(
		text = R.string.labels_analyser,
		painter = R.drawable.ic_label_rope
	);
}