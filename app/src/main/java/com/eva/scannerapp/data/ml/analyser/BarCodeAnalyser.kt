package com.eva.scannerapp.data.ml.analyser

import android.util.Log
import com.eva.scannerapp.data.mapper.toRecognizedModel
import com.eva.scannerapp.domain.ml.MLModelAnalyzer
import com.eva.scannerapp.domain.ml.MLResource
import com.eva.scannerapp.domain.ml.models.RecognizedBarcode
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class BarCodeAnalyser @Inject constructor(
	private val scanner: BarcodeScanner
) : MLModelAnalyzer<RecognizedBarcode> {
	override suspend fun analyseImage(image: InputImage): MLResource<RecognizedBarcode> {
		return suspendCancellableCoroutine { cont ->
			scanner.process(image).apply {
				addOnCompleteListener {

					addOnSuccessListener { barcodes ->
						val codes = barcodes.map(Barcode::toRecognizedModel)
							//removed the empty data
							.filter { it.rawString?.isNotEmpty() == true }
							.toList()
						if (codes.isNotEmpty()) cont.resume(MLResource.Success(data = codes))
						else cont.resume(MLResource.Empty())
					}

					addOnFailureListener { exp ->
						if (exp is MlKitException) {
							Log.i("ANALYZER_TAG", "ML_KIT_EXCEPTION")
							cont.cancel()
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