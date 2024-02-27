package com.eva.scannerapp.presentation.feature_capture

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.eva.scannerapp.presentation.composables.AnalysisOptionsPicker
import com.eva.scannerapp.presentation.feature_capture.composables.CameraWithController
import com.eva.scannerapp.presentation.feature_capture.composables.ImageCaptureScreenTopBar
import com.eva.scannerapp.presentation.feature_capture.states.ImagePreviewState
import com.eva.scannerapp.presentation.feature_capture.states.ImageScannerEvents
import com.eva.scannerapp.presentation.feature_capture.states.ImageScannerState
import com.eva.scannerapp.presentation.util.LocalSnackBarStateProvider

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ImageCaptureScreen(
	screenState: ImageScannerState,
	previewState: ImagePreviewState,
	modifier: Modifier = Modifier,
	onEvent: (ImageScannerEvents) -> Unit,
	snackBarHostState: SnackbarHostState = LocalSnackBarStateProvider.current
) {
	val context = LocalContext.current

	var isPermissionProvided by remember {
		mutableStateOf(
			ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
					== PermissionChecker.PERMISSION_GRANTED
		)
	}

	Scaffold(
		topBar = {
			ImageCaptureScreenTopBar(
				isPermsEnabled = isPermissionProvided,
				isFlashEnabled = screenState.isFlashOn,
				onToggleFlash = { onEvent(ImageScannerEvents.ToggleFlashMode) },
			)
		},
		snackbarHost = {
			SnackbarHost(snackBarHostState)
		},
		modifier = modifier,
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.windowInsetsPadding(WindowInsets.navigationBars)
		) {
			CameraWithController(
				previousImage = previewState,
				isCameraAllowed = isPermissionProvided,
				onCameraPermsChanged = { isPermissionProvided = it },
				controllerCallback = { controller ->
					controller.enableTorch(screenState.isFlashOn)
				},
				onCaptureSuccess = { bitmap ->
					bitmap?.let {
						onEvent(ImageScannerEvents.OnImageCaptureSuccess(bitmap))
					}
				},
				onCaptureFailed = { exception ->
					onEvent(ImageScannerEvents.OnImageCaptureFailed(exception))
				},
				modifier = Modifier
					.clip(shape = RoundedCornerShape(0.dp, 0.dp, 30.dp, 30.dp))
					.background(MaterialTheme.colorScheme.surfaceVariant)
					.weight(1f)
			)
			AnalysisOptionsPicker(
				selectedOption = screenState.analysisOption,
				onOptionSelect = { option -> onEvent(ImageScannerEvents.OnAnalysisModeChange(option)) },
				modifier = Modifier
					.padding(vertical = 6.dp)
					.fillMaxWidth()
			)
		}
	}
}