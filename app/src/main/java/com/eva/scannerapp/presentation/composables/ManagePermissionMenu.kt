package com.eva.scannerapp.presentation.composables

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.eva.scannerapp.R
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@Composable
fun ManagePermissionMenu(
	isExpanded: Boolean,
	onClick: () -> Unit,
	onDismissRequest: () -> Unit,
	modifier: Modifier = Modifier,
	colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
	properties: PopupProperties = remember { PopupProperties() }
) {

	val context = LocalContext.current
	var anchorOffset by remember { mutableStateOf(DpOffset.Zero) }

	DropdownMenu(
		expanded = isExpanded,
		onDismissRequest = onDismissRequest,
		offset = anchorOffset,
		properties = properties
	) {
		DropdownMenuItem(
			text = {
				Text(
					text = stringResource(id = R.string.manage_permissions_icon_desc),
					style = MaterialTheme.typography.labelLarge
				)
			},
			onClick = {
				val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
					.apply {
						data = Uri.fromParts("package", context.packageName, null)
					}
				context.startActivity(intent)
			},
			leadingIcon = {
				Icon(
					painter = painterResource(id = R.drawable.ic_permissions),
					contentDescription = stringResource(id = R.string.manage_permissions_icon_desc)
				)
			},
		)
	}

	IconButton(
		onClick = onClick,
		modifier = modifier.pointerInput(Unit) {
			detectTapGestures(
				onTap = {
					anchorOffset = DpOffset(it.x.dp, it.y.dp)
				},
			)
		},
		colors = colors
	) {
		Icon(
			imageVector = Icons.Default.MoreVert,
			contentDescription = "More Options"
		)
	}
}

@PreviewLightDark
@Composable
fun MenuDropDownOptionButtonPreview() = ScannerAppTheme {
	ManagePermissionMenu(
		isExpanded = true,
		onClick = { },
		onDismissRequest = { },
	)
}