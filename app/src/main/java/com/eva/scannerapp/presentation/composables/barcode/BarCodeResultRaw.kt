package com.eva.scannerapp.presentation.composables.barcode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.R
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun BarCodeResultsRaw(
	rawValue: String,
	modifier: Modifier = Modifier
) {
	Column(modifier = modifier) {
		Text(
			text = stringResource(id = R.string.barcode_cannot_identify_type),
			style = MaterialTheme.typography.titleMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
		Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
			Text(
				text = stringResource(id = R.string.barcode_display_text),
				style = MaterialTheme.typography.bodyLarge,
				color = MaterialTheme.colorScheme.onSurface
			)
			Text(
				text = rawValue,
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurface
			)
		}
	}
}

@PreviewLightDark
@Composable
private fun BarCodeResultsRawPreview() = ScannerAppTheme {
	Surface {
		BarCodeResultsRaw(rawValue = "This is some value")
	}
}