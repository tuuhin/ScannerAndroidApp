package com.eva.scannerapp.presentation.feature_gallery.composables

import android.Manifest
import android.os.Build
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
import com.eva.scannerapp.presentation.feature_gallery.state.GalleryPermissionState

@Composable
fun ReadGalleryPermission(
	onGalleryPermission: (GalleryPermissionState) -> Unit,
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

	var isMediaUserSelectedGranted by remember {
		mutableStateOf(
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
				ContextCompat.checkSelfPermission(
					context,
					Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
				) == PermissionChecker.PERMISSION_GRANTED
			else false
		)
	}

	var isFullReadImagesGranted by remember {
		mutableStateOf(
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
				ContextCompat.checkSelfPermission(
					context,
					Manifest.permission.READ_MEDIA_IMAGES
				) == PermissionChecker.PERMISSION_GRANTED
			else ContextCompat.checkSelfPermission(
				context,
				Manifest.permission.READ_EXTERNAL_STORAGE
			) == PermissionChecker.PERMISSION_GRANTED
		)
	}

	val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
		arrayOf(
			Manifest.permission.READ_MEDIA_IMAGES,
			Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
		)
	else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
		arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
	else arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)


	val permissionLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestMultiplePermissions(),
		onResult = { results ->
			// check if you can read images
			isFullReadImagesGranted =
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
					results.getOrDefault(Manifest.permission.READ_MEDIA_IMAGES, false)
				else
					results.getOrDefault(Manifest.permission.READ_EXTERNAL_STORAGE, false)

			if (isFullReadImagesGranted) {
				onGalleryPermission(GalleryPermissionState.GRANTED)
				return@rememberLauncherForActivityResult
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
				// If build is greater than api 34,
				// then check if read media visual is granted and full read is not granted
				isMediaUserSelectedGranted =
					results.getOrDefault(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED, false)

				if (isMediaUserSelectedGranted)
					onGalleryPermission(GalleryPermissionState.PARTIALLY_GRANTED)
			}
		},
	)

	Button(
		onClick = {
			if (!isFullReadImagesGranted || !isMediaUserSelectedGranted)
				permissionLauncher.launch(permissions)
		},
		modifier = modifier,
		shape = shape,
		colors = colors,
		elevation = elevation,
		contentPadding = contentPadding,
		content = content
	)
}