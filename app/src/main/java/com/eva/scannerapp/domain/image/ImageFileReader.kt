package com.eva.scannerapp.domain.image

import com.eva.scannerapp.domain.models.ImageBucketModel
import com.eva.scannerapp.domain.models.ImageDataModel
import com.eva.scannerapp.util.PagedResource

interface ImageFileReader {

	suspend fun readImages(page: Int, pageSize: Int): PagedResource<ImageDataModel>

	suspend fun readBuckets(): List<ImageBucketModel>

	suspend fun readLastSavedImage(): ImageDataModel

	suspend fun readImagesFromBucket(bucketId: Long): List<ImageDataModel>
}