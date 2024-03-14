package com.eva.scannerapp.presentation.feature_capture.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.eva.scannerapp.R
import com.eva.scannerapp.domain.ml.models.RecognizedBarcode
import com.eva.scannerapp.domain.ml.models.RecognizedLabel
import com.eva.scannerapp.presentation.composables.RecognizedBarCodeResults
import com.eva.scannerapp.presentation.composables.RecognizedLabelResults
import com.eva.scannerapp.presentation.composables.options.AnalysisOption
import com.eva.scannerapp.presentation.util.preview.PreviewFakes
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecognizedResultsBottomSheet(
	option: AnalysisOption,
	modifier: Modifier = Modifier,
	barCodeModel: RecognizedBarcode? = null,
	labelModels: List<RecognizedLabel> = emptyList(),
	onDismissRequest: () -> Unit = {},
) {
	ModalBottomSheet(
		onDismissRequest = onDismissRequest,
		shape = BottomSheetDefaults.ExpandedShape,
		windowInsets = BottomSheetDefaults.windowInsets,
		modifier = modifier
	) {
		when (option) {
			AnalysisOption.BAR_CODE -> barCodeModel?.let { model ->
				RecognizedBarCodeResults(
					model = model,
					modifier = Modifier.padding(
						vertical = dimensionResource(R.dimen.modal_sheet_padding),
						horizontal = dimensionResource(R.dimen.scaffold_padding)
					)
				)
			}

			AnalysisOption.LABELS -> RecognizedLabelResults(
				results = labelModels,
				modifier = Modifier.padding(
					vertical = dimensionResource(R.dimen.modal_sheet_padding),
					horizontal = dimensionResource(R.dimen.scaffold_padding)
				)
			)
		}
	}
}

@PreviewLightDark
@Composable
private fun RecognizedResultsBottomSheetPreview() = ScannerAppTheme {
	RecognizedResultsBottomSheet(
		barCodeModel = PreviewFakes.FAKE_BAR_CODE_TEXT,
		labelModels = emptyList(),
		option = AnalysisOption.BAR_CODE
	)
}