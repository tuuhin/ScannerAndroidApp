package com.eva.scannerapp.presentation.composables.barcode

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
fun BarCodeResultsSms(
	type: BarCodeTypes.Sms,
	modifier: Modifier = Modifier,
	titleStyle: TextStyle = MaterialTheme.typography.labelLarge,
	valueStyle: TextStyle = MaterialTheme.typography.bodyMedium,
	titleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
	valueColor: Color = MaterialTheme.colorScheme.onSurface,
) {
	val context = LocalContext.current

	val canSendSms = remember(context) {
		context.packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)
	}

	Column(modifier = modifier) {
		SuggestionChip(
			onClick = {

				if (!canSendSms) return@SuggestionChip

				try {
					val intent = Intent(Intent.ACTION_SENDTO).apply {
						data = Uri.fromParts("smsto", type.phoneNumber, null)
						putExtra("sms_body", type.message)
					}
					context.startActivity(intent)

				} catch (e: ActivityNotFoundException) {
					e.printStackTrace()
					Toast.makeText(
						context,
						context.getString(R.string.activity_not_found_error),
						Toast.LENGTH_SHORT
					).show()
					e.printStackTrace()
				}

			},
			label = { Text(text = stringResource(id = R.string.barcode_results_helper_send_sms)) },
			icon = {
				Icon(
					imageVector = Icons.AutoMirrored.Outlined.Message,
					contentDescription = null,
				)
			},
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
				modifier = Modifier.width(IntrinsicSize.Max)
			) {
				type.phoneNumber?.let {
					Text(
						text = stringResource(id = R.string.barcode_results_title_sms_ph_number),
						style = titleStyle,
						color = titleColor
					)
				}
				type.message?.let {
					Text(
						text = stringResource(id = R.string.barcode_results_title_sms_text),
						style = titleStyle,
						color = titleColor,
					)
				}
			}
			Column(
				modifier = Modifier.align(Alignment.CenterVertically)
			) {
				type.phoneNumber?.let { phNumber ->
					Text(
						text = phNumber,
						style = valueStyle,
						color = valueColor
					)
				}
				type.message?.let { body ->
					Text(
						text = body,
						style = valueStyle,
						color = valueColor
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