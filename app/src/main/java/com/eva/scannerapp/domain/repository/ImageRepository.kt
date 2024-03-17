package com.eva.scannerapp.domain.repository

import android.graphics.Bitmap
import com.eva.scannerapp.domain.image.models.ImageDataModel
import com.eva.scannerapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

	val lastSavedImage: Flow<Resource<ImageDataModel?>>

	fun saveCaptureContent(image: Bitmap): Flow<Resource<String?>>

	suspend fun deleteCapturedImage(fileUri: String): Resource<Boolean>

	suspend fun deleteAllLocalCache(): Resource<Boolean>
}