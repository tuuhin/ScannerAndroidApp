package com.eva.scannerapp.data.repository

import android.content.Context
import androidx.core.net.toUri
import com.eva.scannerapp.data.ml.analyser.BarCodeAnalyser
import com.eva.scannerapp.data.ml.analyser.ImageLabelAnalyser
import com.eva.scannerapp.domain.ml.MLResource
import com.eva.scannerapp.domain.ml.models.RecognizedBarcode
import com.eva.scannerapp.domain.ml.models.RecognizedLabel
import com.eva.scannerapp.domain.repository.StaticImageAnalyzerRepo
import com.eva.scannerapp.util.Resource
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StaticAnalyzerRepoImpl(
	private val context: Context,
	private val barCodeAnalyser: BarCodeAnalyser,
	private val labelAnalyser: ImageLabelAnalyser,
) : StaticImageAnalyzerRepo {

	override fun prepareBarCodeResults(file: String): Flow<Resource<List<RecognizedBarcode>>> {
		return flow {
			try {

				val inputImage = InputImage.fromFilePath(context, file.toUri())
				emit(Resource.Loading())

				when (val successOrError = barCodeAnalyser.analyseImage(inputImage)) {
					is MLResource.Empty -> emit(Resource.Success(data = emptyList()))
					is MLResource.Error -> emit(Resource.Error(error = successOrError.error))
					is MLResource.Success -> emit(Resource.Success(data = successOrError.data))
				}
			} catch (e: Exception) {

				e.printStackTrace()
				emit(Resource.Error(error = e))
			}
		}
	}

	override fun prepareLabelsResults(file: String): Flow<Resource<List<RecognizedLabel>>> {
		return flow {
			try {

				val inputImage = InputImage.fromFilePath(context, file.toUri())
				emit(Resource.Loading())

				when (val successOrError = labelAnalyser.analyseImage(inputImage)) {
					is MLResource.Empty -> emit(Resource.Success(data = emptyList()))
					is MLResource.Error -> emit(Resource.Error(error = successOrError.error))
					is MLResource.Success -> emit(Resource.Success(data = successOrError.data))
				}

			} catch (e: Exception) {
				e.printStackTrace()
				emit(Resource.Error(error = e))
			}
		}
	}

}