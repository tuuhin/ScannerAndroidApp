package com.eva.scannerapp.presentation.feature_result.util

import android.net.Uri
import com.eva.scannerapp.domain.ml.models.RecognizedBarcode
import com.eva.scannerapp.domain.ml.models.RecognizedLabel
import com.eva.scannerapp.domain.ml.models.RecognizedModel

data class AnalysisScreenState(
	val fileUri: Uri? = null,
	val isAnalysing: Boolean = true,
	val analysisResult: List<RecognizedModel> = emptyList()
) {

	val recognizedLabelsIfAny: List<RecognizedLabel>
		get() = analysisResult.filterIsInstance<RecognizedLabel>()

	val firstRecognizedBarCode: RecognizedBarcode?
		get() = analysisResult.filterIsInstance<RecognizedBarcode>().firstOrNull()

}
