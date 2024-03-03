package com.eva.scannerapp.presentation.composables

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker

@Composable
fun CheckSinglePermissionButton(
	permission: String,
	isPermissionAllowed: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
	shape: Shape = MaterialTheme.shapes.extraLarge,
	colors: ButtonColors = ButtonDefaults.buttonColors(
		containerColor = MaterialTheme.colorScheme.secondary,
		contentColor = MaterialTheme.colorScheme.onSecondary,
	),
	contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
	elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
	content: @Composable RowScope.() -> Unit,
) {
	val context = LocalContext.current

	var isPermissionGranted by remember {
		mutableStateOf(
			ContextCompat.checkSelfPermission(context, permission)
					== PermissionChecker.PERMISSION_GRANTED
		)
	}

	val permissionLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestPermission(),
		onResult = { isGranted ->
			isPermissionGranted = isGranted
			//callback to inform the end user permission may have changed
			isPermissionAllowed(isGranted)
		},
	)

	Button(
		onClick = {
			if (!isPermissionGranted)
				permissionLauncher.launch(permission)
		},
		modifier = modifier,
		shape = shape,
		colors = colors,
		elevation = elevation,
		contentPadding = contentPadding,
		content = content
	)
}