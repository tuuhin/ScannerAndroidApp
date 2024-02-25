package com.eva.scannerapp.presentation.feature_capture.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.presentation.feature_capture.states.AnalysisOption
import com.eva.scannerapp.presentation.util.preview.PreviewLightDarkApi33
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun AnalysisOptionsPicker(
	selectedOption: AnalysisOption,
	onOptionSelect: (AnalysisOption) -> Unit,
	modifier: Modifier = Modifier
) {
	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.Center,
		verticalAlignment = Alignment.CenterVertically
	) {
		AnalysisOption.entries.forEach { option ->
			FilterChip(
				selected = selectedOption == option,
				onClick = { onOptionSelect(option) },
				label = { Text(text = stringResource(id = option.text)) },
				leadingIcon = { Icon(imageVector = option.imageVector, contentDescription = null) },
				shape = MaterialTheme.shapes.medium,
				border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
				modifier = Modifier.padding(horizontal = 4.dp)
			)
		}
	}
}

@PreviewLightDarkApi33
@Composable
fun AnalysisOptionPickerPreview() = ScannerAppTheme {
	Surface {
		AnalysisOptionsPicker(
			selectedOption = AnalysisOption.BAR_CODE,
			onOptionSelect = {})
	}
}