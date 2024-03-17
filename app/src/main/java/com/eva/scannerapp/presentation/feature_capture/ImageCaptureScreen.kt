package com.eva.scannerapp.presentation.feature_capture

import android.Manifest
import androidx.camera.view.CameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
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
import com.eva.scannerapp.presentation.feature_capture.composables.RecognizedResultsBottomSheet
import com.eva.scannerapp.presentation.feature_capture.states.AnalyzerSheetEvents
import com.eva.scannerapp.presentation.feature_capture.states.CameraScreenState
import com.eva.scannerapp.presentation.feature_capture.states.ImageCaptureEvents
import com.eva.scannerapp.presentation.feature_capture.states.ImagePreviewState
import com.eva.scannerapp.presentation.feature_capture.states.RecognizedItemState
import com.eva.scannerapp.presentation.util.LocalSnackBarStateProvider

@Composable
fun ImageCaptureScreen(
	cameraScreenState: CameraScreenState,
	recognizedState: RecognizedItemState,
	previewImageState: ImagePreviewState,
	onImageEvents: (ImageCaptureEvents) -> Unit,
	onSheetEvents: (AnalyzerSheetEvents) -> Unit,
	navigation: () -> Unit,
	modifier: Modifier = Modifier,
	controllerConstraints: (CameraController) -> Unit,
	onSettingsNavigation: () -> Unit = {},
) {
	val context = LocalContext.current
	val snackBarHostState = LocalSnackBarStateProvider.current

	var isPermissionProvided by remember {
		mutableStateOf(
			ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
					== PermissionChecker.PERMISSION_GRANTED
		)
	}

	if (recognizedState.showResults)
		RecognizedResultsBottomSheet(
			option = cameraScreenState.analysisOption,
			labelModels = recognizedState.labelModels,
			barCodeModel = recognizedState.savedBarCodeModel,
			onDismissRequest = { onSheetEvents(AnalyzerSheetEvents.CloseBottomSheet) }
		)


	Scaffold(
		topBar = {
			ImageCaptureScreenTopBar(
				isPermsEnabled = isPermissionProvided,
				isFlashEnabled = cameraScreenState.isFlashOn,
				onToggleFlash = { onImageEvents(ImageCaptureEvents.ToggleFlashMode) },
				onSettings = onSettingsNavigation
			)
		},
		snackbarHost = { SnackbarHost(snackBarHostState) },
		contentWindowInsets = WindowInsets.navigationBars,
		modifier = modifier,
	) { scPadding ->
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(bottom = scPadding.calculateBottomPadding()),
			verticalArrangement = Arrangement.spacedBy(4.dp)
		) {
			CameraWithController(
				isCameraAllowed = isPermissionProvided,
				analysisOption = cameraScreenState.analysisOption,
				previousImage = previewImageState,
				recognizedItem = recognizedState,
				controllerConstraints = controllerConstraints,
				onPreviewClick = navigation,
				onAllowCameraPermission = { isPermissionProvided = it },
				controllerCallback = { controller ->
					controller.enableTorch(cameraScreenState.isFlashOn)
				},
				onCaptureSuccess = { bmp ->
					onImageEvents(ImageCaptureEvents.OnImageCaptureSuccess(bmp))
				},
				onCaptureFailed = { exception ->
					onImageEvents(ImageCaptureEvents.OnImageCaptureFailed(exception))
				},
				onShowLabelAnalysis = {
					onSheetEvents(AnalyzerSheetEvents.OpenBottomSheet)
				},
				onShowBarCodeAnalysis = { model ->
					onSheetEvents(AnalyzerSheetEvents.OnSelectBoundingRect(model))
				},
				modifier = Modifier
					.clip(shape = RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp))
					.background(MaterialTheme.colorScheme.surfaceVariant)
					.weight(1f)
			)
			AnalysisOptionsPicker(
				selectedOption = cameraScreenState.analysisOption,
				onOptionSelect = { option ->
					onImageEvents(ImageCaptureEvents.OnAnalysisModeChange(option))
				},
				modifier = Modifier.fillMaxWidth()
			)
		}
	}
}