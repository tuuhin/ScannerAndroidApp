package com.eva.scannerapp.presentation.util.preview

import androidx.paging.PagingData
import com.eva.scannerapp.domain.image.models.ImageBucketModel
import com.eva.scannerapp.domain.image.models.ImageDataModel
import com.eva.scannerapp.domain.ml.models.RecognizedBarcode
import com.eva.scannerapp.domain.ml.models.RecognizedLabel
import com.eva.scannerapp.domain.ml.util.BarCodeFormat
import com.eva.scannerapp.domain.ml.util.BarCodeTypes
import kotlinx.coroutines.flow.MutableStateFlow

object PreviewFakes {

	private val FAKE_IMAGE_BUCKET_MODEL =
		ImageBucketModel(bucketId = 1, bucketName = "Preview Bucket")

	val FAKE_IMAGE_DATA_MODEL = ImageDataModel(
		id = 0,
		fileSize = "2kb",
		title = "THIS IS WONDERFUL",
		bucketModel = FAKE_IMAGE_BUCKET_MODEL,
		imageUri = ""
	)

	private val FAKE_RECOGNIZED_LABEL_RESULT_MODEL =
		RecognizedLabel(text = "Something", confidence = .5f)

	val FAKE_RECOGNIZED_LABEL_RESULTS_LIST = List(10) {
		FAKE_RECOGNIZED_LABEL_RESULT_MODEL.copy(confidence = it.toFloat())
	}

	val FAKE_QR_CODE_WIFI = RecognizedBarcode(
		type = BarCodeTypes.WiFi(
			ssid = "Network Name",
			password = "Something",
			encryptionType = 0
		),
		displayText = "Network Name Something",
		rawString = "WIFI:S:Network Name;T:WAP;P:Something;H:false;;",
		codeFormat = BarCodeFormat.QR_CODE,
		rect = null
	)

	val FAKE_QR_CODE_URL = RecognizedBarcode(
		type = BarCodeTypes.UrlBookMark(title = "Somthing", url = "https://google.in"),
		rawString = "https://google.in",
		displayText = "",
		rect = null,
		codeFormat = BarCodeFormat.QR_CODE
	)

	val FAKE_BAR_CODE_TEXT =
		RecognizedBarcode(
			type = BarCodeTypes.Text("This is an awesome data based QR code"),
			displayText = "This is an awesome data based QR code",
			rawString = "This is an awesome data based QR code",
			codeFormat = BarCodeFormat.QR_CODE,
			rect = null
		)

	val FAKE_QR_CODE_EMAIL = RecognizedBarcode(
		type = BarCodeTypes.Email(
			subject = "Hey how are you",
			body = "this is an qr to check email data",
			address = "something@some.com"
		),
		displayText = "tuhinbhommick2513@gmail.com",
		rawString = "mailto:something@some.com?subject=Hey how are you&body=this is an qr to check email data",
		codeFormat = BarCodeFormat.QR_CODE,
		rect = null
	)

	val FAKE_QR_CODE_GEO_POINT = RecognizedBarcode(
		type = BarCodeTypes.GeoPoint(23.67, 65.89),
		displayText = "geo:23.67,65.89",
		rawString = "geo:23.67,65.89",
		codeFormat = BarCodeFormat.QR_CODE,
		rect = null
	)

	val FAKE_QR_CODE_PHONE = RecognizedBarcode(
		type = BarCodeTypes.Phone("+911234567890"),
		displayText = "+911234567890",
		rawString = "tel:+911234567890",
		codeFormat = BarCodeFormat.QR_CODE,
		rect = null
	)

	val FAKE_QR_CODE_SMS = RecognizedBarcode(
		type = BarCodeTypes.Sms(phoneNumber = "+911234567890", message = "Hi!!"),
		displayText = "+911234567890\nHi!!",
		rawString = "SMSTO:1234567890:Hi!!",
		codeFormat = BarCodeFormat.QR_CODE,
		rect = null
	)

	val FAKE_IMAGE_DATA_MODELS = List(10) {
		FAKE_IMAGE_DATA_MODEL.copy(id = it.toLong())
	}

	val FAKE_PAGED_DATA = MutableStateFlow(PagingData.from(FAKE_IMAGE_DATA_MODELS))
}