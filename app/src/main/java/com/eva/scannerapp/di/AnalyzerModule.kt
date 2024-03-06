package com.eva.scannerapp.di

import com.eva.scannerapp.data.ml.analyser.BarCodeAnalyser
import com.eva.scannerapp.data.ml.analyser.ImageLabelAnalyser
import com.eva.scannerapp.data.ml.feed.CameraFeedImageAnalyzer
import com.eva.scannerapp.data.ml.feed.FeedAnalyzers
import com.eva.scannerapp.domain.ml.MLModelAnalyzer
import com.eva.scannerapp.domain.ml.models.RecognizedBarcode
import com.eva.scannerapp.domain.ml.models.RecognizedLabel
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
	abstract fun bindsLabelAnalyser(analyser: ImageLabelAnalyser)
			: MLModelAnalyzer<RecognizedLabel>

	@Binds
	@Singleton
	abstract fun bindsBarCodeAnalyser(analyser: BarCodeAnalyser)
			: MLModelAnalyzer<RecognizedBarcode>

	@Binds
	@Singleton
	abstract fun bindsBarCodeFeedAnalyzer(feedAnalyzer: FeedAnalyzers.BarCode)
			: CameraFeedImageAnalyzer<RecognizedBarcode>

	@Binds
	@Singleton
	abstract fun bindsLabelsFeedAnalyzer(feedAnalyzer: FeedAnalyzers.Labels)
			: CameraFeedImageAnalyzer<RecognizedLabel>

}