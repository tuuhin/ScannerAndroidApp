package com.eva.scannerapp.data.ml

import com.eva.scannerapp.data.mapper.toRecognizedModel
import com.eva.scannerapp.domain.models.RecognizedBarcode
import com.eva.scannerapp.util.Resource
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class BarCodeAnalyser @Inject constructor(
	private val scanner: BarcodeScanner
) : ImageAnalyzer<List<RecognizedBarcode>> {
	override suspend fun analyseImage(image: InputImage): Resource<List<RecognizedBarcode>> {
		return suspendCancellableCoroutine { cont ->
			scanner.process(image).apply {
				addOnCompleteListener {
					addOnSuccessListener { barcodes ->
						val codes = barcodes.map(Barcode::toRecognizedModel)
						cont.resume(Resource.Success(data = codes))
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