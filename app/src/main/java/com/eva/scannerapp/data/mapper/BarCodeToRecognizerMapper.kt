package com.eva.scannerapp.data.mapper

import com.eva.scannerapp.domain.ml.models.RecognizedBarcode
import com.eva.scannerapp.domain.ml.util.BarCodeFormat
import com.google.mlkit.vision.barcode.common.Barcode

fun Barcode.toRecognizedModel(): RecognizedBarcode = RecognizedBarcode(
	type = domainType,
	displayText = displayValue,
	rawString = rawValue,
	codeFormat = BarCodeFormat.fromCode(format),
	rect = boundingBox
)
