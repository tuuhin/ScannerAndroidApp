package com.eva.scannerapp.domain.ml.models

data class RecognizedLabel(
	val text: String,
	val confidence: Float
) : RecognizedModel(bounding = null)