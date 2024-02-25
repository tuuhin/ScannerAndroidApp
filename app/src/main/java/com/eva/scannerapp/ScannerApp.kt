package com.eva.scannerapp

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.size.Precision
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers

@HiltAndroidApp
class ScannerApp : Application(), ImageLoaderFactory {
	override fun newImageLoader(): ImageLoader {

		val memoryCache = MemoryCache.Builder(this)
			.maxSizePercent(.2)
			.maxSizeBytes(100 * 1024)
			.build()

		val diskCache = DiskCache.Builder()
			.cleanupDispatcher(Dispatchers.Default)
			.directory(cacheDir)
			.minimumMaxSizeBytes(1024 * 1024L)
			.maxSizePercent(.7)
			.build()

		val debugLogger = DebugLogger()

		return ImageLoader(this).newBuilder()
			.crossfade(true)
			.crossfade(400)
			.decoderDispatcher(Dispatchers.Default)
			.transformationDispatcher(Dispatchers.Default)
			.memoryCachePolicy(CachePolicy.ENABLED)
			.precision(Precision.EXACT)
			.diskCachePolicy(CachePolicy.ENABLED)
			.diskCache(diskCache)
			.memoryCache(memoryCache)
			.logger(debugLogger)
			.build()
	}
}