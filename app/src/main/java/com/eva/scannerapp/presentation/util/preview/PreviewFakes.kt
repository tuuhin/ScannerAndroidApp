package com.eva.scannerapp.presentation.util.preview

import androidx.paging.PagingData
import com.eva.scannerapp.domain.image.models.ImageBucketModel
import com.eva.scannerapp.domain.image.models.ImageDataModel
import kotlinx.coroutines.flow.MutableStateFlow

object PreviewFakes {

	val FAKE_IMAGE_BUCKET_MODEL =
		ImageBucketModel(bucketId = 1, bucketName = "Preview Bucket")

	val FAKE_IMAGE_DATA_MODEL = ImageDataModel(
		id = 0,
		fileSize = "2kb",
		title = "THIS IS WONDERFUL",
		bucketModel = FAKE_IMAGE_BUCKET_MODEL,
		imageUri = ""
	)

	val FAKE_IMAGE_DATA_MODELS = List(10) {
		FAKE_IMAGE_DATA_MODEL.copy(id = it.toLong())
	}

	val FAKE_PAGED_DATA = MutableStateFlow(PagingData.from(FAKE_IMAGE_DATA_MODELS))
}