package com.eva.scannerapp.presentation.feature_capture.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.core.content.ContextCompat

fun Context.takeImageWithCameraController(
	controller: CameraController,
	onImageCapture: (Bitmap?) -> Unit,
	onImageCaptureFailed: (ImageCaptureException) -> Unit,
) {

	val executor = ContextCompat.getMainExecutor(this)

	val onSaveCallBack = object : ImageCapture.OnImageCapturedCallback() {

		override fun onCaptureSuccess(image: ImageProxy) {
			super.onCaptureSuccess(image)
			val imageInfo = image.imageInfo
			// on success don't do anything close it
			val matrix = Matrix().apply {
				postRotate(imageInfo.rotationDegrees.toFloat())
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

	controller.takePicture(executor, onSaveCallBack)
}