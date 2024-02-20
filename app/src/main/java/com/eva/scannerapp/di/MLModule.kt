package com.eva.scannerapp.di

import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MLModule {

	@get:Provides
	@Singleton
	val scannerOptions: BarcodeScannerOptions = BarcodeScannerOptions.Builder()
		.enableAllPotentialBarcodes()
		.build()


	@get:Provides
	@Singleton
	val labelerOptions: ImageLabelerOptions = ImageLabelerOptions.Builder()
		.setConfidenceThreshold(.7f)
		.build()

	@Provides
	@Singleton
	fun providesScannerClient(options: BarcodeScannerOptions): BarcodeScanner =
		BarcodeScanning.getClient(options)


	@Provides
	@Singleton
	fun providesImageLabeler(options: ImageLabelerOptions): ImageLabeler =
		ImageLabeling.getClient(options)

}