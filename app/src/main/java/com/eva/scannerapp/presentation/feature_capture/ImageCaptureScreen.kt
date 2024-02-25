package com.eva.scannerapp.presentation.feature_capture

import android.Manifest
import android.annotation.SuppressLint
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.eva.scannerapp.presentation.feature_capture.composables.AnalysisOptionsPicker
import com.eva.scannerapp.presentation.feature_capture.composables.AndroidPreviewView
import com.eva.scannerapp.presentation.feature_capture.composables.CameraControls
import com.eva.scannerapp.presentation.feature_capture.composables.CameraPermissionPlaceHolder
import com.eva.scannerapp.presentation.feature_capture.composables.CaptureBox
import com.eva.scannerapp.presentation.feature_capture.composables.ImageCaptureScreenTopBar
import com.eva.scannerapp.presentation.feature_capture.states.AnalysisOption
import com.eva.scannerapp.presentation.feature_capture.states.ImagePreviewState

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageCaptureScreen(
	imageState: ImagePreviewState,
	modifier: Modifier = Modifier,
) {
	val context = LocalContext.current

	val cameraController = remember {
		val useCases = CameraController.IMAGE_CAPTURE or CameraController.IMAGE_ANALYSIS

		LifecycleCameraController(context).apply {
			setEnabledUseCases(useCases)
		}
	}

	var isPermissionProvided by remember {
		mutableStateOf(
			ContextCompat.checkSelfPermission(
				context,
				Manifest.permission.CAMERA
			) == PermissionChecker.PERMISSION_GRANTED
		)
	}

	var isFlashEnabled by remember { mutableStateOf(false) }


	Scaffold(
		topBar = {
			ImageCaptureScreenTopBar(
				isFlashEnabled = isFlashEnabled,
				onToggleFlash = { isFlashEnabled = !isFlashEnabled },
			)
		},
		modifier = modifier,
	) {
		Column(
			verticalArrangement = Arrangement.spacedBy(16.dp),
			modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
		) {
			Box(
				modifier = Modifier
					.clip(shape = RoundedCornerShape(0.dp, 0.dp, 30.dp, 30.dp))
					.weight(1f),
			) {
				if (isPermissionProvided) {
					//Camera Preview
					AndroidPreviewView(
						cameraController = cameraController,
						onUpdate = { controller ->
							controller.enableTorch(isFlashEnabled)
						},
						modifier = Modifier.fillMaxSize()
					)
					// Capture Box
					CaptureBox(
						modifier = Modifier
							.fillMaxWidth(.7f)
							.align(Alignment.Center),
					)
					// SHow permissionChecker Button
				} else Box(
					modifier = Modifier
						.fillMaxSize()
						.background(MaterialTheme.colorScheme.background),
					contentAlignment = Alignment.Center
				) {
					CameraPermissionPlaceHolder(
						onAllowAccess = {
							isPermissionProvided = it
						},
					)
				}

				// Controls
				CameraControls(
					isEnabled = isPermissionProvided,
					imageState = imageState,
					onImageCapture = {},
					modifier = Modifier
						.fillMaxWidth()
						.align(Alignment.BottomCenter)
				)
			}
			AnalysisOptionsPicker(
				selectedOption = AnalysisOption.BAR_CODE,
				onOptionSelect = {},
				modifier = Modifier.fillMaxWidth()
			)
		}
	}
}