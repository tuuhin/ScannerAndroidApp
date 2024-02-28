package com.eva.scannerapp.domain.image.models

data class ImageDataModel(
	val id: Long,
	val fileSize: String,
	val bucketModel: ImageBucketModel,
	val imageUri: String,
)
