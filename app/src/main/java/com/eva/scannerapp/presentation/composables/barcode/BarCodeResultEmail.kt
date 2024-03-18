package com.eva.scannerapp.presentation.composables.barcode

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.R
import com.eva.scannerapp.domain.ml.util.BarCodeTypes
import com.eva.scannerapp.presentation.util.preview.PreviewFakes
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun BarCodeEmailResults(
	type: BarCodeTypes.Email,
	modifier: Modifier = Modifier,
	titleStyle: TextStyle = MaterialTheme.typography.labelLarge,
	valueStyle: TextStyle = MaterialTheme.typography.bodyMedium,
	titleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
	valueColor: Color = MaterialTheme.colorScheme.onSurface,
) {
	val context = LocalContext.current

	Column(modifier = modifier) {
		SuggestionChip(
			onClick = {
				try {
					val intent = Intent(Intent.ACTION_SENDTO).apply {
						putExtra(Intent.EXTRA_TITLE, type.subject)
						putExtra(Intent.EXTRA_TEXT, type.body)
						putExtra(Intent.EXTRA_EMAIL, type.address)
					}
					context.startActivity(intent)

				} catch (e: ActivityNotFoundException) {
					Toast.makeText(
						context,
						context.getString(R.string.bar_code_results_no_activity_to_start_with),
						Toast.LENGTH_SHORT
					).show()
					e.printStackTrace()
				}

			},
			label = { Text(text = stringResource(id = R.string.bar_code_results_helper_send_email)) },
			icon = {
				Icon(
					imageVector = Icons.Outlined.Email,
					contentDescription = stringResource(id = R.string.bar_code_results_helper_send_email),
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
				modifier = Modifier.wrapContentHeight()
			) {
				type.address?.let {
					Text(
						text = stringResource(id = R.string.barcode_results_title_email_address),
						style = titleStyle,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
				}
				type.subject?.let {
					Text(
						text = stringResource(id = R.string.barcode_results_title_email_subject),
						style = titleStyle,
						color = titleColor,
					)
				}
				Text(
					text = stringResource(id = R.string.barcode_results_title_email_body),
					style = titleStyle,
					color = titleColor,
				)
			}
			Column(
				modifier = Modifier.align(Alignment.CenterVertically)
			) {
				type.address?.let {
					Text(
						text = type.address,
						style = valueStyle,
						color = valueColor,
					)
				}
				type.subject?.let {
					Text(
						text = type.subject,
						style = valueStyle,
						color = valueColor,
					)
				}
				type.body?.let {
					Text(
						text = type.body,
						style = valueStyle,
						color = valueColor,
						maxLines = 2,
						overflow = TextOverflow.Ellipsis
					)
				}
			}
		}
	}
}

@PreviewLightDark
@Composable
private fun BarCodeUrlResultsPreview() = ScannerAppTheme {
	Surface {
		BarCodeEmailResults(
			type = PreviewFakes.FAKE_QR_CODE_EMAIL.type as BarCodeTypes.Email,
			modifier = Modifier
				.padding(horizontal = 16.dp, vertical = 8.dp)
		)
	}
}