package com.eva.scannerapp.di

import android.content.Context
import androidx.paging.Pager
import com.eva.scannerapp.data.image.AndroidImageReader
import com.eva.scannerapp.data.image.AndroidImageWriter
import com.eva.scannerapp.data.image.PagedImageReader
import com.eva.scannerapp.domain.image.ImageFileReader
import com.eva.scannerapp.domain.image.ImageFileWriter
import com.eva.scannerapp.domain.image.models.ImageDataModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ImageModule {

	@Provides
	@Singleton
	fun providesAndroidImageReader(@ApplicationContext context: Context): ImageFileReader =
		AndroidImageReader(context)

	@Provides
	@Singleton
	fun providesAndroidImageWriter(@ApplicationContext context: Context): ImageFileWriter =
		AndroidImageWriter(context)

	@Provides
	@Singleton
	fun providesImagePager(reader: ImageFileReader): Pager<Int, ImageDataModel> =
		PagedImageReader.createPager(reader)
}