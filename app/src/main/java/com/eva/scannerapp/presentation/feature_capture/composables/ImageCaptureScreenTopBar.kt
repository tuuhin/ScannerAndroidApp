package com.eva.scannerapp.presentation.feature_capture.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.eva.scannerapp.R
import com.eva.scannerapp.presentation.util.preview.BooleanPreviewParams
import com.eva.scannerapp.presentation.util.preview.PreviewApi33
import com.eva.scannerapp.ui.theme.ScannerAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageCaptureScreenTopBar(
	isFlashEnabled: Boolean,
	modifier: Modifier = Modifier,
	onToggleFlash: () -> Unit = {},
	colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(
		containerColor = Color.Transparent,
		titleContentColor = Color.White,
		navigationIconContentColor = Color.White,
		actionIconContentColor = Color.White
	),
) {

	var showMenu by remember { mutableStateOf(false) }

	CenterAlignedTopAppBar(
		title = { Text(text = stringResource(id = R.string.app_name)) },
		navigationIcon = {
			IconButton(
				onClick = onToggleFlash,
			) {
				if (isFlashEnabled) Icon(
					imageVector = Icons.Default.FlashOn,
					contentDescription = null
				)
				else Icon(
					imageVector = Icons.Default.FlashOff,
					contentDescription = null
				)
			}
		},
		actions = {
			MenuDropDownOptionButton(
				isExpanded = showMenu,
				onClick = { showMenu = !showMenu },
				onDismissRequest = { showMenu = false },
			)
		},
		colors = colors,
		modifier = modifier
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewApi33
@Composable
fun ImageCaptureScreenTopBarPreview(
	@PreviewParameter(BooleanPreviewParams::class)
	isFlashEnabled: Boolean
) = ScannerAppTheme {
	ImageCaptureScreenTopBar(isFlashEnabled = isFlashEnabled)
}