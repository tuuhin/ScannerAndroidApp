package com.eva.scannerapp.presentation.feature_settings.composables

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.eva.scannerapp.R

fun LazyListScope.settingsAboutAvailableAnalyzer(
	context: Context,
	modifier: Modifier = Modifier
) {

	item {
		Text(
			text = "Analyzers",
			style = MaterialTheme.typography.titleMedium,
			color = MaterialTheme.colorScheme.onSurface,
			modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
		)
	}
	item {

		ListItem(
			headlineContent = {
				Text(text = stringResource(id = R.string.about_analyzers_bar_code_title))
			},
			supportingContent = {
				Text(
					text = stringResource(id = R.string.about_analyzers_bar_code_desc),
					maxLines = 2,
					overflow = TextOverflow.Ellipsis,
					style = MaterialTheme.typography.bodyMedium,
				)
			},
			trailingContent = {
				TextButton(
					onClick = {
						val uri = context.getString(R.string.about_analyzers_bar_code_url).toUri()
						val intent = Intent(Intent.ACTION_VIEW).apply {
							setDataAndType(uri, "text/html")
						}
						context.startActivity(intent)
					},
				) {
					Text(text = "More..")
				}
			},
			colors = ListItemDefaults.colors(
				headlineColor = MaterialTheme.colorScheme.onSurface,
				supportingColor = MaterialTheme.colorScheme.onSurfaceVariant
			),
			tonalElevation = 2.dp,
			modifier = modifier.clip(MaterialTheme.shapes.medium)
		)
	}
	item {

		ListItem(
			headlineContent = {
				Text(text = stringResource(id = R.string.about_analyzers_image_labeling_title))
			},
			supportingContent = {
				Text(
					text = stringResource(id = R.string.about_analyzers_image_labeling_desc),
					maxLines = 2,
					overflow = TextOverflow.Ellipsis,
					style = MaterialTheme.typography.bodyMedium,
				)
			},
			trailingContent = {
				TextButton(
					onClick = {
						val uri = context.getString(R.string.about_analyzers_image_labeling_url)
							.toUri()

						val intent = Intent(Intent.ACTION_VIEW).apply {
							setDataAndType(uri, "text/html")
						}
						context.startActivity(intent)
					},
				) {
					Text(text = "More..")
				}
			},
			colors = ListItemDefaults.colors(
				headlineColor = MaterialTheme.colorScheme.onSurface,
				supportingColor = MaterialTheme.colorScheme.onSurfaceVariant
			),
			tonalElevation = 2.dp,
			modifier = modifier.clip(MaterialTheme.shapes.medium)
		)
	}
}