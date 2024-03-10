package com.eva.scannerapp.domain.ml.models

import android.graphics.Rect
import com.eva.scannerapp.domain.ml.util.BarCodeFormat
import com.eva.scannerapp.domain.ml.util.BarCodeTypes

data class RecognizedBarcode(
	val type: BarCodeTypes,
	val displayText: String?,
	val rawString: String?,
	val codeFormat: BarCodeFormat,
	val rect: Rect?,
) : RecognizedModel(bounding = rect)