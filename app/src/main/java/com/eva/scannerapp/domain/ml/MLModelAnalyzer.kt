package com.eva.scannerapp.domain.ml

import com.eva.scannerapp.util.Resource
import com.google.mlkit.vision.common.InputImage

interface MLModelAnalyzer<T> {

	suspend fun analyseImage(image: InputImage): Resource<T>
}