package com.eva.scannerapp.data.image

import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.Formatter
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.database.getStringOrNull
import com.eva.scannerapp.domain.image.ImageFileReader
import com.eva.scannerapp.domain.image.exceptions.FileReadPermissionNotFoundException
import com.eva.scannerapp.domain.image.exceptions.QueriedFileNotFoundException
import com.eva.scannerapp.domain.image.models.ImageBucketModel
import com.eva.scannerapp.domain.image.models.ImageDataModel
import com.eva.scannerapp.util.PagedResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class AndroidImageReader(
	private val context: Context
) : ImageFileReader {

	private val contentResolver: ContentResolver
		get() = context.applicationContext.contentResolver

	private val volumeUri: Uri
		get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
			MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
		else MediaStore.Images.Media.EXTERNAL_CONTENT_URI

	private val readPermissions: String
		get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
			Manifest.permission.READ_MEDIA_IMAGES
		else Manifest.permission.READ_EXTERNAL_STORAGE

	private val projection: Array<String>
		get() = arrayOf(
			MediaStore.Images.ImageColumns._ID,
			MediaStore.Images.ImageColumns.BUCKET_ID,
			MediaStore.Images.ImageColumns.TITLE,
			MediaStore.Images.ImageColumns.DESCRIPTION,
			MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
			MediaStore.Images.ImageColumns.SIZE,
			MediaStore.Images.ImageColumns.DATA,
			MediaStore.Images.ImageColumns.DATE_MODIFIED
		)


	override val isFullReadPermissionGranted: Boolean
		get() = ContextCompat.checkSelfPermission(
			context.applicationContext,
			readPermissions
		) == PermissionChecker.PERMISSION_GRANTED

	override val isPartialReadAllowed: Boolean
		get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
			ContextCompat.checkSelfPermission(
				context,
				Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
			) == PermissionChecker.PERMISSION_GRANTED
		else false


	override suspend fun readImagesPaged(page: Int, pageSize: Int): PagedResource<ImageDataModel> {

		val isPartialOrFullRead = isPartialReadAllowed || isFullReadPermissionGranted

		if (!isPartialOrFullRead) throw FileReadPermissionNotFoundException()

		val offset = if (page >= 0) page * pageSize else 0

		val bundle = Bundle().apply {
			//sort columns
			putStringArray(
				ContentResolver.QUERY_ARG_SORT_COLUMNS,
				arrayOf(MediaStore.Images.ImageColumns.DATE_MODIFIED)
			)
			//sort directions
			putInt(
				ContentResolver.QUERY_ARG_SORT_DIRECTION,
				ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
			)
			// limit, offset
			putInt(ContentResolver.QUERY_ARG_LIMIT, pageSize)
			putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
		}

		val sortOrder = "${MediaStore.Images.ImageColumns.DATE_MODIFIED} DESC"
		val limitOffset = " LIMIT $pageSize OFFSET $offset"

		return withContext(Dispatchers.IO) {
			val cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
				contentResolver.query(volumeUri, projection, bundle, null)
			} else contentResolver.query(
				volumeUri,
				projection,
				null,
				null,
				sortOrder + limitOffset,
				null
			)

			cursor?.use {
				val results = fetchModelsFromCursor(cursor)
				val next = if (cursor.count > 0) page + 1 else null
				PagedResource(previous = page, next = next, results = results)
			} ?: PagedResource(results = emptyList())
		}
	}

	override suspend fun readImage(): List<ImageDataModel> {
		if (!isFullReadPermissionGranted) throw FileReadPermissionNotFoundException()

		val bundle = Bundle().apply {
			//sort columns
			putStringArray(
				ContentResolver.QUERY_ARG_SORT_COLUMNS,
				arrayOf(MediaStore.Images.ImageColumns.DATE_MODIFIED)
			)
			//sort directions
			putInt(
				ContentResolver.QUERY_ARG_SORT_DIRECTION,
				ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
			)
		}

		val sortOrder = "${MediaStore.Images.ImageColumns.DATE_MODIFIED} DESC"

		return withContext(Dispatchers.IO) {
			val cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
				contentResolver.query(volumeUri, projection, bundle, null)
			} else contentResolver.query(
				volumeUri,
				projection,
				null,
				null,
				sortOrder,
				null
			)

			cursor?.use { fetchModelsFromCursor(cursor) }
				?: emptyList()
		}
	}

	override suspend fun readImageCount(): Int {
		if (!isFullReadPermissionGranted) throw FileReadPermissionNotFoundException()

		return withContext(Dispatchers.IO) {
			val cursor = contentResolver.query(
				volumeUri,
				arrayOf(MediaStore.Images.ImageColumns._ID),
				null,
				null
			)
			cursor?.use { it.count } ?: 0
		}
	}

	override val readLastImageAsFlow: Flow<ImageDataModel?> = callbackFlow {

		val scope = CoroutineScope(Dispatchers.IO)
		var job: Job? = null

		val observer = object : ContentObserver(null) {
			override fun onChange(selfChange: Boolean) {
				super.onChange(selfChange)
				job?.cancel()
				job = scope.launch {
					Log.d("ANDROID_IMAGE_READER", "NEW_EMIT")
					val image = readLastSavedImage()
					send(image)
				}
			}
		}
		Log.d("ANDROID_IMAGE_READER", "OBSERVER ADDED")
		contentResolver.registerContentObserver(volumeUri, true, observer)
		//send the first edition
		val firstEmit = readLastSavedImage()
		trySend(firstEmit)
		Log.d("ANDROID_IMAGE_READER", "FIRST_EMIT")

		awaitClose {
			Log.d("ANDROID_IMAGE_READER", "OBSERVER_REMOVED")
			contentResolver.unregisterContentObserver(observer)
			scope.cancel()
		}
	}


	override suspend fun readLastSavedImage(): ImageDataModel {
		if (!isFullReadPermissionGranted) throw FileReadPermissionNotFoundException()

		val bundle = Bundle().apply {
			//sort columns
			putStringArray(
				ContentResolver.QUERY_ARG_SORT_COLUMNS,
				arrayOf(MediaStore.Images.ImageColumns.DATE_MODIFIED)
			)
			//sort directions
			putInt(
				ContentResolver.QUERY_ARG_SORT_DIRECTION,
				ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
			)
			// limit 1
			putInt(ContentResolver.QUERY_ARG_LIMIT, 1)
		}

		val sortOrder = "${MediaStore.Images.ImageColumns.DATE_MODIFIED} DESC"
		val limitOffset = " LIMIT 1"

		return withContext(Dispatchers.IO) {
			val cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
				contentResolver.query(volumeUri, projection, bundle, null)
			} else contentResolver.query(
				volumeUri,
				projection,
				null,
				null,
				sortOrder + limitOffset,
				null
			)
			cursor
				?.use { fetchModelsFromCursor(cursor).firstOrNull() }
				?: throw QueriedFileNotFoundException()
		}
	}

	private fun fetchModelsFromCursor(cursor: Cursor): List<ImageDataModel> {
		val results = mutableListOf<ImageDataModel>()
		val imageIdField = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
		val dataField = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA)
		val sizeField = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.SIZE)
		val bucketIdField = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_ID)
		val bucketNameField =
			cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME)
		val imageDescriptionField =
			cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DESCRIPTION)
		val titleField = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.TITLE)

		while (cursor.moveToNext()) {
			val data = cursor.getString(dataField)
			val file = File(data)

			if (!file.exists()) continue
			val title = cursor.getString(titleField)
			val description = cursor.getStringOrNull(imageDescriptionField)
			val imageId = cursor.getLong(imageIdField)
			val fileSize = cursor.getLong(sizeField)
			val bucketId = cursor.getLong(bucketIdField)
			val bucketName = cursor.getString(bucketNameField)
			val imageUri = ContentUris.withAppendedId(volumeUri, imageId)
			//formatted file size
			val formattedFileSize = Formatter.formatShortFileSize(context, fileSize)
			// models
			val bucket = ImageBucketModel(bucketId = bucketId, bucketName = bucketName)
			val imageModel = ImageDataModel(
				id = imageId,
				title = title,
				description = description,
				fileSize = formattedFileSize,
				bucketModel = bucket,
				imageUri = "$imageUri"
			)

			results.add(imageModel)
		}
		return results
	}
}