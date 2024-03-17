package com.eva.scannerapp.domain.image

import android.graphics.Bitmap

interface ImageFileWriter {

	/**
	 * Creates a cached image file from the provided [Bitmap]
	 * @return fileUri of the created cached file
	 */
	suspend fun createLocalCacheFile(bitmap: Bitmap, quality: Int = 50): String?

	/**
	 * Creates an external image file from the provided [Bitmap]
	 * @return fileUri as string of the saved file
	 */
	suspend fun createExternalFile(bitmap: Bitmap, quality: Int = 50): String?

	/**
	 * Clears the cached image saved in the local cache for the app
	 */
	suspend fun deleteLocalCache(fileUri: String): Boolean

	/**
	 * Clear all the capture cache subdirectory
	 */
	suspend fun clearLocalCache(): Boolean
}