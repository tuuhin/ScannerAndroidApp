package com.eva.scannerapp.presentation.feature_capture.composables

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.eva.scannerapp.R
import com.eva.scannerapp.presentation.composables.ManagePermissionMenu
import com.eva.scannerapp.presentation.util.preview.BooleanPreviewParams
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageCaptureScreenTopBar(
	isPermsEnabled: Boolean,
	isFlashEnabled: Boolean,
	modifier: Modifier = Modifier,
	onToggleFlash: () -> Unit = {},
	onSettings: () -> Unit = {},
) {
	var isMenuActive by remember { mutableStateOf(false) }

	val colors = if (isPermsEnabled) TopAppBarDefaults.centerAlignedTopAppBarColors(
		containerColor = Color.Transparent,
		titleContentColor = Color.White,
		navigationIconContentColor = Color.White,
		actionIconContentColor = Color.White
	) else TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)



	CenterAlignedTopAppBar(
		title = { Text(text = stringResource(id = R.string.app_name)) },
		navigationIcon = {
			if (isPermsEnabled)
				IconButton(
					onClick = onToggleFlash,
				) {
					if (isFlashEnabled) Icon(
						painter = painterResource(id = R.drawable.ic_flash_on),
						contentDescription = stringResource(id = R.string.camera_flash_on_icon_desc)
					)
					else Icon(
						painter = painterResource(id = R.drawable.ic_no_flash),
						contentDescription = stringResource(id = R.string.camera_flash_off_icon_desc)
					)
				}
		},
		actions = {
			IconButton(onClick = onSettings) {
				Icon(
					imageVector = Icons.Default.Settings,
					contentDescription = "Settings Icon"
				)
			}
			ManagePermissionMenu(
				isExpanded = isMenuActive,
				onClick = { isMenuActive = !isMenuActive },
				onDismissRequest = { isMenuActive = false },
			)
		},
		colors = colors,
		modifier = modifier
			.background(
				Brush.verticalGradient(
					colors = listOf(
						Color.Black,
						Color.Transparent,
						Color.Transparent
					)
				)
			)
	)
}

@Preview
@Composable
private fun ImageCaptureScreenTopBarIsFlashEnabledPreview(
	@PreviewParameter(BooleanPreviewParams::class)
	isFlashEnabled: Boolean,
) = ScannerAppTheme {
	ImageCaptureScreenTopBar(
		isPermsEnabled = true,
		isFlashEnabled = isFlashEnabled
	)
}