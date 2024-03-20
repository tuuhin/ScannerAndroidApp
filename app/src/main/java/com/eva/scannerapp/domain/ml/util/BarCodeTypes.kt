package com.eva.scannerapp.domain.ml.util

import com.google.mlkit.vision.barcode.common.Barcode

sealed class BarCodeTypes(val codeType: Int) {

	data class Email(
		val address: String? = null,
		val subject: String? = null,
		val body: String? = null
	) : BarCodeTypes(codeType = Barcode.TYPE_EMAIL)

	data class Phone(val number: String? = null) :
		BarCodeTypes(codeType = Barcode.TYPE_PHONE)

	data class GeoPoint(val lat: Double? = null, val long: Double? = null) :
		BarCodeTypes(codeType = Barcode.TYPE_GEO)

	data class UrlBookMark(val title: String? = null, val url: String? = null) :
		BarCodeTypes(codeType = Barcode.TYPE_URL)

	data class WiFi(
		val ssid: String? = null,
		val password: String? = null,
		val encryptionType: WifiEncryptionType? = null
	) : BarCodeTypes(codeType = Barcode.TYPE_WIFI) {
		enum class WifiEncryptionType {
			OPEN, WPA, WEP, UNKNOWN
		}
	}

	data class Text(val text: String) :
		BarCodeTypes(codeType = Barcode.TYPE_TEXT)

	data class Sms(val message: String? = null, val phoneNumber: String? = null) :
		BarCodeTypes(codeType = Barcode.TYPE_SMS)

	data object Unknown : BarCodeTypes(Barcode.TYPE_UNKNOWN)

	data class DriverLicense(
		val documentType: String? = null,
		val firstName: String? = null,
		val middleName: String? = null,
		val lastName: String? = null,
		val gender: String? = null,
		val addressStreet: String? = null,
		val addressCity: String? = null,
		val addressState: String? = null,
		val addressZip: String? = null,
		val licenseNumber: String? = null,
		val issueDate: String? = null,
		val expiryDate: String? = null,
		val birthDate: String? = null,
		val issuingCountry: String?
	) : BarCodeTypes(codeType = Barcode.TYPE_DRIVER_LICENSE)

	data class ContactInfo(
		val name: PersonName? = null,
		val organization: String? = null,
		val title: String? = null,
		val phones: List<Phone?>,
		val emails: List<Email?>,
		val urls: List<String?>,
		val addresses: List<String?>,
	) : BarCodeTypes(codeType = Barcode.TYPE_CONTACT_INFO) {
		data class PersonName(
			val formattedName: String? = null,
			val pronunciation: String? = null,
			val prefix: String? = null,
			val first: String? = null,
			val middle: String? = null,
			val last: String? = null,
			val suffix: String? = null
		) {

			val displayName: String
				get() = buildString {
					if (pronunciation != null) {
						append(pronunciation)
						append(",")
					}
					if (prefix != null) {
						append(prefix)
						append(" ")
					}
					if (formattedName != null)
						append(formattedName)
					else {
						if (first != null) {
							append(first)
							append(" ")
						}
						if (middle != null) {
							append(middle)
							append(" ")
						}
						if (last != null) append(last)
					}
					append(" ")
					if (suffix != null) {
						append(suffix)
						append(" ")
					}
				}
		}
	}
}

