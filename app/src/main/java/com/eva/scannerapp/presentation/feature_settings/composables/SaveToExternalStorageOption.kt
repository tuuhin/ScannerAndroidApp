package com.eva.scannerapp.presentation.feature_settings.composables

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PermMedia
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.eva.scannerapp.R
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun SaveToExternalStorageOption(
	isExternalStorageAllowed: Boolean,
	onToggleOption: (Boolean) -> Unit,
	modifier: Modifier = Modifier
) {
	val context = LocalContext.current

	var isPermissionEnabled by remember {
		mutableStateOf(
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)
				ContextCompat.checkSelfPermission(
					context,
					Manifest.permission.WRITE_EXTERNAL_STORAGE
				) == PermissionChecker.PERMISSION_GRANTED
			else true
		)
	}

	val permissionLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestPermission()
	) { isGranted ->
		isPermissionEnabled = isGranted
	}

	ListItem(
		headlineContent = {
			Text(
				text = stringResource(id = R.string.settings_media_visibility_title),
				style = MaterialTheme.typography.titleMedium
			)
		},
		supportingContent = {
			Text(
				text =  stringResource(id = R.string.settings_media_visibility_desc),
				style = MaterialTheme.typography.bodySmall
			)
		},
		leadingContent = {
			Icon(
				imageVector = Icons.Outlined.PermMedia,
				contentDescription = "Gallery Image",
				modifier = Modifier.size(32.dp)
			)
		},
		trailingContent = {
			Switch(
				checked = isExternalStorageAllowed,
				onCheckedChange = {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q || isPermissionEnabled)
						onToggleOption(it)
					else permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
				}
			)
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
private fun SaveToExternalStorageOptionPreview() = ScannerAppTheme {
	SaveToExternalStorageOption(isExternalStorageAllowed = true, onToggleOption = {})
}