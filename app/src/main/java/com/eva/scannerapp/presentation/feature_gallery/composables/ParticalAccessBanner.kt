package com.eva.scannerapp.presentation.feature_gallery.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.R
import com.eva.scannerapp.presentation.feature_gallery.state.GalleryPermissionState
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun PartialAccessBanner(
	onPermissionChanged: (GalleryPermissionState) -> Unit,
	modifier: Modifier = Modifier
) {
	Card(
		modifier,
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.tertiaryContainer,
			contentColor = MaterialTheme.colorScheme.onTertiaryContainer
		)
	) {
		Row(
			modifier = Modifier.padding(all = dimensionResource(id = R.dimen.scaffold_padding)),
			horizontalArrangement = Arrangement.spacedBy(4.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = stringResource(id = R.string.read_media_images_partial_access),
				style = MaterialTheme.typography.labelSmall,
				modifier = Modifier.weight(.7f)
			)
			ReadGalleryPermission(
				onGalleryPermission = onPermissionChanged,
				shape = ButtonDefaults.textShape,
				colors = ButtonDefaults
					.textButtonColors(contentColor = MaterialTheme.colorScheme.onTertiaryContainer),
				contentPadding = ButtonDefaults.TextButtonContentPadding,
			) {
				Text(text = "Manage", style = MaterialTheme.typography.labelMedium)
			}
		}
	}
}

@PreviewLightDark
@Composable
private fun PartialAccessBannerPreview() = ScannerAppTheme {
	PartialAccessBanner(onPermissionChanged = {})
}