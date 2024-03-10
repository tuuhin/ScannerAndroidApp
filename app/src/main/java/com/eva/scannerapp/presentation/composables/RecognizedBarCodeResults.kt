package com.eva.scannerapp.presentation.composables

import BarCodeEmailResults
import BarCodeResultMaps
import BarCodeResultsContacts
import BarCodeResultsPhone
import BarCodeResultsSms
import BarCodeTextResults
import BarCodeUrlResults
import BarCodeWifiResults
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.R
import com.eva.scannerapp.domain.ml.models.RecognizedBarcode
import com.eva.scannerapp.domain.ml.util.BarCodeTypes
import com.eva.scannerapp.presentation.util.preview.PreviewFakes
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecognizedBarCodeResults(
	model: RecognizedBarcode,
	modifier: Modifier = Modifier
) {
	val showRawValue by remember { mutableStateOf(false) }

	Column(
		verticalArrangement = Arrangement.spacedBy(2.dp),
		modifier = modifier,
	) {
		Text(
			text = stringResource(id = R.string.results_sheet_bar_code_title),
			style = MaterialTheme.typography.headlineMedium,
			color = MaterialTheme.colorScheme.onSurface
		)
		Text(
			text = stringResource(id = R.string.results_sheet_bar_code_desc),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
		Spacer(modifier = Modifier.height(4.dp))
		Text(
			text = buildString {
				append("Code Type : ")
				append("  ")
				append("${model.codeFormat}")
			},
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
		HorizontalDivider(color = MaterialTheme.colorScheme.outline)
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(text = "Check Raw Value")
			CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
				IconButton(
					onClick = { showRawValue != showRawValue },
				) {
					if (showRawValue) Icon(
						imageVector = Icons.Default.ArrowDropUp,
						contentDescription = null
					)
					else Icon(
						imageVector = Icons.Default.ArrowDropDown,
						contentDescription = null
					)
				}
			}
		}

		AnimatedVisibility(
			visible = showRawValue,
			enter = expandVertically(),
			exit = shrinkVertically(),
			modifier = Modifier.fillMaxWidth()
		) {
			Text(
				text = model.rawString ?: "",
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
		}

		HorizontalDivider(color = MaterialTheme.colorScheme.outline)
		when (model.type) {
			is BarCodeTypes.Email -> BarCodeEmailResults(type = model.type)
			is BarCodeTypes.UrlBookMark -> BarCodeUrlResults(type = model.type)
			is BarCodeTypes.WiFi -> BarCodeWifiResults(type = model.type)
			is BarCodeTypes.Text -> BarCodeTextResults(type = model.type)
			is BarCodeTypes.GeoPoint -> BarCodeResultMaps(type = model.type)
			is BarCodeTypes.Phone -> BarCodeResultsPhone(type = model.type)
			is BarCodeTypes.Sms -> BarCodeResultsSms(type = model.type)
			is BarCodeTypes.ContactInfo -> BarCodeResultsContacts(type = model.type)
			else -> {
				Text(
					text = "Cannot identify Type!!",
					style = MaterialTheme.typography.titleMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
				Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
					Text(
						text = "Display Text",
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.onSurface
					)
					Text(
						text = model.displayText ?: "",
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.onSurface
					)
				}
			}
		}

	}
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
private fun RecognizedBarCodeResultsPreview() = ScannerAppTheme {
	Surface(shape = BottomSheetDefaults.ExpandedShape) {
		RecognizedBarCodeResults(
			model = PreviewFakes.FAKE_BAR_CODE_TEXT,
			modifier = Modifier.padding(16.dp)
		)
	}
}