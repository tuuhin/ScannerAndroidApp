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
import androidx.compose.material.icons.filled.Public
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.R
import com.eva.scannerapp.domain.ml.util.BarCodeTypes
import com.eva.scannerapp.presentation.util.preview.PreviewFakes
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun BarCodeUrlResults(
	type: BarCodeTypes.UrlBookMark,
	modifier: Modifier = Modifier,
	titleStyle: TextStyle = MaterialTheme.typography.labelLarge,
	valueStyle: TextStyle = MaterialTheme.typography.bodyMedium,
	titleColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
	valueColor: Color = MaterialTheme.colorScheme.onSurface,
) {
	val context = LocalContext.current

	Column(
		modifier = modifier
	) {
		SuggestionChip(
			onClick = {
				type.url?.let { site ->
					try {
						val intent = Intent(Intent.ACTION_VIEW).apply {
							data = Uri.parse(site)
							putExtra(Intent.EXTRA_TITLE, type.title)
							putExtra(Intent.EXTRA_TEXT, site)
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
				}
			},
			label = { Text(text = stringResource(id = R.string.barcode_results_helper_open_url)) },
			icon = {
				Icon(
					imageVector = Icons.Default.Public,
					contentDescription = stringResource(id = R.string.barcode_results_helper_open_url),
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
				type.title?.let {
					Text(
						text = stringResource(id = R.string.barcode_results_title_url_bookmark_title),
						style = titleStyle,
						color = titleColor
					)
				}
				type.url?.let {
					Text(
						text = stringResource(id = R.string.barcode_results_title_url_bookmark_url),
						style = titleStyle,
						color = titleColor
					)
				}
			}
			Column(
				modifier = Modifier.align(Alignment.CenterVertically)
			) {
				type.title?.let { title ->
					Text(
						text = title,
						style = valueStyle,
						color = valueColor
					)
				}
				type.url?.let { url ->
					Text(
						text = url,
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
private fun BarCodeUrlResultsPreview() = ScannerAppTheme {
	Surface {
		BarCodeUrlResults(
			type = PreviewFakes.FAKE_QR_CODE_URL.type as BarCodeTypes.UrlBookMark,
			modifier = Modifier
				.padding(horizontal = 16.dp, vertical = 8.dp)
		)
	}
}