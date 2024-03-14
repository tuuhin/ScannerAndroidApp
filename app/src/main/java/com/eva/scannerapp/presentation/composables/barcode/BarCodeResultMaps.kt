package com.eva.scannerapp.presentation.composables.barcode

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import java.text.DecimalFormat

@Composable
fun BarCodeResultMaps(
	type: BarCodeTypes.GeoPoint,
	modifier: Modifier = Modifier
) {
	val formatter = remember {
		DecimalFormat.getInstance()
	}
	val context = LocalContext.current

	Column(modifier = modifier) {
		SuggestionChip(
			onClick = {
				try {
					val intent = Intent(Intent.ACTION_VIEW).apply {
						data = Uri.fromParts("geo", "${type.lat},${type.long}", null)
					}
					context.startActivity(intent)
				} catch (e: ActivityNotFoundException) {
					e.printStackTrace()
					Toast.makeText(
						context,
						context.getString(R.string.bar_code_results_no_activity_to_start_with),
						Toast.LENGTH_SHORT
					).show()
				}
			},
			label = { Text(text = stringResource(id = R.string.bar_code_results_helper_map)) },
			icon = {
				Icon(
					imageVector = Icons.Outlined.Explore,
					contentDescription = stringResource(id = R.string.bar_code_results_helper_map),
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
		) {
			Column(
				verticalArrangement = Arrangement.SpaceBetween,
				modifier = Modifier.wrapContentHeight()
			) {
				type.lat?.let {
					Text(
						text = stringResource(id = R.string.barcode_results_title_latitude),
						style = MaterialTheme.typography.titleSmall,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)

				}
				type.long?.let {
					Text(
						text = stringResource(id = R.string.barcode_results_title_longitude),
						style = MaterialTheme.typography.titleSmall,
						color = MaterialTheme.colorScheme.onSurfaceVariant
					)
				}
			}
			Column(
				modifier = Modifier.align(Alignment.CenterVertically)
			) {
				type.lat?.let { lat ->
					Text(
						text = formatter.format(lat),
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.onSurface
					)
				}
				type.long?.let { long ->
					Text(
						text = formatter.format(long),
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.onSurface
					)
				}
			}
		}
	}
}

@PreviewLightDark
@Composable
private fun BarCodeResultMapPreview() = ScannerAppTheme {
	BarCodeResultMaps(
		type = PreviewFakes.FAKE_QR_CODE_GEO_POINT.type as BarCodeTypes.GeoPoint,
		modifier = Modifier
			.padding(horizontal = 16.dp, vertical = 8.dp)
	)

}