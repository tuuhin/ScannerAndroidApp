package com.eva.scannerapp.data.ml

import com.eva.scannerapp.domain.models.RecognizedLabel
import com.eva.scannerapp.util.Resource
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeler
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class ImageLabelAnalyser @Inject constructor(
	private val imageLabeler: ImageLabeler
) : ImageAnalyzer<List<RecognizedLabel>> {
	override suspend fun analyseImage(image: InputImage): Resource<List<RecognizedLabel>> {
		return suspendCancellableCoroutine { cont ->
			imageLabeler.process(image)
				.apply {
					addOnCompleteListener {
						addOnSuccessListener { labels ->
							val models = labels.map { label: ImageLabel ->
								RecognizedLabel(text = label.text, confidence = label.confidence)
							}
							cont.resume(value = Resource.Success(data = models))
						}
						addOnFailureListener { exp ->
							if (exp is MlKitException)
								return@addOnFailureListener cont.resume(Resource.Error(exp))
							cont.cancel(exp)
						}
					}
					addOnCanceledListener(cont::cancel)
				}
		}
	}
}