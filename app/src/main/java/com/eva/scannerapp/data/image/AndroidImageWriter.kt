package com.eva.scannerapp.data.image

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.eva.scannerapp.domain.image.ImageFileWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.time.Instant

class AndroidImageWriter(
	private val context: Context
) : ImageFileWriter {

	private val volumeUri: Uri
		get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
			MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
		else MediaStore.Images.Media.EXTERNAL_CONTENT_URI

	private val contentResolver: ContentResolver
		get() = context.contentResolver

	private val localCacheDirectory: File
		get() = File(context.cacheDir, "capture-cache").apply {
			if (!exists()) mkdirs()
		}

	override suspend fun createExternalFile(bitmap: Bitmap, quality: Int): String? {

		val time = Instant.now().toEpochMilli()
		val fileName = "capture-photo-image-$time.png"
		val picturesDir = Environment.DIRECTORY_PICTURES

		return withContext(Dispatchers.IO) {

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

				val contentValues = ContentValues()
					.apply {
						put(MediaStore.Files.FileColumns.RELATIVE_PATH, picturesDir)
						put(MediaStore.Files.FileColumns.TITLE, fileName)
						put(MediaStore.Files.FileColumns.DISPLAY_NAME, fileName)
						put(MediaStore.Files.FileColumns.MIME_TYPE, "image/png")
						put(MediaStore.Files.FileColumns.DATE_ADDED, System.currentTimeMillis())
						put(MediaStore.Files.FileColumns.DATE_TAKEN, System.currentTimeMillis())
						put(MediaStore.Files.FileColumns.IS_PENDING, 1)
					}

				val newFileUri = contentResolver.insert(volumeUri, contentValues)
					?: return@withContext null

				try {
					contentResolver.openOutputStream(newFileUri, "w")?.use { stream ->
						bitmap.compress(Bitmap.CompressFormat.PNG, quality, stream)
					}

					val updatedValues = ContentValues().apply {
						put(MediaStore.Files.FileColumns.IS_PENDING, 0)
						put(
							MediaStore.Files.FileColumns.DATE_MODIFIED,
							System.currentTimeMillis()
						)
					}
					// update the file content
					contentResolver.update(newFileUri, updatedValues, null, null)
					newFileUri.toString()
				} catch (e: Exception) {
					e.printStackTrace()
					contentResolver.delete(newFileUri, null, null)
					null
				}
			} else {
				val fileSaveDir = Environment.getExternalStoragePublicDirectory(picturesDir)

				val file = File(fileSaveDir, fileName)
				file.outputStream().use { stream ->
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
				}
				file.toUri().toString()
			}
		}
	}

	override suspend fun createLocalCacheFile(bitmap: Bitmap, quality: Int): String? {
		val time = Instant.now().toEpochMilli()
		val fileName = "capture-photo-image-$time.png"
		// create the capture-cache-dir if-required
		return withContext(Dispatchers.IO) {
			try {
				val file = File(localCacheDirectory, fileName)
				file.outputStream().use { stream ->
					bitmap.compress(Bitmap.CompressFormat.PNG, quality, stream)
				}
				file.toUri().toString()
			} catch (e: Exception) {
				e.printStackTrace()
				return@withContext null
			}
		}
	}

	override suspend fun deleteLocalCache(fileUri: String): Boolean = withContext(Dispatchers.IO) {
		fileUri.toUri().toFile().delete()
	}


	override suspend fun clearLocalCache(): Boolean = withContext(Dispatchers.IO) {
		localCacheDirectory.deleteRecursively()
	}

}