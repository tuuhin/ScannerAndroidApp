package com.eva.scannerapp.domain.repository

import com.eva.scannerapp.domain.ml.models.RecognizedBarcode
import com.eva.scannerapp.domain.ml.models.RecognizedLabel
import com.eva.scannerapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StaticImageAnalyzerRepo {

	fun prepareBarCodeResults(file: String): Flow<Resource<List<RecognizedBarcode>>>

	fun prepareLabelsResults(file: String): Flow<Resource<List<RecognizedLabel>>>

}