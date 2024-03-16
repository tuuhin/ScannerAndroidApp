package com.eva.scannerapp.di

import android.content.Context
import com.eva.scannerapp.data.ml.analyser.BarCodeAnalyser
import com.eva.scannerapp.data.ml.analyser.ImageLabelAnalyser
import com.eva.scannerapp.data.repository.ImageRepositoryImpl
import com.eva.scannerapp.data.repository.StaticAnalyzerRepoImpl
import com.eva.scannerapp.domain.image.ImageFileReader
import com.eva.scannerapp.domain.image.ImageFileWriter
import com.eva.scannerapp.domain.repository.ImageRepository
import com.eva.scannerapp.domain.repository.StaticImageAnalyzerRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

	@Provides
	@ViewModelScoped
	fun bindsReaderRepo(
		reader: ImageFileReader,
		writer: ImageFileWriter,
	): ImageRepository = ImageRepositoryImpl(reader = reader, writer = writer)

	@Provides
	@ViewModelScoped
	fun providesStaticAnalyzer(
		@ApplicationContext context: Context,
		barCodeAnalyser: BarCodeAnalyser,
		labelAnalyser: ImageLabelAnalyser,
	): StaticImageAnalyzerRepo = StaticAnalyzerRepoImpl(
		context = context,
		barCodeAnalyser = barCodeAnalyser,
		labelAnalyser = labelAnalyser
	)

}