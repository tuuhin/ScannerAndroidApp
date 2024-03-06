package com.eva.scannerapp.data.ml.feed

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.eva.scannerapp.domain.ml.MLModelAnalyzer
import com.eva.scannerapp.domain.ml.MLResource
import com.eva.scannerapp.domain.ml.models.RecognizedModel
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private const val IMAGE_ANALYSIS_TAG = "IMAGE_ANALYSIS"

@OptIn(ExperimentalGetImage::class)
abstract class CameraFeedImageAnalyzer<T>(
	private val mlAnalyser: MLModelAnalyzer<T>
) : ImageAnalysis.Analyzer where T : RecognizedModel {

	private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

	private var _listener: ((MLResource<T>) -> Unit)? = null

	override fun analyze(image: ImageProxy) {
		scope.launch {
			try {
				val imageObject = image.image ?: run {
					image.close()
					Log.d(IMAGE_ANALYSIS_TAG, "NIL_SO_CLOSE")
					return@launch
				}

				val inputImage = InputImage
					.fromMediaImage(imageObject, image.imageInfo.rotationDegrees)

				val results = mlAnalyser.analyseImage(inputImage)
				_listener?.invoke(results)
				delay(THROTTLE_TIME)
			} catch (e: Exception) {
				e.printStackTrace()
				val errorReason = MLResource.Error<T>(error = e)
				_listener?.invoke(errorReason)
			}
		}.invokeOnCompletion { exp ->
			exp?.printStackTrace()
			image.close()
			Log.d(IMAGE_ANALYSIS_TAG, "CLOSED AND DISPOSED")
		}

	}

	fun setImageListener(listener: ((MLResource<T>) -> Unit)? = null) {
		_listener = listener
	}

	companion object {
		val THROTTLE_TIME = 500.milliseconds
	}
}
