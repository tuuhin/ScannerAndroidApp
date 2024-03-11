package com.eva.scannerapp.domain.image

import com.eva.scannerapp.domain.image.models.ImageDataModel
import com.eva.scannerapp.util.PagedResource

interface ImageFileReader {

	val isFullReadPermissionGranted: Boolean

	val isPartialReadAllowed: Boolean

	suspend fun readImagesPaged(page: Int, pageSize: Int): PagedResource<ImageDataModel>

	suspend fun readImage(): List<ImageDataModel>

	suspend fun readImageCount(): Int

	suspend fun readLastSavedImage(): ImageDataModel

}