package com.eva.scannerapp.presentation.feature_gallery.composables

import android.Manifest
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.eva.scannerapp.R
import com.eva.scannerapp.presentation.composables.CheckSinglePermissionButton
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun ReadPermissionsPlaceHolder(
	onPermissionChanged: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
) {
	val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
		Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE

	Column(
		verticalArrangement = Arrangement.spacedBy(space = dimensionResource(id = R.dimen.space_8)),
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier.padding(horizontal = 20.dp),
	) {
		Image(
			painter = painterResource(id = R.drawable.ic_no_image),
			contentDescription = null,
			colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
			modifier = Modifier.sizeIn(
				maxWidth = 120.dp,
				maxHeight = 120.dp,
				minWidth = 80.dp,
				minHeight = 80.dp
			)
		)
		Text(
			text = stringResource(id = R.string.request_read_image_perms_text),
			style = MaterialTheme.typography.titleLarge,
			textAlign = TextAlign.Center,
		)
		CheckSinglePermissionButton(
			permission = permission,
			isPermissionAllowed = onPermissionChanged,
			modifier = Modifier
				.defaultMinSize(minWidth = dimensionResource(id = R.dimen.perms_button_min_width)),
		) {
			Icon(
				painter = painterResource(id = R.drawable.ic_gallery),
				contentDescription = null
			)
			Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.space_8)))
			Text(
				text = stringResource(id = R.string.allow_access_text),
				style = MaterialTheme.typography.titleMedium,
				textAlign = TextAlign.Center,
			)
		}

	}
}

@PreviewLightDark
@Composable
fun ReadPermissionsPlaceHolderPreview() = ScannerAppTheme {
	Surface {
		ReadPermissionsPlaceHolder(
			onPermissionChanged = {},
			modifier = Modifier.padding(vertical = 10.dp)
		)
	}
}