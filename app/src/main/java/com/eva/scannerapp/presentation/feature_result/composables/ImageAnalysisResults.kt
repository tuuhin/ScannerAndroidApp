package com.eva.scannerapp.presentation.feature_result.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.eva.scannerapp.R
import com.eva.scannerapp.domain.ml.models.RecognizedBarcode
import com.eva.scannerapp.domain.ml.models.RecognizedLabel
import com.eva.scannerapp.presentation.composables.RecognizedBarCodeResults
import com.eva.scannerapp.presentation.composables.RecognizedLabelResults
import com.eva.scannerapp.presentation.composables.options.AnalysisOption

@Composable
fun ImageAnalysisResult(
	isAnalyzing: Boolean,
	modifier: Modifier = Modifier,
	option: AnalysisOption = AnalysisOption.BAR_CODE,
	firstRecognizedBarcode: RecognizedBarcode? = null,
	recognizedLabels: List<RecognizedLabel> = emptyList(),
) {

	val paddingModifier = Modifier.padding(
		vertical = dimensionResource(R.dimen.modal_sheet_padding),
		horizontal = dimensionResource(R.dimen.scaffold_padding)
	)

	AnimatedContent(
		targetState = option,
		label = "Preparing Results",
		transitionSpec = {
			slideIntoContainer(
				towards = AnimatedContentTransitionScope.SlideDirection.Up,
				animationSpec = tween(durationMillis = 200)
			) togetherWith slideOutOfContainer(
				towards = AnimatedContentTransitionScope.SlideDirection.Down,
				animationSpec = tween(durationMillis = 200)
			)

		},
		modifier = modifier
	) { opt ->
		when (opt) {
			AnalysisOption.BAR_CODE -> if (isAnalyzing) PreparingAnalysisResults()
			else firstRecognizedBarcode?.let { model ->
				RecognizedBarCodeResults(
					model = model,
					modifier = Modifier.then(paddingModifier)
				)
			} ?: BarCodeNotFound()


			AnalysisOption.LABELS -> RecognizedLabelResults(
				isAnalyzing = isAnalyzing,
				results = recognizedLabels,
				modifier = Modifier.then(paddingModifier)
			)
		}
	}
}