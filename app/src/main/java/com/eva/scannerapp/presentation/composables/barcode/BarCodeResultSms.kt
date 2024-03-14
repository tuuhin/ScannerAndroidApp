package com.eva.scannerapp.presentation.composables.barcode

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.domain.ml.util.BarCodeTypes
import com.eva.scannerapp.presentation.util.preview.PreviewFakes
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun BarCodeResultsSms(
	type: BarCodeTypes.Sms,
	modifier: Modifier = Modifier
) {
	val context = LocalContext.current

	Column(modifier = modifier) {
		SuggestionChip(
			onClick = {

				val hasFeature = context.packageManager
					.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)
				if (hasFeature) {
					val intent = Intent(Intent.ACTION_SENDTO).apply {
						data = Uri.fromParts("smsto", type.phoneNumber, null)
						putExtra("sms_body", type.message)
					}
					context.startActivity(intent)
				}
			},
			label = { Text(text = "Send SMS") },
			icon = {
				Icon(
					imageVector = Icons.AutoMirrored.Outlined.Message,
					contentDescription = null,
				)
			},
			shape = MaterialTheme.shapes.large,
			colors = SuggestionChipDefaults
				.suggestionChipColors(
					iconContentColor = MaterialTheme.colorScheme.primary,
					containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
				)
		)
		Row(
			horizontalArrangement = Arrangement.spacedBy(32.dp),
		) {
			Column(
				verticalArrangement = Arrangement.SpaceBetween,
				modifier = Modifier.wrapContentHeight()
			) {
				type.phoneNumber?.let {
					Text(
						text = "Phone Number",
						style = MaterialTheme.typography.labelLarge,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
				}
				type.message?.let {
					Text(
						text = "Body",
						style = MaterialTheme.typography.labelLarge,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
				}
			}
			Column(
				modifier = Modifier.align(Alignment.CenterVertically)
			) {
				type.phoneNumber?.let { phNumber ->
					Text(
						text = phNumber,
						style = MaterialTheme.typography.labelMedium,
						color = MaterialTheme.colorScheme.onSurface
					)
				}
				type.message?.let { body ->
					Text(
						text = body,
						style = MaterialTheme.typography.labelMedium,
						color = MaterialTheme.colorScheme.onSurface
					)
				}
			}
		}
	}
}

@PreviewLightDark
@Composable
private fun BarCodeResultsSmsPreview() = ScannerAppTheme {
	Surface {
		BarCodeResultsSms(
			type = PreviewFakes.FAKE_QR_CODE_SMS.type as BarCodeTypes.Sms,
			modifier = Modifier
				.padding(horizontal = 16.dp, vertical = 8.dp)
		)
	}
}