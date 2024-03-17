package com.eva.scannerapp.data.repository

import android.graphics.Bitmap
import com.eva.scannerapp.domain.image.ImageFileReader
import com.eva.scannerapp.domain.image.ImageFileWriter
import com.eva.scannerapp.domain.image.exceptions.FileReadPermissionNotFoundException
import com.eva.scannerapp.domain.image.exceptions.QueriedFileNotFoundException
import com.eva.scannerapp.domain.image.models.ImageDataModel
import com.eva.scannerapp.domain.preferences.SettingsPreferences
import com.eva.scannerapp.domain.repository.ImageRepository
import com.eva.scannerapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
	private val reader: ImageFileReader,
	private val writer: ImageFileWriter,
	private val settings: SettingsPreferences,
) : ImageRepository {

	override val lastSavedImage: Flow<Resource<ImageDataModel?>>
		get() = flow {
			emit(Resource.Loading())
			try {
				val readResult = reader.readLastSavedImage()
				emit(Resource.Success(data = readResult))
			} catch (e: FileReadPermissionNotFoundException) {
				emit(Resource.Success(data = null))
			} catch (e: QueriedFileNotFoundException) {
				emit(Resource.Success(data = null))
			} catch (e: Exception) {
				e.printStackTrace()
				emit(Resource.Error(e))
			}
		}

	override fun saveCaptureContent(image: Bitmap): Flow<Resource<String?>> {
		return flow {
			emit(Resource.Loading())
			try {
				val isSaveToExternal = settings.isSaveToExternalAllowed.first()

				val result = if (isSaveToExternal) writer.createLocalCacheFile(image)
				else writer.createLocalCacheFile(image)

				emit(Resource.Success(data = result))
			} catch (e: Exception) {
				emit(Resource.Error(e))
			}
		}
	}

	override suspend fun deleteCapturedImage(fileUri: String): Resource<Boolean> {
		return try {
			val delete = writer.deleteLocalCache(fileUri)
			Resource.Success(data = delete)
		} catch (e: Exception) {
			Resource.Error(e)
		}
	}

	override suspend fun deleteAllLocalCache(): Resource<Boolean> {
		return try {
			val result = writer.clearLocalCache()
			Resource.Success(result)
		} catch (e: Exception) {
			Resource.Error(e)
		}
	}
}