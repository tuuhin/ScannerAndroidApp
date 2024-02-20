package com.eva.scannerapp.di

import com.eva.scannerapp.data.ml.BarCodeAnalyser
import com.eva.scannerapp.data.ml.ImageAnalyzer
import com.eva.scannerapp.data.ml.ImageLabelAnalyser
import com.eva.scannerapp.domain.models.RecognizedBarcode
import com.eva.scannerapp.domain.models.RecognizedLabel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyzerModule {
	@Binds
	@Singleton
	abstract fun bindsLabelAnalyser(impl: ImageLabelAnalyser): ImageAnalyzer<List<RecognizedLabel>>

	@Binds
	@Singleton
	abstract fun bindsBarCodeAnalyser(impl: BarCodeAnalyser): ImageAnalyzer<List<RecognizedBarcode>>

}