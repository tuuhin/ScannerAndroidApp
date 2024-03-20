package com.eva.scannerapp.presentation.composables.barcode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.R
import com.eva.scannerapp.domain.ml.util.BarCodeTypes
import com.eva.scannerapp.presentation.util.preview.PreviewFakes
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun BarCodeWifiResults(
	type: BarCodeTypes.WiFi,
	modifier: Modifier = Modifier,
	titleStyle: TextStyle = MaterialTheme.typography.labelLarge,
	valueStyle: TextStyle = MaterialTheme.typography.bodyMedium,
	titleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
	valueColor: Color = MaterialTheme.colorScheme.onSurface,
) {
	Column(modifier = modifier) {
		SuggestionChip(
			onClick = {
				// TODO: Configure it Latter

			},
			label = { Text(text = stringResource(id = R.string.barcode_results_helper_join_network)) },
			icon = {
				Icon(
					imageVector = Icons.Default.Wifi,
					contentDescription = stringResource(id = R.string.barcode_results_helper_join_network),
				)
			},
			enabled = false,
			shape = MaterialTheme.shapes.large,
			colors = SuggestionChipDefaults.suggestionChipColors(
				iconContentColor = MaterialTheme.colorScheme.primary,
				containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
			)
		)
		Row(
			horizontalArrangement = Arrangement
				.spacedBy(dimensionResource(id = R.dimen.barcode_resutls_spacing)),
		) {
			Column(
				verticalArrangement = Arrangement.SpaceBetween,
				modifier = Modifier.wrapContentHeight()
			) {
				Text(
					text = stringResource(id = R.string.barcode_results_title_wifi_network_name),
					style = titleStyle,
					color = titleColor
				)
				Text(
					text = stringResource(id = R.string.barcode_results_title_wifi_password),
					style = titleStyle,
					color = titleColor,
				)
			}
			Column(
				modifier = Modifier.align(Alignment.CenterVertically)
			) {
				Text(
					text = type.ssid ?: "",
					style = valueStyle,
					color = valueColor
				)
				Text(
					text = type.password ?: "",
					style = valueStyle,
					color = valueColor
				)
			}
		}
	}
}

@PreviewLightDark
@Composable
fun BarCodeWifiResultsPreview() = ScannerAppTheme {
	Surface {
		BarCodeWifiResults(
			type = PreviewFakes.FAKE_QR_CODE_WIFI.type as BarCodeTypes.WiFi,
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp, vertical = 8.dp)
		)
	}
}