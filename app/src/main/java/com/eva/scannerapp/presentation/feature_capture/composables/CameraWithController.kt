package com.eva.scannerapp.presentation.feature_capture.composables

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.CameraController
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import com.eva.scannerapp.R
import com.eva.scannerapp.domain.ml.models.RecognizedModel
import com.eva.scannerapp.presentation.composables.options.AnalysisOption
import com.eva.scannerapp.presentation.feature_capture.states.ImagePreviewState
import com.eva.scannerapp.presentation.feature_capture.states.RecognizedItemState
import com.eva.scannerapp.presentation.feature_capture.util.AppCameraController
import com.eva.scannerapp.presentation.feature_capture.util.takeImageWithCameraController


@Composable
fun CameraWithController(
	isCameraAllowed: Boolean,
	analysisOption: AnalysisOption,
	previousImage: ImagePreviewState,
	recognizedItem: RecognizedItemState,
	onAllowCameraPermission: (Boolean) -> Unit,
	controllerCallback: (CameraController) -> Unit,
	onCaptureSuccess: (Bitmap?) -> Unit,
	modifier: Modifier = Modifier,
	onPreviewClick: () -> Unit = {},
	onShowLabelAnalysis: () -> Unit = {},
	onShowBarCodeAnalysis: (RecognizedModel) -> Unit = {},
	onCaptureFailed: (ImageCaptureException) -> Unit = {},
	controllerConstraints: CameraController.() -> Unit = {},
) {
	val context = LocalContext.current

	val cameraController = remember { AppCameraController.getController(context) }

	DisposableEffect(cameraController) {
		onDispose {
			// ON Dispose clear the image analysisAnalyzer
			Log.i("CAMERA_ANALYZER", "CAMERA ANALYSER CLEARED")
			cameraController.clearImageAnalysisAnalyzer()
		}
	}

	Box(
		modifier = modifier,
		contentAlignment = Alignment.Center
	) {
		Crossfade(
			targetState = isCameraAllowed,
			label = "Is Camera Permission provided",
		) { isAllowed ->
			if (isAllowed) {
				// Camera preview view
				AndroidPreviewView(
					cameraController = cameraController,
					onUpdate = controllerCallback,
					viewConstraints = {
						controller?.apply(controllerConstraints)
					},
					modifier = Modifier.fillMaxSize()
				)
				// show analysis box
				AnimatedVisibility(
					visible = recognizedItem.showCaptureBox,
					enter = fadeIn(),
					exit = fadeOut()
				) {
					Crossfade(
						targetState = analysisOption,
						label = "Analysis overlay",
						animationSpec = tween(durationMillis = 600)
					) { option ->
						when (option) {
							AnalysisOption.BAR_CODE -> BarCodeShowResultsRectOverlay(
								item = recognizedItem,
								onTap = onShowBarCodeAnalysis,
								modifier = Modifier.fillMaxSize()
							)

							AnalysisOption.LABELS -> LabelsShowResultOverlay(
								onClick = onShowLabelAnalysis,
								modifier = Modifier.fillMaxSize()
							)
						}
					}
				}
				// Capture Box
				CaptureBox(
					modifier = Modifier
						.width(dimensionResource(id = R.dimen.capture_box_size))
						.align(Alignment.Center),
				)
			} else {
				// if camera isn't allowed
				CameraPermissionPlaceHolder(
					onAllowAccess = onAllowCameraPermission,
					modifier = Modifier.align(Alignment.Center)
				)
			}

		}
		// Controls
		CameraControls(
			isEnabled = isCameraAllowed,
			imageState = previousImage,
			onCapture = {
				context.takeImageWithCameraController(
					controller = cameraController,
					onImageCapture = onCaptureSuccess,
					onImageCaptureFailed = onCaptureFailed
				)
			},
			onPreviewClick = onPreviewClick,
			modifier = Modifier
				.fillMaxWidth()
				.align(Alignment.BottomCenter)
		)
	}
}