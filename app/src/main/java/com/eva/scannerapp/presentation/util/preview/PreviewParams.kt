package com.eva.scannerapp.presentation.util.preview

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.eva.scannerapp.domain.ml.models.RecognizedLabel

class BooleanPreviewParams : CollectionPreviewParameterProvider<Boolean>(listOf(true, false))

class RecognizedLabelsResultsPreviewParams :
	CollectionPreviewParameterProvider<List<RecognizedLabel>?>(
		listOf(
			null,
			PreviewFakes.FAKE_RECOGNIZED_LABEL_RESULTS_LIST
		)
	)
