package com.eva.scannerapp.data.mapper

import android.graphics.Rect
import com.eva.scannerapp.domain.models.BarCodeFormat
import com.eva.scannerapp.domain.models.RecognizedBarcode
import com.google.mlkit.vision.barcode.common.Barcode

fun Barcode.toRecognizedModel(): RecognizedBarcode = RecognizedBarcode(
	type = domainType,
	displayText = displayValue,
	rawString = rawValue,
	codeFormat = BarCodeFormat.fromCode(format),
	rect = boundingBox?.let(Rect::toBoundingBox)
)
