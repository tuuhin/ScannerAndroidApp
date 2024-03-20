package com.eva.scannerapp.presentation.util.preview

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.eva.scannerapp.domain.ml.models.RecognizedBarcode
import com.eva.scannerapp.domain.ml.models.RecognizedLabel

class BooleanPreviewParams : CollectionPreviewParameterProvider<Boolean>(listOf(true, false))

class RecognizedLabelsResultsPreviewParams :
	CollectionPreviewParameterProvider<List<RecognizedLabel>?>(
		listOf(
			null,
			PreviewFakes.FAKE_RECOGNIZED_LABEL_RESULTS_LIST
		)
	)

class RecognizedBarCodesPreviewParams : CollectionPreviewParameterProvider<RecognizedBarcode>(
	listOf(
		PreviewFakes.FAKE_QR_CODE_CONTACTS,
		PreviewFakes.FAKE_QR_CODE_EMAIL,
		PreviewFakes.FAKE_QR_CODE_GEO_POINT,
		PreviewFakes.FAKE_QR_CODE_PHONE,
		PreviewFakes.FAKE_QR_CODE_SMS,
		PreviewFakes.FAKE_BAR_CODE_TEXT,
		PreviewFakes.FAKE_QR_CODE_URL,
		PreviewFakes.FAKE_QR_CODE_URL
	)
)