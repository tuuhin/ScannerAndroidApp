package com.eva.scannerapp.data.mapper

import com.eva.scannerapp.domain.ml.util.BarCodeTypes
import com.google.mlkit.vision.barcode.common.Barcode

val Barcode.domainType: BarCodeTypes
	get() = when (valueType) {
		Barcode.TYPE_CONTACT_INFO -> {
			val nameInfo = contactInfo?.name?.run {
				BarCodeTypes.ContactInfo.PersonName(
					formattedName,
					pronunciation = pronunciation,
					prefix = prefix,
					first = first,
					middle = middle,
					last = last,
					suffix = suffix
				)
			}
			val phones = contactInfo?.phones?.map { ph ->
				BarCodeTypes.Phone(number = ph.number)
			} ?: emptyList()

			val emails = contactInfo?.emails?.map { mail ->
				BarCodeTypes.Email(
					address = mail.address,
					subject = mail.subject,
					body = mail.body
				)
			} ?: emptyList()

			val address = contactInfo?.addresses?.map { address ->
				address.addressLines.toList()
			}?.flatten() ?: emptyList()

			BarCodeTypes.ContactInfo(
				name = nameInfo,
				organization = contactInfo?.organization,
				title = contactInfo?.title,
				phones = phones, emails = emails,
				addresses = address,
				urls = contactInfo?.urls ?: emptyList()
			)
		}

		Barcode.TYPE_EMAIL -> BarCodeTypes.Email(
			address = email?.address,
			subject = email?.subject,
			body = email?.body
		)

		Barcode.TYPE_PHONE -> BarCodeTypes.Phone(number = phone?.number)
		Barcode.TYPE_SMS -> BarCodeTypes.Sms(
			message = sms?.message,
			phoneNumber = sms?.phoneNumber
		)

		Barcode.TYPE_TEXT -> BarCodeTypes.Text(text = displayValue ?: "")
		Barcode.TYPE_URL -> BarCodeTypes.UrlBookMark(title = url?.title, url = url?.url)
		Barcode.TYPE_WIFI -> BarCodeTypes.WiFi(
			ssid = wifi?.ssid,
			password = wifi?.password,
			encryptionType = wifi?.encryptionType ?: -1
		)

		Barcode.TYPE_GEO -> BarCodeTypes.GeoPoint(lat = geoPoint?.lat, long = geoPoint?.lng)
		Barcode.TYPE_DRIVER_LICENSE -> BarCodeTypes.DriverLicense(
			documentType = driverLicense?.documentType,
			firstName = driverLicense?.firstName,
			middleName = driverLicense?.middleName,
			lastName = driverLicense?.lastName,
			gender = driverLicense?.gender,
			addressStreet = driverLicense?.addressStreet,
			addressCity = driverLicense?.addressCity,
			addressState = driverLicense?.addressState,
			addressZip = driverLicense?.addressZip,
			licenseNumber = driverLicense?.licenseNumber,
			issueDate = driverLicense?.issueDate,
			expiryDate = driverLicense?.expiryDate,
			birthDate = driverLicense?.birthDate,
			issuingCountry = driverLicense?.issuingCountry
		)

		else -> BarCodeTypes.Unknown
	}
