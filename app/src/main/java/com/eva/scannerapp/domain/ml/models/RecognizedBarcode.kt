package com.eva.scannerapp.domain.ml.models

import com.eva.scannerapp.domain.ml.util.BarCodeFormat
import com.eva.scannerapp.domain.ml.util.BarCodeTypes
import com.eva.scannerapp.domain.ml.util.BoundingRect

data class RecognizedBarcode(
	val type: BarCodeTypes,
	val displayText: String?,
	val rawString: String?,
	val codeFormat: BarCodeFormat,
	val rect: BoundingRect?,
) : RecognizedModel