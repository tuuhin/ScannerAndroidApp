package com.eva.scannerapp.domain.ml.util

import com.google.mlkit.vision.barcode.common.Barcode

enum class BarCodeFormat(val code: Int) {
	UNKNOWN(Barcode.FORMAT_UNKNOWN),
	ALL_FORMATS(Barcode.FORMAT_ALL_FORMATS),
	CODE_128(Barcode.FORMAT_CODE_128),
	CODE_39(Barcode.FORMAT_CODE_39),
	CODE_93(Barcode.FORMAT_CODE_93),
	CODABAR(Barcode.FORMAT_CODABAR),
	DATA_MATRIX(Barcode.FORMAT_DATA_MATRIX),
	EAN_13(Barcode.FORMAT_EAN_13),
	EAN_8(Barcode.FORMAT_EAN_8),
	ITF(Barcode.FORMAT_ITF),
	QR_CODE(Barcode.FORMAT_QR_CODE),
	UPC_A(Barcode.FORMAT_UPC_A),
	UPC_E(Barcode.FORMAT_UPC_E),
	PDF417(Barcode.FORMAT_PDF417),
	AZTEC(Barcode.FORMAT_AZTEC);

	companion object {
		fun fromCode(code: Int): BarCodeFormat {
			return when (code) {
				Barcode.FORMAT_ALL_FORMATS -> ALL_FORMATS
				Barcode.FORMAT_CODE_128 -> CODE_128
				Barcode.FORMAT_CODE_39 -> CODE_39
				Barcode.FORMAT_CODE_93 -> CODE_93
				Barcode.FORMAT_CODABAR -> CODABAR
				Barcode.FORMAT_DATA_MATRIX -> DATA_MATRIX
				Barcode.FORMAT_EAN_13 -> EAN_13
				Barcode.FORMAT_EAN_8 -> EAN_8
				Barcode.FORMAT_ITF -> ITF
				Barcode.FORMAT_QR_CODE -> QR_CODE
				Barcode.FORMAT_UPC_A -> UPC_A
				Barcode.FORMAT_UPC_E -> UPC_E
				Barcode.FORMAT_PDF417 -> PDF417
				Barcode.FORMAT_AZTEC -> AZTEC
				else -> UNKNOWN
			}
		}
	}

}