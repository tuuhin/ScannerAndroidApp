package com.eva.scannerapp.presentation.feature_capture.composables

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.eva.scannerapp.presentation.feature_capture.states.ImagePreviewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CameraWithController(
	isCameraAllowed: Boolean,
	previousImage: ImagePreviewState,
	onCameraPermsChanged: (Boolean) -> Unit,
	controllerCallback: (CameraController) -> Unit,
	onCaptureSuccess: (Bitmap?) -> Unit,
	onCaptureFailed: (ImageCaptureException) -> Unit,
	modifier: Modifier = Modifier,
) {
	val context = LocalContext.current

	val scope = rememberCoroutineScope()

	val cameraController = remember {
		val useCases = CameraController.IMAGE_CAPTURE or CameraController.IMAGE_ANALYSIS

		LifecycleCameraController(context).apply {
			setEnabledUseCases(useCases)
			imageCaptureMode = ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
		}
	}

	Box(
		modifier = modifier,
	) {
		if (isCameraAllowed) {
			//Camera Preview
			AndroidPreviewView(
				cameraController = cameraController,
				onUpdate = controllerCallback,
				modifier = Modifier.fillMaxSize()
			)
			// Capture Box
			CaptureBox(
				modifier = Modifier
					.fillMaxWidth(.7f)
					.align(Alignment.Center),
			)
		} else {
			// Show permissionChecker Button
			CameraPermissionPlaceHolder(
				onAllowAccess = onCameraPermsChanged,
				modifier = Modifier.align(Alignment.Center)
			)
		}
		// Controls
		CameraControls(
			isEnabled = isCameraAllowed,
			imageState = previousImage,
			onImageCapture = {
				scope.launch {
					cameraController.takeImage(
						context = context,
						onImageCapture = onCaptureSuccess,
						onImageCaptureFailed = onCaptureFailed
					)
				}
			},
			modifier = Modifier
				.fillMaxWidth()
				.align(Alignment.BottomCenter)
		)
	}
}


private suspend fun CameraController.takeImage(
	context: Context,
	onImageCapture: (Bitmap?) -> Unit,
	onImageCaptureFailed: (ImageCaptureException) -> Unit,
) {

	val onSaveCallBack = object : ImageCapture.OnImageCapturedCallback() {

		override fun onCaptureSuccess(image: ImageProxy) {
			super.onCaptureSuccess(image)
			// on success don't do anything close it
			val matrix = Matrix().apply {
				postRotate(image.imageInfo.rotationDegrees.toFloat())
			}

			val imageBitmap = image.toBitmap()


			val bitmap = Bitmap.createBitmap(
				imageBitmap, 0, 0, image.width, image.height, matrix, false
			)

			onImageCapture(bitmap)
			// need to close the image proxy
			image.close()
		}

		override fun onError(exception: ImageCaptureException) {
			onImageCaptureFailed(exception)
			super.onError(exception)
		}
	}

	withContext(Dispatchers.Default) {
		val mainExecutor = ContextCompat.getMainExecutor(context)
		takePicture(mainExecutor, onSaveCallBack)
	}
}