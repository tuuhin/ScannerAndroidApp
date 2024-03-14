package com.eva.scannerapp.presentation.composables.barcode

import android.content.ClipData
import android.content.ClipboardManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import com.eva.scannerapp.R
import com.eva.scannerapp.domain.ml.util.BarCodeTypes
import com.eva.scannerapp.presentation.util.preview.PreviewFakes
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun BarCodeTextResults(
	type: BarCodeTypes.Text,
	modifier: Modifier = Modifier
) {
	val context = LocalContext.current

	Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
		SuggestionChip(
			onClick = {
				val clipManager = context.getSystemService<ClipboardManager>()
				val primaryClip = clipManager?.hasPrimaryClip() ?: false

				if (primaryClip) clipManager?.primaryClip?.addItem(ClipData.Item(type.text))
				else clipManager?.setPrimaryClip(ClipData.newPlainText(type.text, type.text))
			},
			label = { Text(text = stringResource(id = R.string.bar_code_results_helper_copy)) },
			icon = {
				Icon(
					imageVector = Icons.Default.CopyAll,
					contentDescription = stringResource(id = R.string.bar_code_results_helper_copy),
				)
			},
			shape = MaterialTheme.shapes.large,
			colors = SuggestionChipDefaults.suggestionChipColors(
				iconContentColor = MaterialTheme.colorScheme.primary,
				containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
			)
		)
		Row(
			horizontalArrangement = Arrangement.spacedBy(32.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = stringResource(id = R.string.barcode_results_title_text),
				style = MaterialTheme.typography.labelLarge,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
			Text(
				text = type.text, style = MaterialTheme.typography.labelMedium,
				color = MaterialTheme.colorScheme.onSurface
			)
		}
	}
}

@PreviewLightDark
@Composable
private fun BarCodeTextResultsPreview() = ScannerAppTheme {
	Surface {
		BarCodeTextResults(
			type = PreviewFakes.FAKE_BAR_CODE_TEXT.type as BarCodeTypes.Text,
			modifier = Modifier
				.padding(horizontal = 16.dp, vertical = 8.dp)
		)
	}
}