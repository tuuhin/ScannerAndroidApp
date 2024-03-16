package com.eva.scannerapp.presentation.composables

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.R
import com.eva.scannerapp.domain.ml.models.RecognizedLabel
import com.eva.scannerapp.presentation.util.preview.RecognizedLabelsResultsPreviewParams
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecognizedLabelResults(
	modifier: Modifier = Modifier,
	isAnalyzing: Boolean = false,
	results: List<RecognizedLabel>? = emptyList(),
) {

	val isNoResults by remember(results) {
		derivedStateOf { results.isNullOrEmpty() }
	}

	Column(
		verticalArrangement = Arrangement.spacedBy(4.dp),
		modifier = modifier,
	) {
		Text(
			text = stringResource(id = R.string.analysing_labels_sheet_title),
			style = MaterialTheme.typography.headlineMedium,
			color = MaterialTheme.colorScheme.onSurface
		)
		Text(
			text = stringResource(id = R.string.analysing_labels_sheet_desc),
			style = MaterialTheme.typography.labelLarge,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
		Crossfade(
			targetState = isAnalyzing,
			label = "Is Ml analyzing",
			animationSpec = tween(durationMillis = 150, easing = EaseIn)
		) { isLoading ->
			if (isLoading) LinearProgressIndicator(
				trackColor = MaterialTheme.colorScheme.surfaceVariant,
				color = MaterialTheme.colorScheme.secondary,
				strokeCap = StrokeCap.Round,
				modifier = Modifier
					.padding(vertical = 8.dp)
					.fillMaxWidth()
			)
			else if (isNoResults) Text(
				text = "No Labels are recognized in the image",
				style = MaterialTheme.typography.titleMedium,
				color = MaterialTheme.colorScheme.onSurface
			)
			else FlowRow(
				horizontalArrangement = Arrangement.spacedBy(4.dp),
				maxItemsInEachRow = 4
			) {
				results?.forEach { label ->
					SuggestionChip(
						onClick = {},
						label = { Text(text = label.text) },
						border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
						colors = SuggestionChipDefaults.suggestionChipColors(
							labelColor = MaterialTheme.colorScheme.onPrimaryContainer,
							containerColor = MaterialTheme.colorScheme.primaryContainer
						),
						shape = MaterialTheme.shapes.large
					)
				}
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun RecognizedLabelsResultsPreview(
	@PreviewParameter(RecognizedLabelsResultsPreviewParams::class)
	results: List<RecognizedLabel>?
) = ScannerAppTheme {
	Surface(shape = BottomSheetDefaults.ExpandedShape) {
		RecognizedLabelResults(
			isAnalyzing = false,
			results = results,
			modifier = Modifier.padding(20.dp)
		)
	}
}