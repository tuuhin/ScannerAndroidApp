package com.eva.scannerapp.data.repository

import com.eva.scannerapp.domain.image.ImageFileReader
import com.eva.scannerapp.domain.models.ImageDataModel
import com.eva.scannerapp.domain.repository.ImageReaderRepo
import com.eva.scannerapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ImageReaderRepoImpl @Inject constructor(
	private val reader: ImageFileReader
) : ImageReaderRepo {

	override val lastSavedImage: Flow<Resource<ImageDataModel>>
		get() = flow {
			emit(Resource.Loading())
			try {
				val readResult = reader.readLastSavedImage()
				emit(Resource.Success(data = readResult))
			} catch (e: Exception) {
				e.printStackTrace()
				emit(Resource.Error(e))
			}
		}
}