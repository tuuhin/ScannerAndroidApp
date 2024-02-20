package com.eva.scannerapp.data.ml

import com.eva.scannerapp.util.Resource
import com.google.mlkit.vision.common.InputImage

interface ImageAnalyzer<T> {

	suspend fun analyseImage(image: InputImage): Resource<T>
}