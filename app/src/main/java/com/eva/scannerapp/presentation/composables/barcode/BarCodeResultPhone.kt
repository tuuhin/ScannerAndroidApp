package com.eva.scannerapp.presentation.composables.barcode

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Phone
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
import com.eva.scannerapp.R
import com.eva.scannerapp.domain.ml.util.BarCodeTypes
import com.eva.scannerapp.presentation.util.preview.PreviewFakes
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun BarCodeResultsPhone(
	type: BarCodeTypes.Phone,
	modifier: Modifier = Modifier
) {
	val context = LocalContext.current

	Column(modifier = modifier) {
		SuggestionChip(
			onClick = {
				try {
					val intent = Intent(Intent.ACTION_DIAL).apply {
						data = Uri.fromParts("tel", type.number, null)
					}
					context.startActivity(intent)
				} catch (e: ActivityNotFoundException) {
					e.printStackTrace()
					Toast.makeText(
						context,
						context.getString(R.string.bar_code_results_no_activity_to_start_with),
						Toast.LENGTH_SHORT
					).show()
					e.printStackTrace()
				}
			},
			label = { Text(text = stringResource(id = R.string.bar_code_results_helper_call)) },
			icon = {
				Icon(
					imageVector = Icons.Outlined.Phone,
					contentDescription = stringResource(id = R.string.bar_code_results_helper_call),
				)
			},
			shape = MaterialTheme.shapes.large,
			colors = SuggestionChipDefaults
				.suggestionChipColors(
					iconContentColor = MaterialTheme.colorScheme.primary,
					containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
				)
		)
		type.number?.let { number ->
			Row(
				horizontalArrangement = Arrangement.spacedBy(32.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(
					text = stringResource(id = R.string.barcode_results_title_number),
					style = MaterialTheme.typography.titleSmall,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
				Text(
					text = number,
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
		}
	}
}

@PreviewLightDark
@Composable
private fun BarCodeResultsPhonePreview() = ScannerAppTheme {
	Surface {
		BarCodeResultsPhone(
			type = PreviewFakes.FAKE_QR_CODE_PHONE.type as BarCodeTypes.Phone,
			modifier = Modifier
				.padding(horizontal = 16.dp, vertical = 8.dp)
		)
	}
}