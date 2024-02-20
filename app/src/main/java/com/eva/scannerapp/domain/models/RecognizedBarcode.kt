package com.eva.scannerapp.domain.models

data class RecognizedBarcode(
	val type: BarCodeTypes,
	val displayText: String?,
	val rawString: String?,
	val codeFormat: BarCodeFormat,
	val rect: BoundingRect?,
)