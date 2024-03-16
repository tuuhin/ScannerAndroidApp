package com.eva.scannerapp.presentation.feature_capture.states

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.toComposeRect
import com.eva.scannerapp.domain.ml.models.RecognizedBarcode
import com.eva.scannerapp.domain.ml.models.RecognizedLabel
import com.eva.scannerapp.domain.ml.models.RecognizedModel


/**
 * Represents the state of the recognized item
 * @param showResults Determine is the bottom sheet should be shown
 * @param models The [List] of [RecognizedModel] recognized by the recognizer
 * @param savedModel A cache model to show analysis results mostly used in case a model have large
 * amount of data and only one of them is being shown
 * @property firstModel The first of the list of model
 * @property boundingRect The rect covering the recognized item
 */
data class RecognizedItemState(
	val showResults: Boolean = false,
	val models: List<RecognizedModel>? = null,
	val savedModel: RecognizedModel? = null
) {
	val firstModel: RecognizedModel?
		get() = models?.firstOrNull()

	val boundingRect: Rect?
		get() = firstModel?.bounding?.toComposeRect()

	val showCaptureBox: Boolean
		get() = !showResults

	val savedBarCodeModel: RecognizedBarcode?
		get() = savedModel as RecognizedBarcode?

	val labelModels: List<RecognizedLabel>
		get() = models?.filterIsInstance<RecognizedLabel>() ?: emptyList()

}
