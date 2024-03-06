package com.eva.scannerapp.domain.ml

import com.eva.scannerapp.domain.ml.models.RecognizedModel
import com.google.mlkit.vision.common.InputImage

interface MLModelAnalyzer<T : RecognizedModel> {

	suspend fun analyseImage(image: InputImage): MLResource<T>
}