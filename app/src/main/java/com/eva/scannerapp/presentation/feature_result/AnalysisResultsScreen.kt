package com.eva.scannerapp.presentation.feature_result

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.areNavigationBarsVisible
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.eva.scannerapp.R
import com.eva.scannerapp.presentation.composables.AnalysisOptionsPicker
import com.eva.scannerapp.presentation.composables.options.AnalysisOption
import com.eva.scannerapp.presentation.feature_result.composables.ImageAnalysisResult
import com.eva.scannerapp.presentation.feature_result.util.AnalysisScreenEvents
import com.eva.scannerapp.presentation.feature_result.util.AnalysisScreenState
import com.eva.scannerapp.presentation.util.LocalSnackBarStateProvider
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@OptIn(
	ExperimentalMaterial3Api::class,
	ExperimentalLayoutApi::class
)
@Composable
fun ResultsScreen(
	resultsState: AnalysisScreenState,
	analysisOption: AnalysisOption,
	onResultEvent: (AnalysisScreenEvents) -> Unit,
	modifier: Modifier = Modifier,
	navigation: @Composable () -> Unit = {},
) {

	val context = LocalContext.current
	val lifeCycleOwner = LocalLifecycleOwner.current
	val snackBarHostState = LocalSnackBarStateProvider.current
	val view = LocalView.current

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = stringResource(id = R.string.results_route_title)) },
				navigationIcon = navigation,
			)
		},
		snackbarHost = { SnackbarHost(snackBarHostState) },
		contentWindowInsets = if (WindowInsets.areNavigationBarsVisible)
			WindowInsets.navigationBars else WindowInsets.statusBars,
		modifier = modifier,
	) { scPadding ->
		Column(
			modifier = Modifier
				.padding(scPadding)
				.padding(horizontal = dimensionResource(id = R.dimen.scaffold_padding)),
			verticalArrangement = Arrangement.spacedBy(4.dp)
		) {
			Surface(
				modifier = Modifier
					.fillMaxWidth()
					.weight(.7f)
					.animateContentSize(tween(durationMillis = 200, easing = FastOutSlowInEasing)),
				shape = MaterialTheme.shapes.medium,
				color = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
			) {
				if (view.isInEditMode) return@Surface
				AsyncImage(
					model = ImageRequest.Builder(context)
						.data(resultsState.fileUri)
						.lifecycle(lifeCycleOwner)
						.build(),
					contentDescription = "preview for the result image",
					imageLoader = context.imageLoader,
					contentScale = ContentScale.Fit,
					modifier = Modifier
						.clip(MaterialTheme.shapes.medium)
						.align(Alignment.CenterHorizontally)
				)
			}
			HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
			AnalysisOptionsPicker(
				selectedOption = analysisOption,
				onOptionSelect = { option ->
					onResultEvent(AnalysisScreenEvents.OnAnalysisOptionSwitched(option))
				},
				modifier = Modifier.align(Alignment.CenterHorizontally)
			)
			HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
			ImageAnalysisResult(
				isAnalyzing = resultsState.isAnalysing,
				option = analysisOption,
				recognizedLabels = resultsState.recognizedLabelsIfAny,
				firstRecognizedBarcode = resultsState.firstRecognizedBarCode,
				modifier = Modifier.padding(vertical = 8.dp),
			)
		}
	}
}

@PreviewLightDark
@Composable
private fun AnalysisResultsScreenPreview() = ScannerAppTheme {
	ResultsScreen(
		resultsState = AnalysisScreenState(),
		analysisOption = AnalysisOption.BAR_CODE,
		onResultEvent = {},
		navigation = {
			Icon(
				imageVector = Icons.AutoMirrored.Filled.ArrowBack,
				contentDescription = "Back Arrow"
			)
		}
	)
}