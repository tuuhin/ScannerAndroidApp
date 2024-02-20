package com.eva.scannerapp.domain.models

import com.google.mlkit.vision.barcode.common.Barcode

sealed class BarCodeTypes(val codeType: Int) {

	data class Email(val address: String?, val subject: String?, val body: String?) :
		BarCodeTypes(codeType = Barcode.TYPE_EMAIL)

	data class Phone(val number: String?) :
		BarCodeTypes(codeType = Barcode.TYPE_PHONE)

	data class GeoPoint(val lat: Double?, val long: Double?) :
		BarCodeTypes(codeType = Barcode.TYPE_GEO)

	data class UrlBookMark(val title: String?, val url: String?) :
		BarCodeTypes(codeType = Barcode.TYPE_URL)

	data class WiFi(val ssid: String?, val password: String?, val encryptionType: Int) :
		BarCodeTypes(codeType = Barcode.TYPE_WIFI)

	data class Text(val text: String) :
		BarCodeTypes(codeType = Barcode.TYPE_TEXT)

	data class Sms(val message: String?, val phoneNumber: String?) :
		BarCodeTypes(codeType = Barcode.TYPE_SMS)

	data object Unknown : BarCodeTypes(Barcode.TYPE_UNKNOWN)

	data class DriverLicense(
		val documentType: String?,
		val firstName: String?,
		val middleName: String?,
		val lastName: String?,
		val gender: String?,
		val addressStreet: String?,
		val addressCity: String?,
		val addressState: String?,
		val addressZip: String?,
		val licenseNumber: String?,
		val issueDate: String?,
		val expiryDate: String?,
		val birthDate: String?,
		val issuingCountry: String?
	) : BarCodeTypes(codeType = Barcode.TYPE_DRIVER_LICENSE)

	data class ContactInfo(
		val name: PersonName?,
		val organization: String?,
		val title: String?,
		val phones: List<Phone?>,
		val emails: List<Email?>,
		val urls: List<String?>,
		val addresses: List<String?>
	) : BarCodeTypes(codeType = Barcode.TYPE_CONTACT_INFO) {
		data class PersonName(
			val formattedName: String?,
			val pronunciation: String?,
			val prefix: String?,
			val first: String?,
			val middle: String?,
			val last: String?,
			val suffix: String?
		)
	}

}
