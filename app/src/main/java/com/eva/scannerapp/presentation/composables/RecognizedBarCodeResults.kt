package com.eva.scannerapp.presentation.composables


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.R
import com.eva.scannerapp.domain.ml.models.RecognizedBarcode
import com.eva.scannerapp.domain.ml.util.BarCodeTypes
import com.eva.scannerapp.presentation.composables.barcode.BarCodeEmailResults
import com.eva.scannerapp.presentation.composables.barcode.BarCodeResultMaps
import com.eva.scannerapp.presentation.composables.barcode.BarCodeResultsContacts
import com.eva.scannerapp.presentation.composables.barcode.BarCodeResultsPhone
import com.eva.scannerapp.presentation.composables.barcode.BarCodeResultsRaw
import com.eva.scannerapp.presentation.composables.barcode.BarCodeResultsSms
import com.eva.scannerapp.presentation.composables.barcode.BarCodeTextResults
import com.eva.scannerapp.presentation.composables.barcode.BarCodeUrlResults
import com.eva.scannerapp.presentation.composables.barcode.BarCodeWifiResults
import com.eva.scannerapp.presentation.util.preview.RecognizedBarCodesPreviewParams
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun RecognizedBarCodeResults(
	model: RecognizedBarcode,
	modifier: Modifier = Modifier
) {
	val codeFormatText = remember { "Code Format :  ${model.codeFormat}" }

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
			text = codeFormatText,
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant
		)
		HorizontalDivider(
			color = MaterialTheme.colorScheme.outline,
			modifier = Modifier.padding(vertical = 2.dp)
		)
		when (model.type) {
			is BarCodeTypes.Email -> BarCodeEmailResults(type = model.type)
			is BarCodeTypes.UrlBookMark -> BarCodeUrlResults(type = model.type)
			is BarCodeTypes.WiFi -> BarCodeWifiResults(type = model.type)
			is BarCodeTypes.Text -> BarCodeTextResults(type = model.type)
			is BarCodeTypes.GeoPoint -> BarCodeResultMaps(type = model.type)
			is BarCodeTypes.Phone -> BarCodeResultsPhone(type = model.type)
			is BarCodeTypes.Sms -> BarCodeResultsSms(type = model.type)
			is BarCodeTypes.ContactInfo -> BarCodeResultsContacts(type = model.type)
			else -> BarCodeResultsRaw(rawValue = model.rawString ?: "")
		}
	}
}

@PreviewLightDark
@Composable
private fun RecognizedBarCodeResultsPreview(
	@PreviewParameter(RecognizedBarCodesPreviewParams::class)
	model: RecognizedBarcode
) = ScannerAppTheme {
	Surface {
		RecognizedBarCodeResults(
			model = model,
			modifier = Modifier.padding(16.dp)
		)
	}
}