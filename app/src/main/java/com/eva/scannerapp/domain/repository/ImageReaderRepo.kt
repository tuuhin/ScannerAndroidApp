package com.eva.scannerapp.domain.repository

import com.eva.scannerapp.domain.image.models.ImageDataModel
import com.eva.scannerapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface ImageReaderRepo {

	val lastSavedImage: Flow<Resource<ImageDataModel>>
}