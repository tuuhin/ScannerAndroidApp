package com.eva.scannerapp.data.ml.analyser

import android.util.Log
import com.eva.scannerapp.domain.ml.MLModelAnalyzer
import com.eva.scannerapp.domain.ml.MLResource
import com.eva.scannerapp.domain.ml.models.RecognizedLabel
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class ImageLabelAnalyser @Inject constructor(
	private val imageLabeler: ImageLabeler
) : MLModelAnalyzer<RecognizedLabel> {
	override suspend fun analyseImage(image: InputImage): MLResource<RecognizedLabel> {
		return suspendCancellableCoroutine { cont ->
			imageLabeler.process(image).apply {

				addOnCompleteListener {
					addOnSuccessListener { labels ->
						val models = labels.map { label: ImageLabel ->
							RecognizedLabel(text = label.text, confidence = label.confidence)
						}
						if (models.isNotEmpty()) cont.resume(MLResource.Success(data = models))
						cont.resume(value = MLResource.Empty())
					}
					addOnFailureListener { exp ->
						if (exp is MlKitException) {
							cont.cancel()
							Log.i("ANALYZER_TAG", "ML_KIT_EXCEPTION")
							return@addOnFailureListener
						}
						cont.resume(MLResource.Error(exp))
					}
				}

				addOnCanceledListener(cont::cancel)
			}
		}
	}
}