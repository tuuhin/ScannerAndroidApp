package com.eva.scannerapp.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.presentation.composables.options.AnalysisOption
import com.eva.scannerapp.presentation.util.preview.PreviewLightDarkApi33
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun AnalysisOptionsPicker(
	selectedOption: AnalysisOption,
	onOptionSelect: (AnalysisOption) -> Unit,
	modifier: Modifier = Modifier,
	spacing: Arrangement.Horizontal = Arrangement.Center
) {
	Row(
		modifier = modifier,
		horizontalArrangement = spacing,
		verticalAlignment = Alignment.CenterVertically
	) {
		AnalysisOption.entries.forEach { option ->
			FilterChip(
				selected = selectedOption == option,
				onClick = { onOptionSelect(option) },
				label = {
					Text(
						text = stringResource(id = option.text),
						style = MaterialTheme.typography.titleSmall,
						modifier = Modifier.padding(horizontal = 4.dp)
					)
				},
				leadingIcon = {
					if (selectedOption == option)
						Icon(imageVector = Icons.Default.Check, contentDescription = null)
					else Icon(painter = painterResource(option.painter), contentDescription = null)
				},
				shape = MaterialTheme.shapes.extraLarge,
				border = FilterChipDefaults.filterChipBorder(
					enabled = true,
					selected = selectedOption == option
				),
				colors = FilterChipDefaults.filterChipColors(
					iconColor = MaterialTheme.colorScheme.primary,
					labelColor = MaterialTheme.colorScheme.onSurface,
					selectedContainerColor = MaterialTheme.colorScheme.inversePrimary
						.copy(alpha = .35f),
					selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
					selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
				),
				modifier = Modifier
					.defaultMinSize(minHeight = 38.dp)
					.padding(horizontal = 4.dp),
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
			onOptionSelect = {},
		)
	}
}