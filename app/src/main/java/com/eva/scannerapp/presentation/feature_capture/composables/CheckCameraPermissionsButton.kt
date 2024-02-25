package com.eva.scannerapp.presentation.feature_capture.composables

import android.Manifest
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker

@Composable
fun CheckCameraPermissionsButton(
	modifier: Modifier = Modifier,
	context: Context = LocalContext.current,
	onPermissionChanged: (Boolean) -> Unit = {},
	shape: Shape = ButtonDefaults.shape,
	colors: ButtonColors = ButtonDefaults.buttonColors(),
	contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
	elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
	content: @Composable RowScope.() -> Unit,
) {

	var isPermissionGranted by remember {
		mutableStateOf(
			ContextCompat.checkSelfPermission(
				context, Manifest.permission.CAMERA
			) == PermissionChecker.PERMISSION_GRANTED
		)
	}

	LaunchedEffect(isPermissionGranted) {
		snapshotFlow { isPermissionGranted }.collect(onPermissionChanged)
	}

	val permissionLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestPermission(),
		onResult = { isGranted ->
			isPermissionGranted = isGranted
		},
	)

	Button(
		onClick = {
			val permission = Manifest.permission.CAMERA
			if (!isPermissionGranted) permissionLauncher.launch(permission)
		},
		shape = shape,
		colors = colors,
		elevation = elevation,
		content = content,
		contentPadding = contentPadding,
		modifier = modifier,
	)

}