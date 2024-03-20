package com.eva.scannerapp.presentation.feature_settings.composables

import android.widget.Toast
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import com.eva.scannerapp.R
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ClearCacheOption(
	onClearCaptureCache: () -> Unit,
	modifier: Modifier = Modifier
) {
	val context = LocalContext.current

	ListItem(
		headlineContent = {
			Text(
				text = stringResource(id = R.string.settings_clear_image_title),
				style = MaterialTheme.typography.titleSmall
			)
		},
		supportingContent = {
			Text(
				text = stringResource(id = R.string.settings_clear_image_desc),
				style = MaterialTheme.typography.bodySmall
			)
		},
		leadingContent = {
			Icon(
				imageVector = Icons.Default.CleaningServices,
				contentDescription = stringResource(id = R.string.settings_option_clear),
				modifier = Modifier.size(32.dp)
			)
		},
		trailingContent = {
			TextButton(
				onClick = {
					// clears memory cache
					try {
						context.imageLoader.memoryCache?.clear()
						// clears disk cache
						context.imageLoader.diskCache?.clear()
						//clear other
						onClearCaptureCache()

						Toast.makeText(context, R.string.settings_clear_image_title, Toast.LENGTH_SHORT)
							.show()

					} catch (e: Exception) {
						e.printStackTrace()
					}
				},
			) {
				Text(text = stringResource(id = R.string.settings_option_clear))
			}
		},
		colors = ListItemDefaults.colors(
			containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp),
			headlineColor = MaterialTheme.colorScheme.onSurface,
			supportingColor = MaterialTheme.colorScheme.onSurfaceVariant,
			leadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
			trailingIconColor = MaterialTheme.colorScheme.primary
		),
		modifier = modifier.clip(MaterialTheme.shapes.medium)
	)
}

@PreviewLightDark
@Composable
private fun ClearImageCacheOptionPreview() = ScannerAppTheme {
	ClearCacheOption(onClearCaptureCache = {})
}